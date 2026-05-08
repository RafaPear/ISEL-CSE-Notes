package pt.isel.reversi.app.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import pt.isel.reversi.app.app.state.*
import pt.isel.reversi.app.pages.aboutPage.AboutPage
import pt.isel.reversi.app.pages.aboutPage.AboutPageViewModel
import pt.isel.reversi.app.pages.game.GamePage
import pt.isel.reversi.app.pages.game.GamePageViewModel
import pt.isel.reversi.app.pages.lobby.LobbyMenu
import pt.isel.reversi.app.pages.lobby.LobbyViewModel
import pt.isel.reversi.app.pages.menu.MainMenu
import pt.isel.reversi.app.pages.menu.MainMenuViewModel
import pt.isel.reversi.app.pages.newGamePage.NewGamePage
import pt.isel.reversi.app.pages.newGamePage.NewGameViewModel
import pt.isel.reversi.app.pages.settingsPage.SettingsPage
import pt.isel.reversi.app.pages.settingsPage.SettingsViewModel
import pt.isel.reversi.app.pages.winnerPage.WinnerPage
import pt.isel.reversi.app.pages.winnerPage.WinnerPageViewModel

/**
 * Creates a composable page view if the provided ViewModel is of the expected type T.
 *
 * Reference:
 *  - [Providing ViewModels to your Screens](https://composedestinations.rafaelcosta.xyz/common-use-cases/providing-viewmodels/)
 *
 * @param T The expected type of the ViewModel.
 * @param vm The ViewModel instance to check.
 * @param content The composable content to return if the type matches.
 * @return A composable function if the ViewModel is of type T, otherwise an empty composable.
 */
private inline fun <reified T : ViewModel<out UiState>> createPageViewIfType(
    vm: ViewModel<out UiState>,
    noinline content: @Composable ReversiScope.() -> Unit,
): @Composable ReversiScope.() -> Unit =
    if (vm is T) content else {
        {}
    }

/**
 * Creates and returns the appropriate composable page view based on the current [Page] type.
 *
 * This extension function acts as a factory method that maps each page type to its corresponding
 * composable view and ViewModel. It handles navigation callbacks and state management for each page,
 * ensuring proper transitions between different screens in the Reversi application.
 *
 * The function uses type-safe ViewModel matching through [createPageViewIfType] to ensure that
 * each page receives the correct ViewModel instance. Navigation is handled through the [pagesState]
 * parameter, which maintains the current page and back navigation stack.
 *
 * @receiver The [Page] instance representing the current page to be displayed.
 * @param vm The ViewModel instance associated with the current page. Must match the expected
 *           ViewModel type for the page, otherwise an empty composable is returned.
 * @param pagesState A mutable state holding the current page state, including the current page and back navigation.
 * @return A composable function with [ReversiScope] receiver that renders the appropriate page view.
 *         Returns an empty composable if the page type is [Page.NONE] or if the ViewModel type doesn't match.
 *
 * @see Page
 * @see ViewModel
 * @see pt.isel.reversi.app.app.state.PagesState
 * @see ReversiScope
 */
@Composable
fun Page.createPageView(
    vm: ViewModel<out UiState>,
    gameSession: MutableState<GameSession>,
    pagesState: MutableState<PagesState>,
): @Composable ReversiScope.() -> Unit = when (this@createPageView) {
    Page.MAIN_MENU -> createPageViewIfType<MainMenuViewModel>(vm) {
        MainMenu(viewModel = vm as MainMenuViewModel, onLeave = {})
    }

    Page.GAME -> createPageViewIfType<GamePageViewModel>(vm) {
        GamePage(viewModel = vm as GamePageViewModel) {
            
                gameSession.setGame(it)
                pagesState.setPage(Page.MAIN_MENU)
            }
        }

    Page.SETTINGS -> createPageViewIfType<SettingsViewModel>(vm) {
        SettingsPage(viewModel = vm as SettingsViewModel) {
            pagesState.setPage(pagesState.value.backPage)
        }
    }

    Page.ABOUT -> createPageViewIfType<AboutPageViewModel>(vm) {
        AboutPage(viewModel = vm as AboutPageViewModel) { pagesState.setPage(pagesState.value.backPage) }
    }

    Page.NEW_GAME -> createPageViewIfType<NewGameViewModel>(vm) {
        NewGamePage(
            viewModel = vm as NewGameViewModel,
            playerNameChange = { name: String -> gameSession.setPlayerName(name) }) { pagesState.setPage(Page.MAIN_MENU) }
    }

    Page.LOBBY -> createPageViewIfType<LobbyViewModel>(vm) {
        LobbyMenu(viewModel = vm as LobbyViewModel) { pagesState.setPage(Page.MAIN_MENU) }
    }

    Page.WINNER -> createPageViewIfType<WinnerPageViewModel>(vm) {
        WinnerPage(viewModel = vm as WinnerPageViewModel) {
            pagesState.setPage(Page.MAIN_MENU)
            vm.deleteGame()
        }
    }

    Page.NONE -> {
        {}
    }
}
