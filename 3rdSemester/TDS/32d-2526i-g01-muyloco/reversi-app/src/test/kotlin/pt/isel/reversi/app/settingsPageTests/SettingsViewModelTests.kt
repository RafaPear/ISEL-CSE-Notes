package pt.isel.reversi.app.settingsPageTests

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.GameSession
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.settingsPage.SettingsViewModel
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTests {
    val appState = AppState.empty(EmptyGameService()).copy(
        gameSession = GameSession(
            Game(
                service = EmptyGameService(),
                gameState = GameState(
                    MatchPlayers(
                        player1 = Player(PieceType.BLACK, "Player1"),
                        player2 = Player(PieceType.WHITE, "Player2")
                    ),
                    lastPlayer = PieceType.BLACK,
                    board = Board(4),
                ),
                myPiece = PieceType.WHITE,
            ),
            playerName = null
        ),
    )
    val reversiScope = ReversiScope(appState)

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `verify applySettings call saveGame if storage type changed`() = runTest {
        var saveGameCalled = false
        val viewModel = SettingsViewModel(
            this,
            appState = appState,
            setTheme = {},
            setPlayerName = {},
            saveGame = { saveGameCalled = true },
            setGame = {},
            setGlobalError = { _, _ -> }
        )

        viewModel.applySettings(
            appState.theme,
            null,
            appState.theme,
            viewModel.uiState.value.draftCoreConfig.copy(gameStorageType = GameStorageType.DATABASE_STORAGE),
            volume = appState.audioPool.getMasterVolume() ?: 1.0f,
            endAction = {}
        )

        advanceUntilIdle()

        assert(saveGameCalled)
    }


}