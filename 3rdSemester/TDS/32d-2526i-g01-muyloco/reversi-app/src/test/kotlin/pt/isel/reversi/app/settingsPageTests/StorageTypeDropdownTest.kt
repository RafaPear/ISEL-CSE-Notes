package pt.isel.reversi.app.settingsPageTests

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.coroutines.CoroutineScope
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.testTagButtonConfirm
import pt.isel.reversi.app.pages.settingsPage.SettingsPage
import pt.isel.reversi.app.pages.settingsPage.SettingsViewModel
import pt.isel.reversi.app.pages.settingsPage.sections.testTagPopUpConfirmButton
import pt.isel.reversi.app.pages.settingsPage.sections.testTagStorageTypeButton
import pt.isel.reversi.app.pages.settingsPage.sections.testTagStorageTypeMenuItem
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class StorageTypeDropdownTest {
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
    fun `check if storage type button exists`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagStorageTypeButton()).assertExists()
    }

    @Test
    fun `verify storage type can be changed in view model`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            val initialConfig = viewModel.uiState.value.draftCoreConfig
            val newConfig = initialConfig.copy(gameStorageType = GameStorageType.DATABASE_STORAGE)
            viewModel.setDraftCoreConfig(newConfig)

            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagStorageTypeButton()).assertExists()
    }

    @Test
    fun `Verify that the popup appears the first time when changing the storage type`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        // Open the dropdown menu
        onNodeWithTag(testTagStorageTypeButton()).performClick()
        onNodeWithTag(testTagPopUpConfirmButton()).assertExists()
        onNodeWithTag(testTagButtonConfirm()).performClick()


        // Open the dropdown menu again to verify it doesn't appear again
        onNodeWithTag(testTagStorageTypeButton()).performClick()
        onNodeWithTag(testTagPopUpConfirmButton()).assertDoesNotExist()
    }

    @Test
    fun `check if FILE_STORAGE menu item can be tagged`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        // Open the dropdown menu
        onNodeWithTag(testTagStorageTypeButton()).performClick()
        onNodeWithTag(testTagButtonConfirm()).performClick()
        onNodeWithTag(testTagStorageTypeButton()).performClick()

        onNodeWithTag(testTagStorageTypeMenuItem(GameStorageType.FILE_STORAGE.name)).assertExists()
    }

    @Test
    fun `check if DATABASE_STORAGE menu item can be tagged`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        // Open the dropdown menu
        onNodeWithTag(testTagStorageTypeButton()).performClick()
        onNodeWithTag(testTagButtonConfirm()).performClick()
        onNodeWithTag(testTagStorageTypeButton()).performClick()

        onNodeWithTag(testTagStorageTypeMenuItem(GameStorageType.DATABASE_STORAGE.name)).assertExists()
    }

    @Test
    fun `verify storage type button displays current selected type`() = runComposeUiTest {

        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)

            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }

        onNodeWithTag(testTagStorageTypeButton()).assertExists()
    }
}
