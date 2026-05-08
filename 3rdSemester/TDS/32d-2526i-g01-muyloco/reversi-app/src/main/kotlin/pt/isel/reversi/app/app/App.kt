package pt.isel.reversi.app.app

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.*
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.painterResource
import pt.isel.reversi.app.MakeMenuBar
import pt.isel.reversi.app.app.state.*
import pt.isel.reversi.app.pages.*
import pt.isel.reversi.app.utils.AppScreenSwitcher
import pt.isel.reversi.app.utils.initializeAppArgs
import pt.isel.reversi.app.utils.installFatalCrashLogger
import pt.isel.reversi.app.utils.runStorageHealthCheck
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.loadCoreConfig
import pt.isel.reversi.utils.ExportFormat
import pt.isel.reversi.utils.LOGGER
import pt.isel.reversi.utils.TRACKER
import reversi.reversi_app.generated.resources.Res
import reversi.reversi_app.generated.resources.reversi
import java.awt.Dimension
import java.lang.System.setProperty


class App(args: Array<String>) {
    private val initializedArgs =
        initializeAppArgs(args) ?: throw IllegalStateException("Failed to initialize application arguments")

    // unica forma que permite sincronizar o estado do app no exit
    private val appJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + appJob)

    private val initialGameService = GameService()

    init {
        setProperty("apple.awt.application.name", "Reversi-TDS-ISEL")

        // Initialize logging utilities
        TRACKER.setTrackerFilePath(autoSave = true)
        LOGGER.info("Development tracker initialized with auto-save enabled")

        // Start storage health check in the background
        scope.launch {
            val conf = loadCoreConfig()
            LOGGER.info("STARTING STORAGE HEALTH CHECK TO SERVICE TYPE: ${conf.gameStorageType.name}")
            val exception = runStorageHealthCheck(service = initialGameService, testConf = conf, save = true)
            if (exception != null)
                LOGGER.severe("Storage type change failed: ${exception.message}")
        }
    }

    /**
     * Starts the Reversi application by creating the main window and initializing the UI.
     * Sets up the window state, application state, and page navigation system.
     * Installs crash logging and handles window close events gracefully.
     */
    fun start() {
        application {
            val windowState = rememberWindowState(
                placement = WindowPlacement.Floating,
                position = WindowPosition.PlatformDefault
            )

            val gameSession = remember {
                mutableStateOf(
                    value = GameSession(
                        Game(service = initialGameService), null
                    )
                )
            }
            val audioThemeState = remember {
                mutableStateOf(AudioThemeState(initializedArgs.audioPool, AppThemes.DARK.appTheme))
            }

            val pagesState = remember {
                mutableStateOf(PagesState(Page.MAIN_MENU, Page.NONE, null))
            }

            val appState = AppState(
                gameSession = gameSession.value,
                pagesState = pagesState.value,
                audioThemeState = audioThemeState.value,
            )

            fun exit() {
                safeExitApplication(appState, pagesState) { exitApplication() }
            }

            installFatalCrashLogger { exit() }

            Window(
                onCloseRequest = { exit() },
                title = "Reversi-DEV",
                icon = painterResource(Res.drawable.reversi),
                state = windowState,
            ) {
                window.minimumSize = Dimension(800, 800)
                MakeMenuBar(
                    appState,
                    scope = scope,
                    windowState,
                    setPage = { pagesState.setPage(it) },
                    setGame = { gameSession.setGame(it) },
                    setTheme = { audioThemeState.setTheme(it) },
                    setGlobalError = { it, type ->
                        pagesState.setGlobalError(it, type)
                    },
                ) { exit() }


                // out [UiState] for allow ViewModel covariance
                val currentViewModel: ViewModel<out UiState>? =
                    remember(pagesState.value) {
                        pagesState.value.page.createViewModel(
                            scope = scope,
                            appState = appState,
                            gameSession = gameSession,
                            audioThemeState = audioThemeState,
                            pagesState = pagesState,
                        )
                    }

                TRACKER.trackRecomposition(category = "App.Main")

                val currentPage = pagesState.value.page
                // Log navigation once per page change
                LaunchedEffect(currentPage) {
                    LOGGER.info("Navigating to page: $currentPage")
                }

                AppScreenSwitcher(pagesState.value, audioThemeState.value.theme) { currentPage ->
                    with(ReversiScope(appState)) {
                        if (currentViewModel == null) {
                            LOGGER.severe("No ViewModel found for page: $currentPage")
                            return@AppScreenSwitcher
                        }

                        val view = currentPage.createPageView(
                            vm = currentViewModel,
                            gameSession = gameSession,
                            pagesState = pagesState,
                        )

                        view()
                    }
                }
            }
        }
    }


    fun safeExitApplication(appState: AppState, pagesState: MutableState<PagesState>, exitApplication: () -> Unit) {
        LOGGER.info("Application exit requested")
        LOGGER.info("Stopping compose application...")

        try {
            LOGGER.info("Saving tracking data...")
            TRACKER.saveToFile()
            LOGGER.info("Tracking data saved.")

            LOGGER.info("Cancelling application coroutines...")
            appJob.cancel()
            pagesState.setPage(Page.NONE)
            runBlocking {
                appJob.join()
                LOGGER.info("Application coroutines finished.")
                LOGGER.info("Saving game state...")
                appState.game.saveEndGame()
                LOGGER.info("Game state saved.")
                LOGGER.info("Closing game storage...")
                appState.game.closeStorage()
                LOGGER.info("Game storage closed.")
            }
            LOGGER.info("Destroying audio pool...")
            getStateAudioPool(appState).destroy()
            LOGGER.info("Audio pool destroyed. Application exited safely.")
        } catch (e: Exception) {
            LOGGER.info("Did it blow up? ${e.message}")
        }

        for (type in ExportFormat.entries) TRACKER.saveToFile(type)

        for (handler in LOGGER.handlers) {
            handler.flush()
            handler.close()
        }
        System.out.flush()
        exitApplication()
    }
}
