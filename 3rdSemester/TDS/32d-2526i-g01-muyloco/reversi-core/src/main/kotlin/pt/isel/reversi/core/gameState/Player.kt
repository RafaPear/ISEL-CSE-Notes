package pt.isel.reversi.core.gameState

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType

/**
 * Represents a player in the game.
 *
 * @property type The type of piece the player uses (BLACK or WHITE).
 * @property points The current number of pieces the player has on the board.
 * @property name The name of the player (defaults to piece type name if null).
 */
data class Player(
    val type: PieceType,
    val name: String = type.name,
    val points: Int = 0,
) {

    init {
        require(!(name.contains(',') || name.contains(';') || name.contains(':'))) {
            "Player name cannot contain the following characters: , ; :"
        }
    }

    /**
     * Updates the player's points based on the current board state.
     *
     * @param board The current board state.
     * @return A new Player instance with updated points matching the board's piece count.
     */
    fun refresh(board: Board): Player {
        return Player(
            type = type,
            points = when (type) {
                PieceType.BLACK -> board.totalBlackPieces
                PieceType.WHITE -> board.totalWhitePieces
            },
            name = name
        )
    }

    /**
     * Returns a new Player instance with the opposite piece type.
     *
     * @return A new Player instance with swapped piece type and points reset to 0.
     */
    fun swap(): Player {
        val swappedType = when (type) {
            PieceType.BLACK -> PieceType.WHITE
            PieceType.WHITE -> PieceType.BLACK
        }
        return Player(type = swappedType, name = name)
    }
}