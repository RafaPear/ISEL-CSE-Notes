package pt.isel.reversi.app.mainMenuTests

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.pages.menu.*
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MainMenuTest {
    val appState = AppState.empty(EmptyGameService())
    val reversiScope = ReversiScope(appState)

    private val mainMenuViewModel: MainMenuViewModel
        get() = MainMenuViewModel(
            appState = appState,
            setPage = {},
            setGlobalError = { _, _ -> }
        )

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `check if the Main Menu page is displayed`() = runComposeUiTest {
        val viewModel = mainMenuViewModel
        setContent {
            reversiScope.MainMenu(
                viewModel = viewModel,
                onLeave = {},
                isTestMode = true
            )
        }
        waitForIdle()
        onNodeWithTag(testTagMainMenu()).assertExists()
    }

    @Test
    fun `check if the New Game button is displayed`() = runComposeUiTest {
        val viewModel = mainMenuViewModel
        setContent {
            reversiScope.MainMenu(
                viewModel = viewModel,
                onLeave = {},
                isTestMode = true
            )
        }
        onNodeWithTag(testTagNewGameButton()).assertExists()
    }

    @Test
    fun `check if the Lobby button is displayed`() = runComposeUiTest {
        val viewModel = mainMenuViewModel
        setContent {
            reversiScope.MainMenu(
                viewModel = viewModel,
                onLeave = {},
                isTestMode = true
            )
        }
        onNodeWithTag(testTagLobbyButton()).assertExists()
    }

    @Test
    fun `check if the Settings button is displayed`() = runComposeUiTest {
        val viewModel = mainMenuViewModel
        setContent {
            reversiScope.MainMenu(
                viewModel = viewModel,
                onLeave = {},
                isTestMode = true
            )
        }
        onNodeWithTag(testTagSettingsButton()).assertExists()
    }

    @Test
    fun `check if the About button is displayed`() = runComposeUiTest {
        val viewModel = mainMenuViewModel
        setContent {
            reversiScope.MainMenu(
                viewModel = viewModel,
                onLeave = {},
                isTestMode = true
            )
        }
        onNodeWithTag(testTagAboutButton()).assertExists()
    }

    @Test
    fun `check if on click button call setPage`() = runComposeUiTest {
        var newPage: Page? = null
        val viewModel = MainMenuViewModel(
            appState = appState,
            setPage = { page -> newPage = page },
            setGlobalError = { _, _ -> }
        )
        setContent {
            reversiScope.MainMenu(
                viewModel = viewModel,
                onLeave = {},
                isTestMode = true
            )
        }
        onNodeWithTag(testTagNewGameButton()).performClick()
        assert(newPage == Page.NEW_GAME)

        onNodeWithTag(testTagLobbyButton()).performClick()
        assert(newPage == Page.LOBBY)

        onNodeWithTag(testTagSettingsButton()).performClick()
        assert(newPage == Page.SETTINGS)

        onNodeWithTag(testTagAboutButton()).performClick()
        assert(newPage == Page.ABOUT)
    }
}
