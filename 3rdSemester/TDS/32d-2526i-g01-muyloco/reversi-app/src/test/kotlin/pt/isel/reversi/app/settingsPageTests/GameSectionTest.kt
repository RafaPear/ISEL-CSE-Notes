package pt.isel.reversi.app.settingsPageTests

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.coroutines.CoroutineScope
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.settingsPage.SettingsPage
import pt.isel.reversi.app.pages.settingsPage.SettingsViewModel
import pt.isel.reversi.app.pages.settingsPage.sections.testTagPlayerNameSettings
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class GameSectionTest {
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
    fun `check if player name text field exists`() = runComposeUiTest {
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
    fun `verify player name text field is empty initially`() = runComposeUiTest {
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
    fun `verify player name can be changed in text field`() = runComposeUiTest {
        var vm: SettingsViewModel? = null
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
            vm = viewModel
        }
        val newName = "TestPlayer"
        val textFieldNode = onNodeWithTag(testTagPlayerNameSettings())
        textFieldNode.assertExists()
        textFieldNode.performTextInput(newName)
        assertEquals(newName, vm!!.uiState.value.draftPlayerName)
    }
}
