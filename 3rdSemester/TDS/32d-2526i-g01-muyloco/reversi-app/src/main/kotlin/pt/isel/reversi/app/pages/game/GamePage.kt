package pt.isel.reversi.app.pages.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import pt.isel.reversi.app.ScaffoldView
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.exceptions.GameNotStartedYet
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.utils.PreviousPage
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.utils.LOGGER
import pt.isel.reversi.utils.TRACKER

/**
 * Main game page displaying the Reversi board, player scores, and game controls.
 * Manages game music playback and periodic game state refreshes for multiplayer games.
 *
 * @param viewModel The game page view model containing UI state and game logic.
 * @param modifier Modifier for layout adjustments.
 * @param freeze When true, disables interactions with the board.
 * @param onLeave Callback invoked when navigating back, receives the current game.
 */
@Composable
fun ReversiScope.GamePage(
    viewModel: GamePageViewModel,
    modifier: Modifier = Modifier,
    freeze: Boolean = false,
    onLeave: (Game) -> Unit,
) {
    TRACKER.trackPageEnter(customName = "GamePage", category = Page.GAME)

    val game = viewModel.uiState.value.game
    val theme = appState.theme

    if (!game.hasStarted()) {
        LOGGER.warning("Game not started yet, navigating back to previous page")
        viewModel.setGlobalError(GameNotStartedYet(), null)
        onLeave(game)
        return
    }

    // Launch the game refresh coroutine
    DisposableEffect(viewModel) {
        TRACKER.trackEffectStart(this, category = Page.GAME)
        if (game.currGameName != null && !viewModel.isPollingActive()) {
            viewModel.startPolling()
        }

        appState.audioPool.run {
            if (!isPlaying(theme.gameMusic)) {
                stop(theme.backgroundMusic)
                stop(theme.gameMusic)
                play(theme.gameMusic)
            }
        }

        onDispose {
            viewModel.stopPolling()
            TRACKER.trackEffectStop(this, category = Page.GAME)
        }
    }

    val name = game.currGameName

    ScaffoldView(
        setError = { it, type -> viewModel.setError(it, type) },
        error = viewModel.error,
        isLoading = viewModel.uiState.value.screenState.isLoading,
        title = name ?: "Reversi",
        previousPageContent = {
            PreviousPage { onLeave(game) }
        }
    ) { padding ->
        GamePageView(
            modifier = modifier.fillMaxSize()
                .padding(paddingValues = padding),
            game = game,
            freeze = freeze,
            getAvailablePlays = { viewModel.getAvailablePlays() },
            onCellClick = { viewModel.playMove(it) },
            setTargetMode = { viewModel.setTarget(!game.target) },
            pass = { viewModel.pass() }
        )
    }
}
