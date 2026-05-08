package pt.isel.reversi.app.gamePageTest.gamePageViewsTests

import androidx.compose.ui.test.*
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.game.utils.cellView
import pt.isel.reversi.app.pages.game.utils.testTagCellView
import pt.isel.reversi.app.pages.game.utils.testTagPiece
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.fail

/**
 * Tests for the CellView composable function.
 * Have 3 possible states:
 * 1. No piece (cellValue and ghostPiece are null) -> no piece view and clickable
 * 2. Piece present (cellValue is not null) -> piece view and not clickable
 * 3. Ghost piece present (ghostPiece is not null and cellValue is null) -> piece view and clickable
 * If both cellValue and ghostPiece are not null, cellValue takes precedence.
 */
@OptIn(ExperimentalTestApi::class)
class CellViewTeste {
    val reversiScope = ReversiScope(AppState.empty(EmptyGameService()))

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `cellView with piece null expect no piece view`() = runComposeUiTest {
        val expectedCoordinates = Coordinate(1, 1)

        setContent {
            reversiScope.cellView(
                coordinate = expectedCoordinates,
                piece = null,
                onClick = { fail("onClick should not be called") }
            )
        }
        val cellViewTag = testTagCellView(coordinate = expectedCoordinates)
        onNodeWithTag(testTag = cellViewTag, useUnmergedTree = true)
            .onChildren()[0]
            .assertDoesNotExist()
    }

    @Test
    fun `cellView with piece != null and isGhostPiece false expect existing piece view and not clickable (Piece)`() =
        runComposeUiTest {
            val expectedCoordinates = Coordinate(1, 1)
            val expectedValue = PieceType.BLACK
            setContent {
                reversiScope.cellView(
                    coordinate = expectedCoordinates,
                    piece = Piece(coordinate = expectedCoordinates, expectedValue, isGhostPiece = false),
                    onClick = { fail("onClick should not be called") }
                )
            }

            val pieceTag = testTagPiece(coordinate = expectedCoordinates, type = expectedValue)
            onNodeWithTag(testTag = pieceTag, useUnmergedTree = true)
                .assertExists()

            val cellViewTag = testTagCellView(coordinate = expectedCoordinates)
            onNodeWithTag(testTag = cellViewTag, useUnmergedTree = true)
                .assertHasNoClickAction()
        }

    @Test
    fun `cellView with isGhostPiece true expect existing piece and clickable (ghostPiece)`() = runComposeUiTest {
        val expectedCoordinates = Coordinate(1, 1)
        val expectedValue = PieceType.BLACK
        setContent {
            reversiScope.cellView(
                coordinate = expectedCoordinates,
                piece = Piece(coordinate = expectedCoordinates, expectedValue, isGhostPiece = true),
                onClick = { fail("onClick should not be called") }
            )
        }
        val pieceTag = testTagPiece(coordinate = expectedCoordinates, type = expectedValue)
        onNodeWithTag(testTag = pieceTag, useUnmergedTree = true)
            .assertExists()

        val cellViewTag = testTagCellView(coordinate = expectedCoordinates)
        onNodeWithTag(testTag = cellViewTag, useUnmergedTree = true)
            .assertIsEnabled()
    }

    @Test
    fun `cellView with empty cell and freeze true expect not clickable`() = runComposeUiTest {
        val expectedCoordinates = Coordinate(1, 1)
        setContent {
            reversiScope.cellView(
                coordinate = expectedCoordinates,
                piece = null,
                freeze = true,
                onClick = { fail("onClick should not be called") }
            )
        }

        val cellViewTag = testTagCellView(coordinate = expectedCoordinates)
        onNodeWithTag(testTag = cellViewTag, useUnmergedTree = true)
            .assertHasNoClickAction()
    }

    @Test
    fun `cellView with ghostPiece and freeze true expect not clickable`() = runComposeUiTest {
        val expectedCoordinates = Coordinate(1, 1)
        val expectedValue = PieceType.BLACK
        setContent {
            reversiScope.cellView(
                coordinate = expectedCoordinates,
                piece = Piece(coordinate = expectedCoordinates, expectedValue, isGhostPiece = true),
                freeze = true,
                onClick = { fail("onClick should not be called") }
            )
        }

        val pieceTag = testTagPiece(coordinate = expectedCoordinates, type = expectedValue)
        onNodeWithTag(testTag = pieceTag, useUnmergedTree = true)
            .assertExists()

        val cellViewTag = testTagCellView(coordinate = expectedCoordinates)
        onNodeWithTag(testTag = cellViewTag, useUnmergedTree = true)
            .assertHasNoClickAction()
    }
}