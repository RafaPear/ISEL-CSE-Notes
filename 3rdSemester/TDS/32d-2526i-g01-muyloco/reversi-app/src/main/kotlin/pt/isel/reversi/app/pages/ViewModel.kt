package pt.isel.reversi.app.pages

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import pt.isel.reversi.app.app.state.setError
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.utils.TRACKER

/**
 * Abstract base class for view models managing UI state.
 * Each view model holds a mutable UI state and provides error handling capabilities.
 *
 * @param T The type of UI state managed by this view model.
 * @property _uiState The mutable internal UI state.
 * @property uiState Public read-only access to the UI state.
 * @property globalError Optional global error state.
 * @property setGlobalError Callback to set a global error.
 */
abstract class ViewModel<T : UiState> {
    protected abstract val _uiState: MutableState<T>
    val uiState: State<T> get() = _uiState
    abstract val globalError: ReversiException?
    abstract val setGlobalError: (Exception?, ErrorType?) -> Unit

    /**
     * Gets the current error state, preferring global errors over screen-specific errors.
     */
    val error get() = globalError ?: uiState.value.screenState.error

    init {
        TRACKER.trackViewModelCreated(viewModel = this)
    }

    /**
     * Sets an error on either the global error state or the screen state.
     * @param error The exception to set as an error.
     * @param type The severity level of the error.
     */
    fun setError(error: Exception?, type: ErrorType? = ErrorType.WARNING) {
        if (globalError != null) {
            setGlobalError(error, type)
        } else {
            _uiState.setError(error, type ?: ErrorType.WARNING)
        }
    }
}

/**
 * Represents the state of a screen including error and loading states.
 * @property error The current error on the screen, if any.
 * @property isLoading Whether the screen is in a loading state.
 */
data class ScreenState(
    val error: ReversiException? = null,
    val isLoading: Boolean = false,
)

/**
 * Base class for UI state with screen state management.
 * Each subclass must implement updateScreenState to define how to copy itself with a new ScreenState.
 */
interface UiState {
    val screenState: ScreenState

    /**
     * Creates a copy of this UiState with the given ScreenState.
     * Each subclass implements this using its data class copy() method.
     */
    fun updateScreenState(newScreenState: ScreenState): UiState
}
