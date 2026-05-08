package pt.isel.reversi.app.newGamePageTests

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.*
import kotlinx.coroutines.CoroutineScope
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.newGamePage.*
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class NewGamePageTest {
    val appState = AppState.empty(EmptyGameService())
    val reversiScope = ReversiScope(appState)

    private fun vmForTest(scope: CoroutineScope) =
        NewGameViewModel(
            scope = scope,
            appState = appState,
            createGame = { _: Game -> },
            setGlobalError = { _, _ -> }
        )

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `check if the New Game page is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }
        onNodeWithTag(testTagNewGamePage()).assertExists()
    }

    @Test
    fun `check if the local game checkbox is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }
        onNodeWithTag(testTagLocalGameCheckbox()).assertExists()
    }

    @Test
    fun `check if the local game checkbox is checked by default`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }
        onNodeWithTag(testTagLocalGameCheckbox()).assertIsOn()
    }

    @Test
    fun `check if the Game Name text field is not displayed when local game is checked`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }
        // assume checkBox is checked by default
        onNodeWithTag(testTagGameNameTextField()).assertDoesNotExist()
    }

    @Test
    fun `check if the Game Name text field is displayed when local game is unchecked`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }

        // assume checkBox is checked by default, so we uncheck it
        onNodeWithTag(testTagLocalGameCheckbox()).performClick()
        onNodeWithTag(testTagGameNameTextField()).assertExists()
    }

    @Test
    fun `check if the player name text field is displayed when checkBox is unchecked`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }
        // assume checkBox is checked by default, so we uncheck it
        onNodeWithTag(testTagLocalGameCheckbox()).performClick()
        onNodeWithTag(testTagPlayerNameTextField()).assertExists()
    }

    @Test
    fun `check if the player name text field is not displayed when checkBox is checked`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }
        // assume checkBox is checked by default
        onNodeWithTag(testTagPlayerNameTextField()).assertDoesNotExist()
    }

    @Test
    fun `check if the Board Size text field is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }
        onNodeWithTag(testTagBoardSizeTextField()).assertExists()
    }

    @Test
    fun `check if the Piece dropdown button is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }
        onNodeWithTag(testTagDropdownButton()).assertExists()
    }

    @Test
    fun `check if the Piece dropdown is displayed when clicked`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }

        onNodeWithTag(testTagDropdownButton()).performClick()
        onNodeWithTag(testTagPieceDropdown()).assertExists()
    }

    @Test
    fun `check if the Piece dropdown is not displayed when not clicked`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }

        onNodeWithTag(testTagPieceDropdown()).assertDoesNotExist()
    }

    @Test
    fun `check if the Start Game button is displayed`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = vmForTest(scope)
            reversiScope.NewGamePage(
                viewModel = viewModel,
                playerNameChange = {},
                onLeave = {}
            )
        }
        onNodeWithTag(testTagStartGameButton()).assertExists()
    }
}
