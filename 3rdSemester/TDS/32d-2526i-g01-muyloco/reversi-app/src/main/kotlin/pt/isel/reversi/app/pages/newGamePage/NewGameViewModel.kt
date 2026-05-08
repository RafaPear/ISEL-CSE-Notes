package pt.isel.reversi.app.pages.newGamePage

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.isel.reversi.app.app.state.AppStateImpl
import pt.isel.reversi.app.exceptions.NoPieceSelected
import pt.isel.reversi.app.pages.ScreenState
import pt.isel.reversi.app.pages.UiState
import pt.isel.reversi.app.pages.ViewModel
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.utils.LOGGER

/**
 * UI state for the new game screen.
 * @property screenState The screen state containing error and loading information.
 */
data class NewGameUiState(
    override val screenState: ScreenState = ScreenState()
) : UiState {
    /**
     * Creates a copy of this UI state with the given screen state.
     * @param newScreenState The new screen state to apply.
     * @return A new NewGameUiState with the updated screen state.
     */
    override fun updateScreenState(newScreenState: ScreenState) =
        copy(screenState = newScreenState)
}

/**
 * ViewModel that builds local or remote games based on user inputs.
 * Validates piece selection, triggers game creation, and exposes UI state/errors.
 *
 * @param scope Coroutine scope for background operations.
 * @param playerName Current player name used when creating non-local games.
 * @param globalError Optional global error propagated into initial UI state.
 * @param setGlobalError Callback to update global error when present.
 * @param createGame Callback invoked with the created game instance.
 */
class NewGameViewModel(
    private val scope: CoroutineScope,
    private val appState: AppStateImpl,
    override val globalError: ReversiException? = null,
    override val setGlobalError: (Exception?, ErrorType?) -> Unit,
    private val createGame: (Game) -> Unit,
) : ViewModel<NewGameUiState>() {
    override val _uiState = mutableStateOf(
        NewGameUiState(
            screenState = ScreenState(error = globalError)
        )
    )

    fun tryCreateGame(game: Game, boardSize: Int, playerName: String?) {
        LOGGER.info("Using ${appState.service.getStorageTypeName()}")

        val currGameName = game.currGameName

        val myPiece: PieceType = game.myPiece ?: run {
            setError(NoPieceSelected())
            return
        }

        scope.launch {
            try {
                val newGame = if (currGameName.isNullOrBlank()) {
                    createLocalGame(boardSize, myPiece)
                } else {
                    val name = playerName ?: myPiece.name
                    createNotLocalGame(playerName = name, currGameName, boardSize, myPiece)
                }

                LOGGER.info("Novo jogo '${currGameName?.ifBlank { "(local)" } ?: "(local)"} ' iniciado.")
                createGame(newGame)
            } catch (e: Exception) {
                setError(e)
            }
        }
    }

    private suspend fun createLocalGame(boardSize: Int, myPiece: PieceType) =
        startNewGame(
            side = boardSize,
            players = MatchPlayers(
                Player(PieceType.BLACK),
                Player(PieceType.WHITE)
            ),
            firstTurn = myPiece,
            service = appState.service,
        )


    private suspend fun createNotLocalGame(
        playerName: String,
        gameName: String,
        boardSize: Int,
        myPiece: PieceType
    ) =
        startNewGame(
            side = boardSize,
            players = MatchPlayers(
                Player(myPiece, name = playerName),
            ),
            firstTurn = myPiece,
            currGameName = gameName.trim(),
            service = appState.service,
        )
}