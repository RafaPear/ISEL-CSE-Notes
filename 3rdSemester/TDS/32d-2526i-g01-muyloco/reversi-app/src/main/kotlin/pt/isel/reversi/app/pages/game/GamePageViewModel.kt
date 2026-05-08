package pt.isel.reversi.app.pages.game

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import pt.isel.reversi.app.app.state.setError
import pt.isel.reversi.app.exceptions.GameCorrupted
import pt.isel.reversi.app.exceptions.GameNotStartedYet
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.pages.ScreenState
import pt.isel.reversi.app.pages.UiState
import pt.isel.reversi.app.pages.ViewModel
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.utils.LOGGER
import pt.isel.reversi.utils.TRACKER
import kotlin.coroutines.cancellation.CancellationException


/**
 * UI state for the game page.
 * @property game The current game instance.
 * @property screenState The screen state containing error and loading information.
 */
data class GameUiState(
    val game: Game,
    override val screenState: ScreenState = ScreenState()
) : UiState {
    /**
     * Creates a copy of this UI state with the given screen state.
     * @param newScreenState The new screen state to apply.
     * @return A new GameUiState with the updated screen state.
     */
    override fun updateScreenState(newScreenState: ScreenState): GameUiState {
        return copy(screenState = newScreenState)
    }
}

/**
 * View model for the game page managing game state, UI updates, and user interactions.
 * Handles game move execution, state polling for multiplayer games, and sound effects.
 *
 * @property game The current game state.
 * @property scope Coroutine scope for launching async game operations.
 * @property setGame Callback function to persist updated game state.
 * @property audioPlayMove Callback function to play move sound effect.
 * @property globalError Optional error to display on initial load.
 * @property setGlobalError Callback function to update global error state.
 */
class GamePageViewModel(
    private val game: Game,
    private val scope: CoroutineScope,
    private val setGame: (Game) -> Unit,
    private val audioPlayMove: () -> Unit,
    private val setPage: (Page) -> Unit = {},
    override val globalError: ReversiException? = null,
    override val setGlobalError: (Exception?, ErrorType?) -> Unit,
) : ViewModel<GameUiState>() {
    override val _uiState by lazy {
        mutableStateOf(
            GameUiState(
                game = game,
                screenState = ScreenState(error = globalError)
            )
        )
    }

    private var pollingJob: Job? = null

    init {
        TRACKER.trackViewModelCreated(this, category = Page.GAME)
    }

    fun gotoWinnerPage(game: Game) {
        setGame(game)
        setPage(Page.WINNER)
    }

    /**
     * Starts a coroutine that periodically polls the game state to refresh it.
     * If the game has a winner, it navigates to the winner page.
     * If the game has not started yet or is corrupted, it sets an appropriate error.
     * The polling runs every 50 milliseconds until cancelled.
     *
     * @throws IllegalStateException if polling is already started.
     */
    fun startPolling() {
        if (pollingJob != null) throw IllegalStateException("Polling already started")

        LOGGER.info("Starting auto-refreshing game state coroutine")

        scope.launch {
            try {
                while (isActive) {
                    val game = _uiState.value.game
                    val gameState = game.gameState
                    if (gameState?.winner != null) {
                        gotoWinnerPage(game)
                    }
                    val myPiece = game.myPiece ?: run {
                        _uiState.setError(GameNotStartedYet(), ErrorType.ERROR)
                        return@launch
                    }

                    if (game.gameState != null && game.currGameName != null) {
                        var newGame = game.refresh()
                        val newGameState = newGame.gameState ?: run {
                            _uiState.setError(GameCorrupted(), ErrorType.ERROR)
                            return@launch
                        }
                        val myName = gameState?.players?.getPlayerByType(myPiece)?.name

                        if (myName != null) {
                            newGame = newGame.copy(
                                gameState = newGameState.changeName(myName, myPiece)
                            )
                        }

                        val needsUpdate = newGame.lastModified != game.lastModified
                        if (needsUpdate) {
                            _uiState.value = _uiState.value.copy(game = newGame)
                        }
                    }

                    delay(50L)
                }
                throw IllegalStateException("Polling coroutine ended unexpectedly")
            } catch (_: CancellationException) {
                LOGGER.info("Game polling cancelled.")
            } catch (e: Exception) {
                LOGGER.warning("Auto-refreshing game state gave an error ${e.message}")
            } finally {
                LOGGER.info("Stop auto-refreshing game state coroutine: ${this@GamePageViewModel}")
            }
        }.also { pollingJob = it }
    }

    fun stopPolling() {
        pollingJob?.let {
            pollingJob = null
            it.cancel()
        }
    }

    fun isPollingActive() = pollingJob != null

    fun setTarget(target: Boolean) {
        _uiState.value = _uiState.value.copy(game = _uiState.value.game.setTargetMode(target))
    }

    /*
     * Plays a move at the given coordinate.
     * Updates the UI state with the new game state after the move.
     * Saves the updated game state using the setGame callback.
     */
    fun playMove(coordinate: Coordinate) {
        scope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    game = _uiState.value.game.play(coordinate)
                )
                setGame(_uiState.value.game)
                audioPlayMove()
            } catch (e: Exception) {
                setGame(_uiState.value.game)
                _uiState.setError(e)
            }
        }
    }

    fun getAvailablePlays() = _uiState.value.game.getAvailablePlays()

    /*
        * Passes the current player's turn.
        * Saves the updated game state using the setGame callback.
        * If the game has a winner after passing, navigates to the winner page.
     */
    fun pass() {
        scope.launch {
            try {
                _uiState.value = _uiState.value.copy(game = _uiState.value.game.pass())
                if (_uiState.value.game.gameState?.winner != null) {
                    gotoWinnerPage(_uiState.value.game)
                }
            } catch (e: Exception) {
                setGame(_uiState.value.game)
                _uiState.setError(e)
            }
        }
    }
}
