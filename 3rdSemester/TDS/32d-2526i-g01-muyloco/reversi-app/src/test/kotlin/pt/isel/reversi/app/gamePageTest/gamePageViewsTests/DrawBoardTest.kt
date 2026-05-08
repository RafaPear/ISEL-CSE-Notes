package pt.isel.reversi.app.gamePageTest.gamePageViewsTests

import androidx.compose.ui.test.*
import kotlinx.coroutines.runBlocking
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.game.utils.DrawBoard
import pt.isel.reversi.app.pages.game.utils.testTagBoard
import pt.isel.reversi.app.pages.game.utils.testTagCellView
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
import kotlin.test.fail

@OptIn(ExperimentalTestApi::class)
class DrawBoardTest {
    val reversiScope = ReversiScope(AppState.empty(EmptyGameService()))

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `DrawBoard test if all cells are displayed`() = runComposeUiTest {
        val expectedSide = 4
        setContent {
            val game = runBlocking {
                startNewGame(
                    side = expectedSide,
                    players = MatchPlayers(Player(type = PieceType.BLACK)),
                    firstTurn = PieceType.BLACK,
                    currGameName = null,
                    service = EmptyGameService()
                )
            }

            val gameState = game.gameState!!

            reversiScope.DrawBoard(
                target = false,
                gameState = gameState,
                onCellClick = { fail("onCellClick should not be called during this test") },
                getAvailablePlays = { emptyList() }
            )
        }

        val boardTag = testTagBoard()
        onNodeWithTag(testTag = boardTag, useUnmergedTree = true)
            .onChildren()
            .assertCountEquals(expectedSide * expectedSide)
    }

    @Test
    fun `DrawBoard test if all pieces on the game are displayed`() = runComposeUiTest {
        val expectedSide = 4
        var game = startNewGame(
            side = expectedSide,
            players = MatchPlayers(Player(type = PieceType.BLACK), Player(type = PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )
        game = game.play(coordinate = game.getAvailablePlays()[0])
        val board = game.gameState!!.board
        val expectedPiecesCount = board.totalBlackPieces + board.totalWhitePieces

        setContent {
            val gameState = game.gameState!!

            reversiScope.DrawBoard(
                target = false,
                gameState = gameState,
                onCellClick = { fail("onCellClick should not be called during this test") },
                getAvailablePlays = { emptyList() }
            )
        }

        val boardTag = testTagBoard()
        var countPieces = 0

        //On Board make list of children (cells),
        // for each cell check if it has a piece, if so increment countPieces
        onNodeWithTag(testTag = boardTag, useUnmergedTree = true)
            .onChildren().fetchSemanticsNodes().forEach { cellNode ->
                val pieceNode = cellNode.children
                if (pieceNode.isNotEmpty()) countPieces++
            }
        assert(countPieces == expectedPiecesCount) {
            "Expected $expectedPiecesCount pieces, but found $countPieces"
        }
    }

    @Test
    fun `DrawBoard test if onCellClick is called when empty cell is clicked`() = runComposeUiTest {
        val expectedSide = 4
        val game = startNewGame(
            side = expectedSide,
            players = MatchPlayers(Player(type = PieceType.BLACK), Player(type = PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )
        val coordinateToClick = game.getAvailablePlays()[0]
        var onCellClickCalled = false

        val gameState = game.gameState!!

        setContent {
            reversiScope.DrawBoard(
                target = false,
                gameState = gameState,
                onCellClick = { coordinate ->
                    if (coordinate == coordinateToClick) {
                        onCellClickCalled = true
                    }
                },
                getAvailablePlays = { emptyList() }
            )
        }

        val cellTagToClick = testTagCellView(coordinateToClick)

        onNodeWithTag(testTag = cellTagToClick, useUnmergedTree = true).performClick()

        assert(value = onCellClickCalled) {
            "Expected onCellClick to be called for coordinate $coordinateToClick, but it was not."
        }
    }

    @Test
    fun `DrawBoard test if onCellClick is not called when frozen and cell is clicked`() = runComposeUiTest {
        val expectedSide = 4
        val game = startNewGame(
            side = expectedSide,
            players = MatchPlayers(Player(type = PieceType.BLACK), Player(type = PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )
        val coordinateToClick = game.getAvailablePlays()[0]
        var onCellClickCalled = false

        val gameState = game.gameState!!

        setContent {
            reversiScope.DrawBoard(
                target = false,
                gameState = gameState,
                freeze = true,
                onCellClick = { coordinate ->
                    if (coordinate == coordinateToClick) {
                        onCellClickCalled = true
                    }
                },
                getAvailablePlays = { emptyList() }
            )
        }

        val cellTagToClick = testTagCellView(coordinateToClick)

        onNodeWithTag(testTag = cellTagToClick, useUnmergedTree = true).performClick()

        assert(value = !onCellClickCalled) {
            "Expected onCellClick to not be called for coordinate $coordinateToClick when frozen, but it was."
        }
    }

    @Test
    fun `DrawBoard test if onCellClick is not called when non-empty cell is clicked`() = runComposeUiTest {
        val expectedSide = 4
        val game = startNewGame(
            side = expectedSide,
            players = MatchPlayers(Player(type = PieceType.BLACK), Player(type = PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )

        val coordinateToClick = game.gameState!!.board.last().coordinate
        var onCellClickCalled = false

        setContent {
            val gameState = game.gameState!!

            reversiScope.DrawBoard(
                target = false,
                gameState = gameState,
                onCellClick = { coordinate ->
                    if (coordinate == coordinateToClick) {
                        onCellClickCalled = true
                    }
                },
                getAvailablePlays = { emptyList() }
            )
        }

        val cellTagToClick = testTagCellView(coordinateToClick)

        onNodeWithTag(testTag = cellTagToClick, useUnmergedTree = true).performClick()

        assert(value = !onCellClickCalled) {
            "Expected onCellClick to not be called for non-empty cell at $coordinateToClick, but it was."
        }
    }

    @Test
    fun `DrawBoard test if all ghost pieces are displayed in target mode`() = runComposeUiTest {
        val expectedSide = 4
        val game = startNewGame(
            side = expectedSide,
            players = MatchPlayers(Player(type = PieceType.BLACK), Player(type = PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )

        val availablePlays = game.getAvailablePlays()
        val piecesCount = game.gameState!!.board.totalBlackPieces + game.gameState!!.board.totalWhitePieces
        val expectedGhostPiecesAndPiecesCount = availablePlays.size + piecesCount

        setContent {
            val gameState = game.gameState!!

            reversiScope.DrawBoard(
                target = true,
                gameState = gameState,
                onCellClick = { fail("onCellClick should not be called during this test") },
                getAvailablePlays = { game.getAvailablePlays() }
            )
        }

        val boardTag = testTagBoard()
        var countPieces = 0

        //On Board make list of children (cells),
        // for each cell check if it has a piece or ghostPiece, if so increment countPieces
        onNodeWithTag(testTag = boardTag, useUnmergedTree = true)
            .onChildren().fetchSemanticsNodes().forEach { cellNode ->
                val pieceNode = cellNode.children
                if (pieceNode.isNotEmpty()) countPieces++
            }
        assert(countPieces == expectedGhostPiecesAndPiecesCount) {
            "Expected $expectedGhostPiecesAndPiecesCount pieces, but found $countPieces"
        }
    }
}