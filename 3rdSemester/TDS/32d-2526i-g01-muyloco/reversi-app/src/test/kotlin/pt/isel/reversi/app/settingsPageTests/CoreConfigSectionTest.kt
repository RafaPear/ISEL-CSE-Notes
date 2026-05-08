package pt.isel.reversi.app.settingsPageTests

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.*
import kotlinx.coroutines.CoroutineScope
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.testTagButtonConfirm
import pt.isel.reversi.app.pages.settingsPage.SettingsPage
import pt.isel.reversi.app.pages.settingsPage.SettingsViewModel
import pt.isel.reversi.app.pages.settingsPage.sections.CoreConfigSection
import pt.isel.reversi.app.pages.settingsPage.sections.testTagPopUpConfirmButton
import pt.isel.reversi.app.pages.settingsPage.sections.testTagSavesPathTextField
import pt.isel.reversi.app.pages.settingsPage.sections.testTagStorageTypeButton
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.loadCoreConfig
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class CoreConfigSectionTest {
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
    fun `check if saves path text field appears for FILE_STORAGE`() = runComposeUiTest {
        val path = "test_configs/core_config.properties"
        setContent {
            reversiScope.CoreConfigSection(
                coreConfig = loadCoreConfig().copy(savesPath = path, gameStorageType = GameStorageType.FILE_STORAGE),
                onConfigChange = {}
            )
        }
        onNodeWithTag(testTagSavesPathTextField()).assertTextContains(path)
    }

    @Test
    fun `verify when change path in saves path text field, first time, then popUp appears`() = runComposeUiTest {
        val path = "test_configs/core_config.properties"
        setContent {
            reversiScope.CoreConfigSection(
                coreConfig = loadCoreConfig().copy(savesPath = path, gameStorageType = GameStorageType.FILE_STORAGE),
                onConfigChange = {}
            )
        }
        val newPath = "new_test_configs/core_config.properties"
        onNodeWithTag(testTagSavesPathTextField()).performTextInput(newPath)
        onNodeWithTag(testTagPopUpConfirmButton()).assertExists()

        // Confirm the pop-up to proceed
        onNodeWithTag(testTagButtonConfirm()).performClick()

        //second time changing the path, pop-up should not appear
        val anotherPath = "another_test_configs/core_config.properties"
        onNodeWithTag(testTagSavesPathTextField()).performTextInput(anotherPath)
        onNodeWithTag(testTagPopUpConfirmButton()).assertDoesNotExist()
    }

    @Test
    fun `check that saves path text field does not appear for DATABASE_STORAGE`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            val coreConfig = viewModel.uiState.value.draftCoreConfig
            viewModel.setDraftCoreConfig(coreConfig.copy(gameStorageType = GameStorageType.DATABASE_STORAGE))

            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagSavesPathTextField()).assertDoesNotExist()
    }
}
