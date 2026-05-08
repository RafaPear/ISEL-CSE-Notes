@file:Suppress("UNCHECKED_CAST")

package pt.isel.reversi.app.app.state

import androidx.compose.runtime.MutableState
import pt.isel.reversi.app.app.AppTheme
import pt.isel.reversi.app.exceptions.ExitApp
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.pages.UiState
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ErrorType.Companion.toReversiException
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.utils.LOGGER


/**
 * Updates the current page in the application state.
 * The backPage is auto-calculated if not provided.
 * If there is a global error != null or INFO, the page will not be changed.
 * IF the global error is of type INFO, it will be cleared.
 * @param page The new page to set.
 * @param backPage The new back page (auto-calculated if null).
 */
fun MutableState<PagesState>.setPage(page: Page, backPage: Page? = null) {
    var newError = value.globalError

    when {
        value.globalError != null -> {
            LOGGER.info("Cannot change page to ${page.name} due to existing global error: ${value.globalError?.message}")
            return
        }

        page == value.page -> {
            LOGGER.info("Page is the same: ${page.name}, no changes made")
            return
        }

        value.globalError?.type == ErrorType.INFO -> newError = null
    }


    val newBackPage =
        if (page == Page.MAIN_MENU) {
            Page.NONE
        } else {
            backPage ?: value.page
        }

    LOGGER.info("Set page ${page.name}, backPage: ${newBackPage.name}")
    value = value.copy(
        page = page,
        backPage = newBackPage,
        globalError = newError
    )
}

/**
 * Sets the application theme in the audio theme state.
 * @param theme The new application theme.
 */
fun MutableState<AudioThemeState>.setTheme(theme: AppTheme) {
    LOGGER.info("Set theme: ${theme.name}")
    value = value.copy(theme = theme)
}

/**
 * Sets the game state.
 * @param game The new game state.
 */
fun MutableState<GameSession>.setGame(game: Game) {
    LOGGER.info("Set new game state")
    value = value.copy(game = game)
}

/**
 * Sets the player name in the game session.
 * @param name The new player name, or null to unset.
 */
fun MutableState<GameSession>.setPlayerName(name: String?) {
    LOGGER.info("Set player name: ${name ?: "null"}")
    value = value.copy(playerName = name)
}

/**
 * Retrieves the AudioPool from the current AppState.
 * @param appState The application state holder.
 * @return the AudioPool instance.
 */
fun getStateAudioPool(appState: AppStateImpl) = appState.audioPool

/**
 * Updates the loading state for any UiState implementation.
 *
 * @param T The concrete UiState type.
 * @param isLoading Whether the app is loading.
 */
fun <T : UiState> MutableState<T>.setLoading(isLoading: Boolean) {
    if (isLoading == value.screenState.isLoading) return
    LOGGER.info("Set loading: $isLoading")
    val newScreenState = value.screenState.copy(isLoading = isLoading)
    value = value.updateScreenState(newScreenState) as T
}

/**
 * Updates the error state for any UiState implementation.
 *
 * @param T The concrete UiState type.
 * @param error The new error.
 */
fun <T : UiState> MutableState<T>.setError(error: Exception?, type: ErrorType = ErrorType.CRITICAL) {
    LOGGER.info("Set error: ${error?.message ?: "null"}")
    val newError = error as? ReversiException ?: error?.toReversiException(type)
    val newScreenState = value.screenState.copy(error = newError)
    value = value.updateScreenState(newScreenState) as T
}

fun MutableState<PagesState>.setGlobalError(error: Exception?, type: ErrorType? = ErrorType.CRITICAL) {
    LOGGER.info("Set global error: ${error?.message ?: "null"}")
    val newError = error as? ReversiException ?: error?.toReversiException(type ?: ErrorType.CRITICAL)
    value = value.copy(globalError = newError)
}

// Throw is handled by installFatalCrashLogger and will exit the app gracefully and save state
fun safeKillApp() {
    throw ExitApp()
}
