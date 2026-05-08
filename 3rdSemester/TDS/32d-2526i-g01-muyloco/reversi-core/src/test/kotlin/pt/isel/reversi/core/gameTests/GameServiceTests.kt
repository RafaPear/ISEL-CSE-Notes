package pt.isel.reversi.core.gameTests

import kotlinx.coroutines.test.runTest
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.InvalidFile
import pt.isel.reversi.core.exceptions.InvalidGame
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.game.loadAndEntryGame
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.core.storage.StorageParams
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.*

class GameServiceTests {
    val gameService = GameService(
        storage = GameStorageType.FILE_STORAGE,
        params = StorageParams.FileStorageParams(folder = "test-saves")
    )

    @BeforeTest
    @AfterTest
    fun cleanup() {
        File("test-saves").deleteRecursively()
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `refresh in not local game loads updated game state`() = runTest {
        var uutB = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK)),
            firstTurn = PieceType.WHITE,
            currGameName = "testGame",
            service = gameService,
        )

        var uutW = loadAndEntryGame(
            gameName = "testGame",
            desiredType = null,
            service = gameService,
        )

        uutW = uutW.play(uutW.getAvailablePlays().first())

        uutB = uutB.refresh()
        assertEquals(uutW.gameState, uutB.gameState)
    }

    @Test
    fun `saveEndGame in not local game succeeds`() = runTest {

        val uut = startNewGame(
            players = MatchPlayers(Player(PieceType.BLACK), Player(PieceType.WHITE)),
            firstTurn = PieceType.WHITE,
            currGameName = "testGame",
            side = 4,
            service = gameService,
        )

        uut.saveEndGame()

        val loadedGameState = uut.service.hardLoad("testGame")

        val expectedGameState = uut.gameState?.copy(
            players = MatchPlayers(Player(PieceType.BLACK)).refreshPlayers(uut.gameState.board),
        )

        assertEquals(expectedGameState, loadedGameState)
    }

    @Test
    fun `saveOnlyBoard in not local game, verify if my name is preserved when my name is changed`() = runTest {

        val initialMyPlayer = Player(PieceType.BLACK, name = "Player 1")
        var initialPlayer2 = Player(PieceType.WHITE, name = "Player 2")
        var uut = startNewGame(
            side = 4,
            players = MatchPlayers(initialMyPlayer, initialPlayer2),
            firstTurn = initialMyPlayer.type,
            currGameName = "testGame",
            service = gameService,
        )

        val expectedMyPlayer =
            initialMyPlayer.copy(name = "Changed Name").refresh(uut.gameState!!.board) // Refresh points
        initialPlayer2 = initialPlayer2.refresh(uut.gameState.board) // Refresh points

        uut = uut.copy(
            gameState = uut.gameState.changeName(
                newName = expectedMyPlayer.name,
                pieceType = expectedMyPlayer.type
            )
        )

        uut.saveOnlyBoard(uut.gameState)

        val lsGameState = uut.service.hardLoad(uut.currGameName!!)

        assertEquals(expectedMyPlayer, lsGameState?.players?.getPlayerByType(expectedMyPlayer.type))
        assertEquals(initialPlayer2, lsGameState?.players?.getPlayerByType(initialPlayer2.type))
    }

    @Test
    fun `saveEndGame with players empty fails`() = runTest {

        val uut = Game(
            target = false,
            gameState = GameState(
                players = MatchPlayers(),
                lastPlayer = PieceType.BLACK,
                board = Board(4).startPieces()
            ),
            currGameName = "testGame",
            service = gameService,
        )

        assertFailsWith<InvalidGame> {
            uut.saveEndGame()
        }
    }

    @Test
    fun `saveEndGame with game not started yet fails`() = runTest {
        val game = Game(
            service = gameService,
        )

        assertFailsWith<InvalidGame> {
            game.service.saveEndGame(game)
        }
    }

    @Test
    fun `saveOnlyBoard in local game fails`() = runTest {
        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK), Player(PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = gameService,
        )

        assertFailsWith<InvalidFile> {
            uut.saveOnlyBoard(uut.gameState)
        }
    }
}