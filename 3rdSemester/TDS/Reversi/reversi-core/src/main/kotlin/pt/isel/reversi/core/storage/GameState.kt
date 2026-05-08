package pt.isel.reversi.core.storage

import pt.isel.reversi.core.Player
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType

/**
 * Represents the state of a Reversi game, including the last player who made a move and the current board configuration.
 * @property lastPlayer The type of the last player who made a move, or null if no moves have been made.
 * @property board The current state of the game board, or null if the board is not initialized.
 */
data class GameState(
    val players: List<Player>,
    val lastPlayer: PieceType,
    val board: Board
)

