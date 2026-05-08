package pt.isel.reversi.app.pages.game.utils

import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.gameState.Player

/**
 * Creates a test tag for the game board.
 *
 * @return The test tag string for the board.
 */
fun testTagBoard() = "game_page_board"

/**
 * Creates a test tag for a specific board cell.
 *
 * @param coordinate The board coordinate of the cell.
 * @return The test tag string for the cell.
 */
fun testTagCellView(coordinate: Coordinate) =
    "cell_${coordinate.row},${coordinate.col}"

/**
 * Creates a test tag for a piece on the board.
 *
 * @param coordinate The board coordinate of the piece.
 * @param type The piece type (BLACK, WHITE, or null).
 * @return The test tag string for the piece.
 */
fun testTagPiece(coordinate: Coordinate, type: PieceType?): String {
    val value = when (type) {
        PieceType.BLACK -> "BLACK"
        PieceType.WHITE -> "WHITE"
        null -> ""
    }
    return "Piece_${testTagCellView(coordinate)}_${value}"
}

/**
 * Creates a test tag for the game page container.
 *
 * @return The test tag string for the game page.
 */
fun testTagGamePage() = "game_page"

/**
 * Creates a test tag for a player's score display.
 *
 * @param player The player whose score is being tagged.
 * @return The test tag string for the player score.
 */
fun testTagPlayerScore(player: Player) =
    "player_score_${if (player.type == PieceType.BLACK) "BLACK" else "WHITE"}_${player.points}"

/**
 * Creates a test tag for the target mode toggle button.
 *
 * @param target Whether target mode is currently enabled.
 * @return The test tag string for the target button.
 */
fun testTagTargetButtons(target: Boolean) = "game_page_target_buttons_${if (target) "ON" else "OFF"}"