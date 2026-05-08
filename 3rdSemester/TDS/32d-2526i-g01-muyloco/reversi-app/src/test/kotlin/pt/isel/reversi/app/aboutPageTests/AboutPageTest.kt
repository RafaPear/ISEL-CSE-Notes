package pt.isel.reversi.app.aboutPageTests

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.aboutPage.AboutPage
import pt.isel.reversi.app.pages.aboutPage.AboutPageViewModel
import pt.isel.reversi.app.pages.aboutPage.testTagAboutPage
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class AboutPageTest {
    val appState = AppState.empty(EmptyGameService())
    val reversiScope = ReversiScope(appState)

    private val aboutPageViewModel: AboutPageViewModel
        get() = AboutPageViewModel(
            setGlobalError = { _, _ -> }
        )

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `check if the About page is displayed`() = runComposeUiTest {
        val viewModel = aboutPageViewModel
        setContent {
            reversiScope.AboutPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }
        onNodeWithTag(testTagAboutPage()).assertExists()
    }
}
