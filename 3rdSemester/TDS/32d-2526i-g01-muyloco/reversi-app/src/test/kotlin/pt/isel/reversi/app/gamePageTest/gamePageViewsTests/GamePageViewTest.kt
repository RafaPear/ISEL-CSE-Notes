package pt.isel.reversi.app.gamePageTest.gamePageViewsTests

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.coroutines.runBlocking
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.game.GamePageView
import pt.isel.reversi.app.pages.game.utils.testTagGamePage
import pt.isel.reversi.app.pages.game.utils.testTagPlayerScore
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class GamePageViewTest {
    val game = runBlocking {
        startNewGame(
            side = 4,
            players = MatchPlayers(Player(type = PieceType.BLACK), Player(type = PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )
    }

    val reversiScope = ReversiScope(AppState.empty(EmptyGameService()))

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `check if the GamePage is displayed`() = runComposeUiTest {

        setContent {
            reversiScope.GamePageView(
                game = game,
                freeze = false,
                onCellClick = {},
                getAvailablePlays = { emptyList() },
                setTargetMode = {},
                pass = {}
            )
        }
        onNodeWithTag(testTag = testTagGamePage())
            .assertExists()
    }

    @Test
    fun `check if have a board`() = runComposeUiTest {
        setContent {
            reversiScope.GamePageView(
                game = game,
                freeze = false,
                onCellClick = {},
                getAvailablePlays = { emptyList() },
                setTargetMode = {},
                pass = {}
            )
        }
        onNodeWithTag(testTag = testTagGamePage()).assertExists()
    }

    @Test
    fun `check if have a two players on Score`() = runComposeUiTest {
        setContent {
            reversiScope.GamePageView(
                game = game,
                freeze = false,
                onCellClick = {},
                getAvailablePlays = { emptyList() },
                setTargetMode = {},
                pass = {}
            )
        }

        val players = game.gameState?.players

        players?.forEach { player ->
            onNodeWithTag(testTag = testTagPlayerScore(player)).assertExists()
        }
    }

    @Test
    fun `check if don't have players on Score when gameState is null`() = runComposeUiTest {
        setContent {
            reversiScope.GamePageView(
                game = game.copy(gameState = null),
                freeze = false,
                onCellClick = {},
                getAvailablePlays = { emptyList() },
                setTargetMode = {},
                pass = {}
            )
        }

        onNodeWithTag(testTag = testTagPlayerScore(game.gameState?.players!!.first()))
            .assertDoesNotExist()
    }
}