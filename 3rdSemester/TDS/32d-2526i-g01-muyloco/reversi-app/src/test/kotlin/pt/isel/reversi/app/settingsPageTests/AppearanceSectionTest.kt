package pt.isel.reversi.app.settingsPageTests

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.*
import kotlinx.coroutines.CoroutineScope
import pt.isel.reversi.app.app.AppThemes
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.settingsPage.SettingsPage
import pt.isel.reversi.app.pages.settingsPage.SettingsViewModel
import pt.isel.reversi.app.pages.settingsPage.sections.AppearanceSection
import pt.isel.reversi.app.pages.settingsPage.sections.testTagThemeButton
import pt.isel.reversi.app.pages.settingsPage.sections.testTagThemeDropdown
import pt.isel.reversi.app.pages.settingsPage.sections.testTagThemeMenuItem
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AppearanceSectionTest {
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
    fun `check if theme button exists`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagThemeButton()).assertExists()
    }

    @Test
    fun `check if theme dropdown exists when menu is opened`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }

        onNodeWithTag(testTagThemeButton()).performClick()
        onNodeWithTag(testTagThemeDropdown()).assertExists()
    }

    @Test
    fun `verify theme dropdown is dismissed when an option is selected`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }

        onNodeWithTag(testTagThemeButton()).performClick()
        onNodeWithTag(testTagThemeDropdown()).assertExists()

        // Select the first theme option
        val firstThemeName = AppThemes.entries.first().appTheme.name
        onNodeWithTag(testTagThemeMenuItem(firstThemeName))
            .performClick()

        // Verify the dropdown is dismissed
        onNodeWithTag(testTagThemeDropdown()).assertDoesNotExist()
    }

    @Test
    fun `check if all theme options exist in the dropdown`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }

        onNodeWithTag(testTagThemeButton()).performClick()

        AppThemes.entries.forEach { entry ->
            val themeName = entry.appTheme.name
            onNodeWithTag(testTagThemeMenuItem(themeName)).assertExists()
        }
    }

    @Test
    fun `verify selecting a theme option calls the onClick handler`() = runComposeUiTest {
        var selectedThemeName: String? = null
        setContent {
            reversiScope.AppearanceSection(
                appState.theme,
                appState.theme,
                onClick = { theme ->
                    selectedThemeName = theme.name
                }
            )
        }

        onNodeWithTag(testTagThemeButton()).performClick()
        val targetThem = AppThemes.BENFICA.appTheme.name
        onNodeWithTag(testTagThemeMenuItem(targetThem)).performClick()
        assert(selectedThemeName == targetThem)
    }

    @Test
    fun `verify theme button displays the correct current theme name`() = runComposeUiTest {
        val currentTheme = AppThemes.NORD.appTheme
        setContent {
            reversiScope.AppearanceSection(
                currentTheme,
                appState.theme,
                onClick = {}
            )
        }
        onNodeWithTag(testTagThemeButton())
            .assertTextEquals(currentTheme.name)
    }
}
