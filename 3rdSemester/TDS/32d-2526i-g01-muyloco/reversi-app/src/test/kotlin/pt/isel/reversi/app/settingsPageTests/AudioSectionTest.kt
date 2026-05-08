package pt.isel.reversi.app.settingsPageTests

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.*
import kotlinx.coroutines.CoroutineScope
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.settingsPage.SettingsPage
import pt.isel.reversi.app.pages.settingsPage.SettingsViewModel
import pt.isel.reversi.app.pages.settingsPage.sections.*
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class AudioSectionTest {
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
    fun `check if volume slider exists`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagVolumeSlider()).assertExists()
    }

    @Test
    fun `check if volume label text exists`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = settingsViewModel(scope)
            reversiScope.SettingsPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagVolumeLabelText()).assertExists()
    }

    @Test
    fun `verify volume label shows Mudo when volume is at minimum`() = runComposeUiTest {
        setContent {
            reversiScope.AudioSection(
                currentVol = DEFAULT_MIN_VOLUME,
                onVolumeChange = {_-> }
            )
        }
        onNodeWithTag(testTagVolumeLabelText()).assertTextEquals("Mudo")
    }

    @Test
    fun `verify volume label shows correct percentage`() = runComposeUiTest {
        val midVolume = (DEFAULT_MIN_VOLUME + DEFAULT_MAX_VOLUME) / 2
        val volume25 = midVolume / 2
        val volume75 = midVolume + (midVolume / 2)
        val maxVolume = DEFAULT_MAX_VOLUME
        val volume1 = DEFAULT_MIN_VOLUME + 1f

        val volumes = listOf(midVolume, volume25, volume75, maxVolume, volume1)

        for (vol in volumes) {
            val expectedPercent = vol.toPercent(DEFAULT_MIN_VOLUME, DEFAULT_MAX_VOLUME)
            setContent {
                reversiScope.AudioSection(
                    currentVol = vol,
                    onVolumeChange = {_-> }
                )
            }
            onNodeWithTag(testTagVolumeLabelText()).assertTextEquals("$expectedPercent%")
        }
    }

    /*
     * Reference:
     * - [performTouchInput](https://composables.com/docs/androidx.compose.ui/ui-test/functions/performTouchInput)
     */
    @Test
    fun `verify if slider call onVolumeChange when moved`() = runComposeUiTest {
        var changedVolume = DEFAULT_MIN_VOLUME
        setContent {
            reversiScope.AudioSection(
                currentVol = DEFAULT_MIN_VOLUME,
                onVolumeChange = { newVol -> changedVolume = newVol }
            )
        }
        val sliderNode = onNodeWithTag(testTagVolumeSlider())
        val expected = (DEFAULT_MIN_VOLUME + DEFAULT_MAX_VOLUME) / 2

        sliderNode.performTouchInput {
            down(center)
        }
        sliderNode.performTouchInput {
            up()
        }
        assertEquals(expected, changedVolume)
    }
}
