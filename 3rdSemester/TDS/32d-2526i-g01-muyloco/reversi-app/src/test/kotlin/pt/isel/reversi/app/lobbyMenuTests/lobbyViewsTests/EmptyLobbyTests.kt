package pt.isel.reversi.app.lobbyMenuTests.lobbyViewsTests

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.lobby.lobbyViews.*
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class EmptyLobbyTests {
    val reversiScope = ReversiScope(AppState.empty(EmptyGameService()))

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `test if empty lobby view is displayed correctly`() = runComposeUiTest {
        setContent {
            reversiScope.Empty()
        }
        onNodeWithTag(EMPTY_LOBBY_TAG).assertExists()
    }

    @Test
    fun `verify empty lobby text is correct`() = runComposeUiTest {
        setContent {
            reversiScope.Empty()
        }
        onNodeWithTag(EMPTY_LOBBY_TEXT_TAG).assertExists().assertTextEquals(TEXT_EMPTY_LOBBY)
    }

    @Test
    fun `verify empty lobby icon exists`() = runComposeUiTest {
        setContent {
            reversiScope.Empty()
        }
        onNodeWithTag(EMPTY_LOBBY_ICON_TAG).assertExists()
    }
}