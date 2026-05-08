package pt.isel.reversi.app.pages.settingsPage

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pt.isel.reversi.app.app.AppTheme
import pt.isel.reversi.app.app.state.AppStateImpl
import pt.isel.reversi.app.app.state.setLoading
import pt.isel.reversi.app.gameAudio.loadGameAudioPool
import pt.isel.reversi.app.pages.ScreenState
import pt.isel.reversi.app.pages.UiState
import pt.isel.reversi.app.pages.ViewModel
import pt.isel.reversi.app.utils.runStorageHealthCheck
import pt.isel.reversi.core.CoreConfig
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.loadCoreConfig
import pt.isel.reversi.core.saveCoreConfig
import pt.isel.reversi.utils.LOGGER
import pt.isel.reversi.utils.TRACKER
import pt.isel.reversi.utils.audio.AudioPool

/**
 * UI state for the settings page screen.
 * @property screenState The screen state containing error and loading information.
 * @property draftPlayerName The player name being edited in the settings.
 * @property draftTheme The theme being selected in the settings.
 * @property draftCoreConfig The core configuration being modified in the settings.
 * @property currentVol The current audio volume level.
 */
data class SettingsUiState(
    override val screenState: ScreenState = ScreenState(),
    val draftPlayerName: String?,
    val draftTheme: AppTheme,
    val draftCoreConfig: CoreConfig,
    val currentVol: Float
) : UiState {
    /**
     * Creates a copy of this UI state with the given screen state.
     * @param newScreenState The new screen state to apply.
     * @return A new SettingsUiState with the updated screen state.
     */
    override fun updateScreenState(newScreenState: ScreenState) =
        copy(screenState = newScreenState)
}

/**
 * ViewModel for the settings page screen.
 * Manages settings changes, audio volume, theme selection, and persistence.
 *
 * @property scope Coroutine scope for background operations.
 * @property appState The global application state.
 * @property globalError Optional global error to display on initial load.
 * @property setTheme Callback function to update the theme.
 * @property setPlayerName Callback function to update the player name.
 * @property setGlobalError Callback function to update global error state.
 */
class SettingsViewModel(
    private val scope: CoroutineScope,
    private val appState: AppStateImpl,
    override val globalError: ReversiException? = null,
    private val setTheme: (AppTheme) -> Unit,
    private val setPlayerName: suspend (String?) -> Unit,
    private val saveGame: suspend () -> Unit,
    private val setGame: (Game) -> Unit,
    override val setGlobalError: (Exception?, ErrorType?) -> Unit,
) : ViewModel<SettingsUiState>() {

    override val _uiState = mutableStateOf(
        SettingsUiState(
            draftPlayerName = appState.playerName,
            draftTheme = appState.theme,
            draftCoreConfig = loadCoreConfig(),
            currentVol = 0f,
            screenState = ScreenState(error = globalError)
        )
    )

    init {
        val masterVol = appState.audioPool.getMasterVolume()
        val isMuted = appState.audioPool.isPoolMuted()
        val min = appState.audioPool.getMasterVolumeRange()?.first

        val currentVol = if (isMuted) min ?: -20f
        else masterVol ?: 0f

        _uiState.value = _uiState.value.copy(
            currentVol = currentVol,
        )
    }

    fun setDraftPlayerName(name: String?) {
        _uiState.value = _uiState.value.copy(draftPlayerName = name)
    }

    fun setDraftTheme(theme: AppTheme) {
        _uiState.value = _uiState.value.copy(draftTheme = theme)
    }

    fun setDraftCoreConfig(coreConfig: CoreConfig) {
        _uiState.value = _uiState.value.copy(draftCoreConfig = coreConfig)
    }

    fun setCurrentVol(volume: Float) {
        _uiState.value = _uiState.value.copy(currentVol = volume)
    }

    fun applySettings(
        oldTheme: AppTheme,
        newName: String?,
        newTheme: AppTheme,
        draftCoreConfig: CoreConfig,
        volume: Float,
        endAction: () -> Unit
    ) {
        _uiState.setLoading(true)
        TRACKER.trackFunctionCall(details = "Apply settings clicked")

        scope.launch {
            try {
                // check if storage type changed and test connection if needed
                val currentCoreConfig = loadCoreConfig()
                var resetGame = false
                if (currentCoreConfig != draftCoreConfig) {
                    if (currentCoreConfig.gameStorageType != draftCoreConfig.gameStorageType) {
                        LOGGER.info("Storage type changed from ${currentCoreConfig.gameStorageType} to ${draftCoreConfig.gameStorageType}, testing connectivity...")
                        if (appState.game.hasStarted()) {
                            LOGGER.info("Saving Game...")
                            saveGame()
                        }
                        resetGame = true
                    }
                    val exception = runStorageHealthCheck(appState.service, testConf = draftCoreConfig, save = true)
                    if (exception != null) {
                        setGlobalError(exception, ErrorType.WARNING)
                        LOGGER.severe("Storage type change failed: ${exception.message}")
                    } else {
                        saveCoreConfig(draftCoreConfig)
                        LOGGER.info("Core config saved: storageType=${draftCoreConfig.gameStorageType}")
                    }
                }

                val playingAudios = appState.audioPool.getPlayingAudios()

                val loadedAudioPool = loadGameAudioPool(newTheme) { err ->
                    setGlobalError(err, ErrorType.WARNING)
                }

                appState.audioPool.merge(loadedAudioPool)

                // Apply audio volume
                parseVolume(volume, appState.audioPool)

                // Apply theme and player name

                try {
                    if (newName != null) Player(type = PieceType.BLACK, name = newName)
                    setPlayerName(newName)
                } catch (ex: Exception) {
                    setGlobalError(ex, ErrorType.INFO)
                }
                setDraftPlayerName(newName)
                setTheme(newTheme)
                if (resetGame) setGame(Game(service = GameService()))


                // Resume previously playing theme-related audios
                for (audio in playingAudios) {
                    val audioToPlay = when (audio) {
                        oldTheme.backgroundMusic -> newTheme.backgroundMusic
                        oldTheme.gameMusic -> newTheme.gameMusic
                        else -> null
                    }
                    if (audioToPlay != null && !appState.audioPool.isPlaying(audioToPlay)) {
                        appState.audioPool.play(audioToPlay)
                        LOGGER.info("Resuming audio: $audioToPlay")
                    }
                }

                val loadedAudios = appState.audioPool.pool.map { it.id }
                LOGGER.info("Loaded audios after applying settings: $loadedAudios")
                // Small delay for UX
                delay(100)
            } catch (e: Exception) {
                LOGGER.severe("Failed to apply settings: ${e.message}")
            } finally {
                _uiState.setLoading(false)
            }
            if (error == null) endAction()
        }
    }


    private fun parseVolume(volume: Float, current: AudioPool) {
        val minVol = current.getMasterVolumeRange()?.first ?: -20f
        if (volume <= minVol) {
            current.mute(true)
        } else {
            current.mute(false)
            current.setMasterVolume(volume)
        }
    }
}
