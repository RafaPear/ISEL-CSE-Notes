package pt.isel.reversi.app.pages.aboutPage

import androidx.compose.runtime.mutableStateOf
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.pages.ScreenState
import pt.isel.reversi.app.pages.UiState
import pt.isel.reversi.app.pages.ViewModel
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.utils.TRACKER

/**
 * UI state for the about page screen.
 * @property screenState The screen state containing error and loading information.
 */
data class AboutUiState(
    override val screenState: ScreenState = ScreenState()
) : UiState {
    /**
     * Creates a copy of this UI state with the given screen state.
     * @param newScreenState The new screen state to apply.
     * @return A new AboutUiState with the updated screen state.
     */
    override fun updateScreenState(newScreenState: ScreenState) =
        copy(screenState = newScreenState)
}

/**
 * ViewModel for the about page screen.
 * Manages about page state and error handling.
 *
 * @property globalError Optional global error to display on initial load.
 * @property setGlobalError Callback function to update global error state.
 */
class AboutPageViewModel(
    override val globalError: ReversiException? = null,
    override val setGlobalError: (Exception?, ErrorType?) -> Unit,
) : ViewModel<AboutUiState>() {
    override val _uiState = mutableStateOf(
        AboutUiState(
            screenState = ScreenState(error = globalError)
        )
    )

    init {
        TRACKER.trackViewModelCreated(viewModel = this, category = Page.ABOUT)
    }
}