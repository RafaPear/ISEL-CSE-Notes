package pt.isel.reversi.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.isel.reversi.app.app.AppTheme
import pt.isel.reversi.app.app.state.AppStateImpl
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.utils.LOGGER
import javax.swing.JOptionPane

/**
 * Creates the application menu bar with File, View, Dev, and Help menus.
 * Provides navigation to different pages and application controls.
 *
 * @param appState Global application state for navigation and configuration.
 * @param windowState Window state for toggling fullscreen mode.
 * @param setPage Callback to change the current page.
 * @param setGame Callback to update the current game instance.
 * @param setTheme Callback to reapply the current theme (used after fullscreen toggle).
 * @param setGlobalError Callback to surface errors triggered from menu actions.
 * @param exitAction Callback function to execute on application exit.
 */
@Composable
fun FrameWindowScope.MakeMenuBar(
    appState: AppStateImpl,
    scope: CoroutineScope,
    windowState: WindowState,
    setPage: (Page) -> Unit,
    setGame: (Game) -> Unit,
    setTheme: (AppTheme) -> Unit,
    setGlobalError: (Exception?, ErrorType?) -> Unit,
    exitAction: () -> Unit
) {
    MenuBar {
        Menu("Ficheiro") {
            Item("Novo Jogo") {
                setPage(Page.NEW_GAME)
            }
            Item("Definições") {
                setPage(Page.SETTINGS)
            }
            Item("Menu Principal") {
                setPage(Page.MAIN_MENU)
            }
            Item("Jogo Atual") {
                try {
                    appState.game.requireStartedGame()
                    setPage(Page.GAME)
                } catch (e: Exception) {
                    setGlobalError(e, null)
                }
            }
            Item("Sair do jogo atual") {
                if (appState.game.currGameName != null) {
                    scope.launch { appState.service.saveEndGame(appState.game) }
                }
                setGame(Game(service = appState.service))
                setPage(Page.MAIN_MENU)
            }
            Separator()
            Item("Sair") {
                exitAction()
            }
        }

        Menu("View") {
            Item("Toggle Fullscreen") {
                windowState.placement =
                    if (windowState.placement == WindowPlacement.Floating) WindowPlacement.Fullscreen
                    else WindowPlacement.Floating
                setTheme(appState.theme) // Force recomposition
            }
        }

        Menu("Dev") {
            Item("Nullify Game State") {
                setGame(Game(service = appState.service))
                LOGGER.info("Estado do jogo anulado para fins de teste.")
            }
            Item("Reload Config") {
                try {
                    setGame(appState.game.reloadConfig())
                    LOGGER.info("Config recarregada com sucesso.")
                } catch (e: Exception) {
                    setGlobalError(e, null)
                }
            }
            Item("Trigger Error") {
                val message = JOptionPane.showInputDialog(
                    null,
                    "Introduza a mensagem do erro",
                    "Trigger Error",
                    JOptionPane.QUESTION_MESSAGE
                )

                if (!message.isNullOrBlank()) {
                    val options = ErrorType.entries.toTypedArray()
                    val selectedIndex = JOptionPane.showOptionDialog(
                        null,
                        "Escolha o tipo de erro",
                        "Trigger Error",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        ErrorType.ERROR
                    )

                    val selectedType = options.getOrNull(selectedIndex) ?: ErrorType.ERROR
                    setGlobalError(Exception(message), selectedType)
                }
            }
            Item("Crash App") {
                throw RuntimeException("App crash triggered from Dev menu")
            }
        }

        Menu("Ajuda") {
            Item("Sobre") {
                setPage(Page.ABOUT)
            }
        }
    }
}
