package pt.isel.reversi.app.lobbyMenuTests

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.*
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils.*
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class UtilsTests {
    val reversiScope = ReversiScope(AppState.empty(EmptyGameService()))

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `navButton executes onClick when clicked`() = runComposeUiTest {
        var clicked = false
        val testTag = testTagNavButton("back")

        setContent {
            Box {
                NavButton(
                    modifier = Modifier.testTag(testTag),
                    alignment = Alignment.CenterStart,
                    onClick = { clicked = true },
                    icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
                )
            }
        }

        onNodeWithTag(testTag).performClick()
        assert(clicked)
    }

    @Test
    fun `pageIndicator has correct number of indicators`() = runComposeUiTest {
        val totalIndicators = 5
        val currentIndicator = 2

        setContent {
            reversiScope.PageIndicators(
                total = totalIndicators,
                current = currentIndicator
            )
        }

        val count = onNodeWithTag(testTagPageIndicators(), true)
            .onChildren().fetchSemanticsNodes().size

        assert(count == totalIndicators)
    }

    @Test
    fun `pageIndicator text is correct`() = runComposeUiTest {
        val totalIndicators = 4
        val currentIndicator = 2
        val expectedText = textPageIndicator(currentIndicator, totalIndicators)

        setContent {
            reversiScope.PageIndicators(
                total = totalIndicators,
                current = currentIndicator
            )
        }

        onNodeWithTag("page_indicator_text").assertTextEquals(expectedText)
    }

    @Test
    fun `search component updates value on input change`() = runComposeUiTest {
        var searchText = ""

        setContent {
            reversiScope.Search(
                search = searchText,
                onValueChange = { searchText = it }
            )
        }

        val inputText = "New Search"
        onNodeWithTag(testTagSearch()).performTextInput(inputText)

        assert(searchText == inputText)
    }

    @Test
    fun `search component has correct value`() = runComposeUiTest {
        val searchValue = "Test Search"
        setContent {
            reversiScope.Search(
                search = searchValue,
                onValueChange = {}
            )
        }

        onNodeWithTag(testTagSearch(), useUnmergedTree = true).assertTextEquals(searchValue)
    }
}