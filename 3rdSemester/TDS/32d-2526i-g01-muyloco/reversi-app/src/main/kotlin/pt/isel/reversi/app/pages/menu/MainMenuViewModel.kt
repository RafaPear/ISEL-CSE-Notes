package pt.isel.reversi.app.pages.menu

import androidx.compose.runtime.mutableStateOf
import pt.isel.reversi.app.app.state.AppStateImpl
import pt.isel.reversi.app.app.state.getStateAudioPool
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.pages.ScreenState
import pt.isel.reversi.app.pages.UiState
import pt.isel.reversi.app.pages.ViewModel
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.utils.TRACKER


/**
 * UI state for the main menu screen.
 * @property screenState The screen state containing error and loading information.
 */
data class MainMenuUIState(
    override val screenState: ScreenState = ScreenState()
) : UiState {
    /**
     * Creates a copy of this UI state with the given screen state.
     * @param newScreenState The new screen state to apply.
     * @return A new MainMenuUIState with the updated screen state.
     */
    override fun updateScreenState(newScreenState: ScreenState): UiState {
        return this.copy(screenState = newScreenState)
    }
}

/**
 * ViewModel for the main menu screen.
 * Manages menu interactions, audio playback, and navigation state.
 *
 * @property appState The global application state.
 * @property globalError The current global error, if any.
 * @property setGlobalError A callback function to update the global error state.
 */
class MainMenuViewModel(
    private val appState: AppStateImpl,
    override val globalError: ReversiException? = null,
    override val setGlobalError: (Exception?, ErrorType?) -> Unit,
    val setPage: (Page) -> Unit,
) : ViewModel<MainMenuUIState>() {
    override val _uiState = mutableStateOf(
        MainMenuUIState(
            screenState = ScreenState(
                error = globalError
            )
        )
    )

    init {
        TRACKER.trackViewModelCreated(this, category = Page.MAIN_MENU)
    }

    /**
     * Plays the background menu music from the current theme if not already playing.
     * Stops all other audio before starting the background music.
     */
    fun playMenuAudio() {
        val audioPool = getStateAudioPool(appState)
        val theme = appState.theme
        if (!audioPool.isPlaying(theme.backgroundMusic)) {
            audioPool.stopAll()
            audioPool.play(theme.backgroundMusic)
        }
    }
}