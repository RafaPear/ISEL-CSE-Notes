package pt.isel.reversi.app.settingsPageTests

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.coroutines.CoroutineScope
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.settingsPage.SettingsPage
import pt.isel.reversi.app.pages.settingsPage.SettingsViewModel
import pt.isel.reversi.app.pages.settingsPage.sections.*
import pt.isel.reversi.app.pages.settingsPage.testTagApplyButton
import pt.isel.reversi.app.pages.settingsPage.testTagSettingsPage
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class SettingsPageTest {
    val appState = AppState.empty(EmptyGameService())
    val reversiScope = ReversiScope(appState)

    fun settingsViewModel(scope: CoroutineScope) =
        SettingsViewModel(
            scope = scope,
            appState = appState,
            setTheme = {},
            setPlayerName = {},
            saveGame = {},
            setGame = {},
            setGlobalError = { _, _ -> }
        )

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `check if the Settings page is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagSettingsPage()).assertExists()
    }

    @Test
    fun `check if the Game section is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagGameSection()).assertExists()
    }

    @Test
    fun `check if the player name text field is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagPlayerNameSettings()).assertExists()
    }

    @Test
    fun `check if the Core Config section is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagCoreConfigSection()).assertExists()
    }

    @Test
    fun `check if the Audio section is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagAudioSection()).assertExists()
    }

    @Test
    fun `check if the Appearance section is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagAppearanceSection()).assertExists()
    }

    @Test
    fun `check if the Apply button is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagApplyButton()).assertExists()
    }
}
