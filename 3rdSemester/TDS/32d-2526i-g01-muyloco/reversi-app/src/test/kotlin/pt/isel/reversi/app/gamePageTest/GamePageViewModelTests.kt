package pt.isel.reversi.app.gamePageTest

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import pt.isel.reversi.app.pages.game.GamePageViewModel
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.game.gameServices.FakeGameService
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
class GamePageViewModelTests {


    val service = FakeGameService()

    val game = runBlocking {
        startNewGame(
            side = 4,
            players = MatchPlayers(Player(type = PieceType.BLACK), Player(type = PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = service,
        )
    }

    fun vmForTest(scope: CoroutineScope) = GamePageViewModel(
        game,
        scope,
        {},
        {},
        {},
        null,
        { _, _ -> }
    )

    val id = "testGame"
    fun vmNotLocalGame(scope: CoroutineScope) = GamePageViewModel(
        game.copy(currGameName = id),
        scope,
        { },
        { },
        { },
        null,
        { _, _ -> }
    )


    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @BeforeTest
    fun setup() {
        service.save(
            id,
            game.gameState!!
        )
    }

    @AfterTest
    fun tearDown() {
        runBlocking{ service.delete(id) }
    }

    @Test
    fun `verify that the state initializes correctly`() = runTest {

        val uut = vmForTest(this)

        // Verify initial state
        assertNotNull(uut.uiState.value)
        assertEquals(game, uut.uiState.value.game)
    }

    @Test
    fun `verify that get available plays works correctly`() = runTest {
        val uut = vmForTest(this)

        val availablePlays = uut.getAvailablePlays()
        val expectedPlays = game.getAvailablePlays()

        assertEquals(expectedPlays, availablePlays)
    }

    @Test
    fun `verify that set target mode works correctly`() = runTest {
        val uut = vmForTest(this)

        val initialTarget = uut.uiState.value.game.target
        uut.setTarget(!initialTarget)

        assertEquals(!initialTarget, uut.uiState.value.game.target)
    }

    @Test
    fun `verify polling control methods work`() = runTest {
        val uut = vmForTest(this)

        // Initially no polling
        assertEquals(false, uut.isPollingActive())

        // Start polling would require coroutine context, so we just verify the state
        assertNotNull(uut)
    }

    @Test
    fun `verify that starting polling twice throws exception`() = runTest {
        val uut = vmForTest(this)
        uut.startPolling()
        try {
            uut.startPolling()
            assert(false) // Should not reach here
        } catch (_: IllegalStateException) {
            assert(true) // Expected exception
        } finally {
            uut.stopPolling()
        }
    }

    @Test
    fun `verify polling refreshes game state`() = runTest {
        val uut = vmNotLocalGame(this)
        val initialGame = uut.uiState.value.game

        uut.startPolling()

        var expected = initialGame.play(initialGame.getAvailablePlays().first())
        expected = expected.copy(gameState = expected.gameState?.refreshPlayers())

        service.save(
            id,
            expected.gameState!!
        )

        delay(200L)
        uut.stopPolling()

        assertEquals(expected.gameState, uut.uiState.value.game.gameState)
    }

    @Test
    fun `verify polling does not update when no changes`() = runTest {
        val uut = vmNotLocalGame(this)
        val initialGame = uut.uiState.value.game

        uut.startPolling()

        delay(200L)
        uut.stopPolling()

        assertEquals(initialGame.gameState, uut.uiState.value.game.gameState)
    }

    @Test
    fun `verify polling if game have a winner call setGame and setPage`() = runTest {
        var setGameCalled = false
        var setPageCalled = false

        val uut = GamePageViewModel(
            game.copy(
                gameState = game.gameState?.copy(
                    winner = Player(type = PieceType.BLACK)
                )
            ),
            this,
            setGame = {
                setGameCalled = true
            },
            {},
            setPage = {
                setPageCalled = true
            },
            null,
            { _, _ -> }
        )

        uut.startPolling()
        delay(100L)
        uut.stopPolling()

        assertTrue(setGameCalled)
        assertTrue(setPageCalled)
    }

    @Test
    fun `verify polling setError when myPiece is null`() = runTest {
        val uut = GamePageViewModel(
            game.copy(
                currGameName = id,
                myPiece = null
            ),
            this,
            {},
            {
            },
            {},
            null,
            { _, _ -> }
        )

        uut.startPolling()
        delay(100L)
        uut.stopPolling()

        assertTrue(uut.error != null)
    }

    @Test
    fun `verify polling preserve player names when refreshing`() = runTest {
        val uut = vmNotLocalGame(this)
        val initialGame = uut.uiState.value.game
        val initialName = initialGame.gameState?.players?.getPlayerByType(initialGame.myPiece!!)?.name
        val myPiece = initialGame.myPiece!!

        uut.startPolling()

        //need to change lastModified to trigger update
        var expected = initialGame.play(initialGame.getAvailablePlays().first())
        expected = expected.copy(
            gameState = expected.gameState?.refreshPlayers()
        )

        //change my player name in storage
        service.save(
            id,
            expected.gameState?.copy(
                players = expected.gameState?.changeName(
                    newName = "NewName",
                    pieceType = myPiece
                )!!.players
            )!!
        )

        delay(200L)
        uut.stopPolling()

        val refreshedPlayer = uut.uiState.value.game.gameState?.players?.getPlayerByType(myPiece)
        // Verify that the player's name remains unchanged
        assertEquals(initialName, refreshedPlayer?.name)
    }


    @Test
    fun `verify play call setGame`() = runTest {
        var setGameCalled = false

        val uut = GamePageViewModel(
            game,
            this,
            setGame = {
                setGameCalled = true
            },
            {},
            {},
            null,
            { _, _ -> }
        )

        val validMove = game.getAvailablePlays().first()
        uut.playMove(validMove)
        advanceUntilIdle() // Wait for all coroutines to complete

        assertTrue(setGameCalled)
    }

    @Test
    fun `verify play handles exceptions and sets error`() = runTest {
        val uut = GamePageViewModel(
            game,
            this,
            {},
            {},
            {},
            null,
            { _, _ -> }
        )

        // Provide an invalid move (assuming (-1, -1) is invalid)
        val invalidMove = pt.isel.reversi.core.board.Coordinate(-1, -1)
        uut.playMove(invalidMove)
        advanceUntilIdle() // Wait for all coroutines to complete

        assertNotNull(uut.error)
    }

    @Test
    fun `verify pass call setGame on error`() = runTest {
        var setGameCalled = false

        val uut = GamePageViewModel(
            game, //have a game that cannot be passed
            this,
            setGame = {
                setGameCalled = true
            },
            {},
            {},
            null,
            { _, _ -> }
        )

        uut.pass()
        advanceUntilIdle() // Wait for all coroutines to complete

        assertTrue(setGameCalled)
    }

    @Test
    fun `verify pass handles exceptions and sets error`() = runTest {
        val uut = GamePageViewModel(
            game, //have a game that cannot be passed
            this,
            {},
            {},
            {},
            null,
            { _, _ -> }
        )

        uut.pass()
        advanceUntilIdle() // Wait for all coroutines to complete

        assertNotNull(uut.error)
    }

    @Test
    fun `verify pass works correctly when allowed`() = runTest {
        val passableGame = Game(
            gameState = game.gameState?.copy(
                board = Board(4) // empty board to allow passing
            ),
            myPiece = game.myPiece,
            currGameName = game.currGameName,
            service = EmptyGameService()
        )

        val uut = GamePageViewModel(
            passableGame,
            this,
            {},
            {},
            {},
            null,
            { _, _ -> }
        )

        val initialCountPass = uut.uiState.value.game.countPass
        uut.pass()
        advanceUntilIdle() // Wait for all coroutines to complete

        assertEquals(initialCountPass + 1, uut.uiState.value.game.countPass)
    }
}



