package pt.isel.reversi.app.lobbyMenuTests

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.GameSession
import pt.isel.reversi.app.pages.lobby.LobbyLoadedState
import pt.isel.reversi.app.pages.lobby.LobbyState
import pt.isel.reversi.app.pages.lobby.LobbyViewModel
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.FakeGameService
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LobbyViewModelTests {
    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    val board = Board(4).startPieces()

    val gamesBase = listOf(
        LobbyLoadedState(
            gameState = GameState(
                players = MatchPlayers(
                    Player(PieceType.BLACK),
                    Player(PieceType.WHITE)
                ),
                board = board,
                lastPlayer = PieceType.BLACK
            ),
            name = "game0"
        ),
        LobbyLoadedState(
            gameState = GameState(
                players = MatchPlayers(
                    Player(PieceType.BLACK)
                ),
                board = board,
                lastPlayer = PieceType.BLACK
            ),
            name = "game1"
        ),
        LobbyLoadedState(
            gameState = GameState(
                players = MatchPlayers(),
                lastPlayer = PieceType.BLACK,
                board = board,
            ),
            name = "game2"
        )
    )

    fun duplicateGames(games: List<LobbyLoadedState>): List<LobbyLoadedState> {
        val newList = games.toMutableList()
        games.forEach {
            newList.add(
                LobbyLoadedState(
                    gameState = it.gameState,
                    name = "game_${newList.size}"
                )
            )
        }
        return newList
    }

    val games = duplicateGames(gamesBase)

    val service = FakeGameService()

    @BeforeTest
    fun setup() {
        games.forEach {
            service.save(
                it.name,
                it.gameState
            )
        }
    }

    @AfterTest
    fun tearDown() {
        games.forEach {
            runBlocking {
                service.delete(it.name)
            }
        }
    }

    fun vm(scope: CoroutineScope) =
        LobbyViewModel(
            AppState.empty(service = service),
            scope = scope,
            pickGame = {},
            globalError = null,
            setGlobalError = { _, _ -> },
        )

    @Test
    fun `verify lobby state change after loading games`() = runTest {
        val uut = vm(this)

        assert(uut.uiState.value.gameStates.isEmpty())
        uut.refreshAll()
        advanceUntilIdle()

        assert(uut.uiState.value.gameStates.size == games.size)
        assert(uut.uiState.value.lobbyState == LobbyState.SHOW_GAMES)
    }

    @Test
    fun `verify lobby state when no games are available`() = runTest {
        val uut = vm(this)

        // Remove all games
        games.forEach {
            service.delete(it.name)
        }

        assert(uut.uiState.value.gameStates.isEmpty())
        uut.refreshAll()
        advanceUntilIdle()

        assert(uut.uiState.value.gameStates.isEmpty())
        assert(uut.uiState.value.lobbyState == LobbyState.EMPTY)
    }

    @Test
    fun `verify polling loads initial games`() = runTest {
        val uut = vm(this)

        assert(uut.uiState.value.gameStates.isEmpty())
        uut.startPolling()
        advanceTimeBy(200L)
        uut.stopPolling()

        assert(uut.uiState.value.gameStates.size == games.size)
        assert(uut.uiState.value.lobbyState == LobbyState.SHOW_GAMES)
    }

    @Test
    fun `verify polling set canRefresh to true if have new games`() = runTest {
        val uut = vm(this)

        assert(uut.uiState.value.gameStates.isEmpty())
        uut.startPolling()
        advanceTimeBy(1000L) // give tive for loading initial games

        // Add a new game
        val newGame = LobbyLoadedState(
            gameState = GameState(
                players = MatchPlayers(
                    Player(PieceType.BLACK)
                ),
                board = board,
                lastPlayer = PieceType.BLACK
            ),
            name = "new_game"
        )
        service.save(
            newGame.name,
            newGame.gameState
        )

        advanceTimeBy(300L)
        uut.stopPolling()

        assert(uut.uiState.value.canRefresh)
    }

    @Test
    fun `verify tryLoadGame return correct game`() = runTest {
        val uut = vm(this)

        uut.refreshAll()// load games
        advanceUntilIdle()

        val targetGame = games[2]
        val loadedGameState = uut.tryLoadGame(
            targetGame.name,
            PieceType.BLACK
        )
        advanceUntilIdle()

        val expectedGameState = service.game[2].second.refreshPlayers()

        assert(loadedGameState != null)
        assertEquals(expectedGameState, loadedGameState?.gameState)
    }

    @Test
    fun `verify tryLoadGame return null for non existing game`() = runTest {
        val uut = vm(this)

        uut.refreshAll()// load games
        advanceUntilIdle()

        val loadedGameState = uut.tryLoadGame(
            "non_existing_game",
            PieceType.BLACK
        )
        advanceUntilIdle()

        assert(loadedGameState == null)
    }

    @Test
    fun `verify tryLoadGame setError for non existing game`() = runTest {
        val uut = vm(this)

        uut.refreshAll()// load games
        advanceUntilIdle()

        assert(uut.error == null)

        val loadedGameState = uut.tryLoadGame(
            "non_existing_game",
            PieceType.BLACK
        )
        advanceUntilIdle()

        assert(loadedGameState == null)
        assert(uut.error != null)
    }

    @Test
    fun `verify refreshGme works correctly`() = runTest {
        val uut = vm(this)

        uut.refreshAll()// load games
        advanceUntilIdle()

        val targetGame = games[1]
        val updatedGameState = GameState(
            players = MatchPlayers(
                Player(PieceType.BLACK),
                Player(PieceType.WHITE)
            ),
            board = board,
            lastPlayer = PieceType.WHITE
        )

        // Update game in service
        service.save(
            targetGame.name,
            updatedGameState
        )

        // Refresh specific game
        uut.refreshGame(LobbyLoadedState(targetGame.gameState, targetGame.name))
        advanceUntilIdle()

        val refreshedGameState = uut.uiState.value.gameStates.find { it.name == targetGame.name }?.gameState

        assertEquals(updatedGameState, refreshedGameState)
    }

    @Test
    fun `joinGameValidations in current game, verify if call pickGame`() = runTest {
        val targetGame = gamesBase[0] // full game

        var pickGameCalled = false

        val uut = LobbyViewModel(
            AppState.empty(service = service).copy(
                gameSession = GameSession(
                    game = Game(
                        currGameName = games[0].name,
                        myPiece = PieceType.BLACK,
                        service = service,
                    ),
                    "BLACK"
                ),
            ),
            scope = this,
            pickGame = { pickGameCalled = true },
            globalError = null,
            setGlobalError = { _, _ -> },
        )

        uut.refreshAll()

        uut.joinGameValidations(
            targetGame,
        )

        assert(pickGameCalled)
    }
}