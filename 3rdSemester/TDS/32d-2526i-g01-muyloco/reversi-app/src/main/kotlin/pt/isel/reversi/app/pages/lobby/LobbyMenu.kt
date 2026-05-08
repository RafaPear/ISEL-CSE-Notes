package pt.isel.reversi.app.pages.lobby

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pt.isel.reversi.app.ScaffoldView
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.getTheme
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.pages.lobby.lobbyViews.Empty
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.LobbyCarousel
import pt.isel.reversi.app.pages.lobby.lobbyViews.utils.PopupPickAPiece
import pt.isel.reversi.app.pages.lobby.lobbyViews.utils.RefreshButton
import pt.isel.reversi.app.utils.PreviousPage
import pt.isel.reversi.app.utils.reversiFadeAnimation
import pt.isel.reversi.utils.LOGGER
import pt.isel.reversi.utils.TRACKER

/**
 * Enumeration of possible lobby screen states.
 */
enum class LobbyState {
    /** Initial uninitialized state. */
    NONE,

    /** Lobby has no available games. */
    EMPTY,

    /** Lobby displaying available games for joining. */
    SHOW_GAMES
}

private const val PAGE_TRANSITION_DURATION_MS = 500

/**
 * Lobby menu screen for browsing and joining saved multiplayer games.
 * Displays available games in a carousel and handles game selection and joining.
 *
 * @param viewModel The lobby view model managing game list and selection logic.
 * @param onLeave Callback invoked when navigating back from the lobby.
 */
@Composable
fun ReversiScope.LobbyMenu(
    viewModel: LobbyViewModel,
    onLeave: () -> Unit,
) {
    TRACKER.trackPageEnter(customName = "LobbyMenu", category = Page.LOBBY)

    val uiState = viewModel.uiState.value
    val games = uiState.gameStates
    val lobbyState = uiState.lobbyState
    val canRefresh = uiState.canRefresh
    val appState = this.appState

    viewModel.initLobbyAudio()

    DisposableEffect(viewModel) {
        LOGGER.info("Starting polling for lobby updates.")
        TRACKER.trackEffectStart(viewModel, category = Page.LOBBY)
        viewModel.startPolling()
        onDispose {
            TRACKER.trackEffectStop(viewModel, category = Page.LOBBY)
            viewModel.stopPolling()
        }
    }

    val refreshAction: @Composable () -> Unit = {
        if (canRefresh) {
            RefreshButton { viewModel.refreshAll() }
        }
    }

    ScaffoldView(
        setError = { error, type -> viewModel.setError(error, type) },
        error = uiState.screenState.error,
        isLoading = uiState.screenState.isLoading,
        title = "Lobby - Jogos Guardados",
        previousPageContent = {
            PreviousPage { onLeave() }
        },
    ) { padding ->
        val reversiScope = this
        AnimatedContent(
            targetState = lobbyState,
            transitionSpec = {
                val iOSEasing = CubicBezierEasing(0.22f, 1f, 0.36f, 1f)
                reversiFadeAnimation(PAGE_TRANSITION_DURATION_MS, iOSEasing)
            },
            modifier = Modifier
                .fillMaxSize()
                .background(getTheme().backgroundColor),
            label = "PageTransition"
        ) { page ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = padding)
                    .padding(all = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (page) {
                    LobbyState.NONE -> {}
                    LobbyState.EMPTY -> Empty { refreshAction() }
                    LobbyState.SHOW_GAMES -> LobbyCarousel(
                        currentGameName = appState.game.currGameName,
                        games = games,
                        viewModel,
                        reversiScope = reversiScope,
                        buttonRefresh = { refreshAction() }
                    ) { game ->
                        viewModel.selectGame(game)
                    }
                }
            }

            viewModel.uiState.value.selectedGame?.let { game ->
                val state = viewModel.joinGameValidations(game)
                if (state == null) {
                    viewModel.selectGame(null)
                    return@let
                }
                val players = state.players.getAvailableTypes()

                if (game.name != appState.game.currGameName) {
                    PopupPickAPiece(
                        pieces = players,
                        onPick = { pieceType -> viewModel.joinGame(game, pieceType) },
                        onDismiss = { viewModel.selectGame(null) }
                    )
                }
            }
        }
    }
}
