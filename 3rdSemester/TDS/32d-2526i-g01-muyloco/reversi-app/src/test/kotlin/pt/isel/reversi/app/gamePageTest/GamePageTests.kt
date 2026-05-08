package pt.isel.reversi.app.gamePageTest

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.GameSession
import pt.isel.reversi.app.app.state.PagesState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.pages.game.GamePage
import pt.isel.reversi.app.pages.game.GamePageViewModel
import pt.isel.reversi.app.pages.game.utils.testTagBoard
import pt.isel.reversi.app.pages.game.utils.testTagCellView
import pt.isel.reversi.app.pages.game.utils.testTagPlayerScore
import pt.isel.reversi.app.pages.game.utils.testTagTargetButtons
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class GamePageTests {

    fun vmForTest(scope: CoroutineScope) =
        GamePageViewModel(
            game,
            scope,
            {},
            { },
            {},
            null,
            { _, _ -> }
        )

    val game = runBlocking {
        startNewGame(
            side = 4,
            players = MatchPlayers(Player(type = PieceType.BLACK), Player(type = PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )
    }

    val appState = AppState.empty(service = EmptyGameService()).copy(
        gameSession = GameSession(game, null),
        pagesState = PagesState(Page.GAME, Page.NONE, null)
    )

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `check if player score change after a move`() = runComposeUiTest {

        setContent {
            val scope = rememberCoroutineScope()
            val gameViewModel = vmForTest(scope)
            ReversiScope(appState).GamePage(gameViewModel, onLeave = { })
        }

        val players = game.gameState?.players!!

        players.forEach { player ->
            onNodeWithTag(testTag = testTagPlayerScore(player)).assertExists()
        }

        val validMove = game.getAvailablePlays().first()
        val expectedGameState = game.play(coordinate = validMove)

        //find the valid cell and perform click
        onNodeWithTag(testTag = testTagCellView(coordinate = validMove)).performClick()

        val newPlayers = expectedGameState.gameState?.players!!

        onNodeWithTag(testTag = testTagPlayerScore(players.first())).assertDoesNotExist()
        onNodeWithTag(testTag = testTagPlayerScore(players.last())).assertDoesNotExist()

        onNodeWithTag(testTag = testTagPlayerScore(player = newPlayers.first())).assertExists()
        onNodeWithTag(testTag = testTagPlayerScore(player = newPlayers.last())).assertExists()
    }

    @Test
    fun `check if player score not change if freeze is true`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            val gameViewModel = vmForTest(scope)
            ReversiScope(appState).GamePage(gameViewModel, onLeave = { }, freeze = true)
        }

        val players = game.gameState?.players!!

        players.forEach { player ->
            onNodeWithTag(testTag = testTagPlayerScore(player)).assertExists()
        }

        val validMove = game.getAvailablePlays().first()

        //find the valid cell and perform click
        onNodeWithTag(testTag = testTagCellView(coordinate = validMove)).performClick()

        //verify if the players score have not changed
        players.forEach { player ->
            onNodeWithTag(testTag = testTagPlayerScore(player)).assertExists()
        }
    }

    @Test
    fun `check if ghost pieces shows when target mode is on`() = runComposeUiTest {
        val board = game.gameState!!.board
        val expectedGhostPieces = game.getAvailablePlays().size
        val expectedPieces = board.totalBlackPieces + board.totalWhitePieces

        setContent {
            val scope = rememberCoroutineScope()
            val gameViewModel = vmForTest(scope)
            ReversiScope(appState).GamePage(gameViewModel, onLeave = { })
        }

        var countPieces = 0

        //check if the pieces are correct before target mode
        onNodeWithTag(testTagBoard(), useUnmergedTree = true).onChildren().fetchSemanticsNodes().forEach { cellNode ->
            val cell = cellNode.children
            if (cell.isNotEmpty()) countPieces++
        }

        assertEquals(expectedPieces, countPieces)

        //click on target mode button
        onNodeWithTag(testTagTargetButtons(false)).performClick()

        //check if the pieces are correct after target mode
        countPieces = 0
        onNodeWithTag(testTagBoard(), useUnmergedTree = true).onChildren().fetchSemanticsNodes().forEach { cellNode ->
            val cell = cellNode.children
            if (cell.isNotEmpty()) countPieces++
        }

        assertEquals(expectedPieces + expectedGhostPieces, countPieces)
    }

    @Test
    fun `check if ghost pieces not shows when target mode is on and freeze is true`() = runComposeUiTest {
        val board = game.gameState!!.board
        val expectedPieces = board.totalBlackPieces + board.totalWhitePieces

        setContent {
            val scope = rememberCoroutineScope()
            val gameViewModel = vmForTest(scope)
            ReversiScope(appState).GamePage(gameViewModel, onLeave = { }, freeze = true)
        }

        var countPieces = 0

        //check if the pieces are correct before target mode
        onNodeWithTag(testTagBoard(), useUnmergedTree = true).onChildren().fetchSemanticsNodes().forEach { cellNode ->
            val cell = cellNode.children
            if (cell.isNotEmpty()) countPieces++
        }

        assertEquals(expectedPieces, countPieces)

        //click on target mode button
        onNodeWithTag(testTagTargetButtons(false)).performClick()

        //check if the pieces not changed after target mode
        countPieces = 0
        onNodeWithTag(testTagBoard(), useUnmergedTree = true).onChildren().fetchSemanticsNodes().forEach { cellNode ->
            val cell = cellNode.children
            if (cell.isNotEmpty()) countPieces++
        }

        assertEquals(expectedPieces, countPieces)
    }

    @Test
    fun `check if game is saved when play valid move`() = runComposeUiTest {
        val expectedGame = game.play(game.getAvailablePlays()[0])
        var gameSaved: Game? = null

        setContent {
            val scope = rememberCoroutineScope()
            val gameViewModel = GamePageViewModel(game, scope, { gameSaved = it }, { }, {}, null, { _, _ -> })

            ReversiScope(appState).GamePage(
                viewModel = gameViewModel, onLeave = { })
        }

        //click on a valid cell to change the game state on view model
        onNodeWithTag(testTagCellView(game.getAvailablePlays()[0]), useUnmergedTree = true).performClick()

        assertEquals(expectedGame, gameSaved!!)
    }
}