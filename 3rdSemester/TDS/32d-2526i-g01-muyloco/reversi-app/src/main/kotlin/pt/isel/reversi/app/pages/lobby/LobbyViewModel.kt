package pt.isel.reversi.app.pages.lobby

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.*
import pt.isel.reversi.app.app.state.AppStateImpl
import pt.isel.reversi.app.app.state.getStateAudioPool
import pt.isel.reversi.app.app.state.setError
import pt.isel.reversi.app.app.state.setLoading
import pt.isel.reversi.app.exceptions.GameCorrupted
import pt.isel.reversi.app.exceptions.GameIsFull
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.pages.ScreenState
import pt.isel.reversi.app.pages.UiState
import pt.isel.reversi.app.pages.ViewModel
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.loadAndEntryGame
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.utils.LOGGER
import pt.isel.reversi.utils.TRACKER

private const val UI_DELAY_SHORT_MS = 100L
private const val POLL_INTERVAL_MS = 1000L

/**
 * Represents a game loaded from storage in the lobby.
 * @property gameState The state of the loaded game.
 * @property name The name/identifier of the game.
 */
data class LobbyLoadedState(
    val gameState: GameState,
    val name: String,
)

/**
 * UI state for the lobby screen displaying available games.
 *
 * @property gameStates List of loaded games available to join.
 * @property lobbyState Current state of the lobby (empty, showing games, etc.).
 * @property selectedGame The currently selected game for joining.
 * @property canRefresh Whether the refresh button is enabled.
 * @property screenState The screen state containing error and loading information.
 */
data class LobbyUiState(
    val gameStates: List<LobbyLoadedState> = emptyList(),
    val lobbyState: LobbyState = LobbyState.NONE,
    val selectedGame: LobbyLoadedState? = null,
    val canRefresh: Boolean = false,
    override val screenState: ScreenState = ScreenState(
        error = null,
        isLoading = false
    )
) : UiState {
    /**
     * Creates a copy of this UI state with the given screen state.
     * @param newScreenState The new screen state to apply.
     * @return A new LobbyUiState with the updated screen state.
     */
    override fun updateScreenState(newScreenState: ScreenState): LobbyUiState {
        return copy(screenState = newScreenState)
    }
}

/**
 * View model for the lobby screen managing game list, polling, and user interactions.
 * Handles loading available games, polling for updates, and game selection.
 *
 * @property scope Coroutine scope for launching async lobby operations.
 * @property appState Global application state for game and UI updates.
 * @property pickGame Callback function when a game is picked.
 * @property globalError Optional error to display on initial load.
 */
class LobbyViewModel(
    val appState: AppStateImpl,
    private val scope: CoroutineScope,
    private val pickGame: (Game) -> Unit,
    override val globalError: ReversiException? = null,
    override val setGlobalError: (Exception?, ErrorType?) -> Unit,
) : ViewModel<LobbyUiState>() {
    override val _uiState = mutableStateOf(
        LobbyUiState(
            screenState = ScreenState(error = globalError)
        )
    )

    private val service = appState.service

    private var knownNames: List<String> = emptyList()

    private var pollingJob: Job? = null

    init {
        TRACKER.trackViewModelCreated(this, category = Page.LOBBY)
    }

    fun refreshAll() {
        scope.launch { loadGamesAndUpdateState() }
    }

    private suspend fun loadGamesAndUpdateState() {
        _uiState.setLoading(true)
        try {
            val ids = service.getAllGameNames().sorted()
            delay(UI_DELAY_SHORT_MS)
            val loaded = ids.mapNotNull { id ->
                try {
                    val state = service.hardLoad(id) ?: return@mapNotNull null
                    LobbyLoadedState(
                        gameState = state,
                        name = id,
                    )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    LOGGER.warning("Erro ao ler jogo: $id - ${e.message}")
                    null
                }
            }.sortedBy { it.name }

            knownNames = ids

            LOGGER.info("Jogos carregados: ${loaded.size}")

            val newLobbyState = if (loaded.isEmpty()) LobbyState.EMPTY else LobbyState.SHOW_GAMES

            _uiState.setLoading(false)

            _uiState.value = _uiState.value.copy(
                gameStates = loaded,
                lobbyState = newLobbyState,
                canRefresh = false,
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            LOGGER.warning("Erro ao carregar jogos: ${e.message}")
            _uiState.setLoading(false)
            _uiState.setError(e)
            _uiState.value = _uiState.value.copy(
                gameStates = emptyList(),
                lobbyState = LobbyState.EMPTY,
                canRefresh = false,
            )
        }
    }

    fun initLobbyAudio() {
        val audioPool = getStateAudioPool(appState)
        val theme = appState.theme
        if (!audioPool.isPlaying(theme.backgroundMusic)) {
            audioPool.stopAll()
            audioPool.play(theme.backgroundMusic)
        }
    }

    fun startPolling() {
        if (pollingJob != null) throw IllegalStateException("Polling already started")
        LOGGER.info("Starting lobby polling.")
        scope.launch {
            try {
                loadGamesAndUpdateState()
                while (isActive) {
                    pollLobbyUpdates()
                }
                throw IllegalStateException("Polling coroutine ended unexpectedly")
            } catch (_: CancellationException) {
                LOGGER.info("Lobby polling cancelled.")
            } catch (e: Exception) {
                LOGGER.severe("Error starting lobby polling: ${e.message}")
            } finally {
                LOGGER.info("Lobby polling stopped.")
            }
        }.also { pollingJob = it }
    }

    private suspend fun pollLobbyUpdates() {
        try {
            val ids = service.getAllGameNames()
            if (ids != knownNames && ids.isNotEmpty()) {
                _uiState.value = _uiState.value.copy(canRefresh = true)
                knownNames = ids
            } else if (ids.isEmpty() && knownNames.isNotEmpty()) {
                _uiState.value = _uiState.value.copy(canRefresh = true)
                knownNames = ids
            }
        } catch (e: Exception) {
            LOGGER.warning("Polling error: ${e.message}")
        }
        delay(POLL_INTERVAL_MS)
    }

    fun stopPolling() {
        pollingJob?.let { pollingJob = null; it.cancel() }
    }

    suspend fun tryLoadGame(gameName: String, desiredType: PieceType): Game? {
        return try {
            loadAndEntryGame(
                gameName = gameName,
                playerName = appState.playerName,
                desiredType = desiredType,
                service = appState.service
            )
        } catch (e: Exception) {
            LOGGER.warning("Erro ao carregar jogo $gameName: ${e.message}")
            _uiState.setError(e)
            null
        }
    }

    fun refreshGame(game: LobbyLoadedState) {
        scope.launch {
            try {
                val state = service.hardLoad(game.name) ?: return@launch
                val newGame = LobbyLoadedState(
                    gameState = state,
                    name = game.name,
                )
                if (newGame != game) _uiState.value = _uiState.value.copy(
                    gameStates = _uiState.value.gameStates.map {
                        if (it.name == newGame.name) newGame else it
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                LOGGER.warning("Erro: ${e.message}, ${e.localizedMessage}")
            }
        }
    }

    fun selectGame(game: LobbyLoadedState?) {
        LOGGER.info("Jogo selecionado: ${game?.name}")
        _uiState.value = _uiState.value.copy(selectedGame = game)
    }

    fun joinGameValidations(game: LobbyLoadedState): GameState? {
        val state: GameState = game.gameState
        val name: String = game.name
        when {
            name == appState.game.currGameName -> {
                pickGame(appState.game)
                selectGame(null)
            }

            state.players.isFull() -> {
                LOGGER.warning("Jogo cheio selecionado: $name")
                _uiState.setError(GameIsFull())
                return null
            }

            state.players.isNotEmpty() -> {
                val freeType = state.players.getFreeType()
                if (freeType == null) {
                    LOGGER.warning("Jogo corrompido selecionado: $name")
                    _uiState.setError(GameCorrupted("No available piece types in the selected game: $name."))
                    return null
                }
            }
        }
        return state
    }

    fun joinGame(game: LobbyLoadedState, pieceType: PieceType) {
        val appGame = appState.game
        val name = game.name

        scope.launch {
            try {
                _uiState.setLoading(true)
                try {
                    appGame.saveEndGame()
                } catch (e: Exception) {
                    LOGGER.warning("Erro ao salvar estado atual do jogo: ${e.message}")
                }
                val joinedGame = tryLoadGame(gameName = name, desiredType = pieceType) ?: run {
                    selectGame(null); return@launch
                }
                LOGGER.info("Entrou no jogo '${joinedGame.currGameName}' como peça $pieceType.")

                pickGame(joinedGame)
                selectGame(null)
            } catch (e: Exception) {
                setError(e)
            } finally {
                _uiState.setLoading(false)
            }
        }
    }
}
