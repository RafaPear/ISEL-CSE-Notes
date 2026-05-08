package pt.isel.reversi.app.gamePageTest.gamePageViewsTests

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.game.utils.TargetButton
import pt.isel.reversi.app.pages.game.utils.testTagTargetButtons
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ButtonsTest {
    val reversiScope = ReversiScope(AppState.empty(EmptyGameService()))

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `check if the Target button is displayed and clickable`() = runComposeUiTest {
        var clicked = false
        val target = true
        setContent {
            reversiScope.TargetButton(
                target = target,
                freeze = false,
                onClick = { clicked = true }
            )
        }

        onNodeWithTag(testTag = testTagTargetButtons(target = target))
            .assertExists()
            .performClick()

        assert(value = clicked) { "Button was not clicked" }
    }

    @Test
    fun `check if the Target button is disabled when freeze is true`() = runComposeUiTest {
        var clicked = false
        val target = false
        setContent {
            reversiScope.TargetButton(
                target = target,
                freeze = true,
                onClick = { clicked = true }
            )
        }

        onNodeWithTag(testTag = testTagTargetButtons(target = target))
            .assertExists()
            .performClick()

        assert(value = !clicked) { "Button should be disabled and not clickable" }
    }
}
