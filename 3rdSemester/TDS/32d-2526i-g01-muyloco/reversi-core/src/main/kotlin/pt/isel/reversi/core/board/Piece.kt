package pt.isel.reversi.core.board

/**
 * Represents a piece on the board with its location and type.
 *
 * @property coordinate The location of the piece on the board.
 * @property value The type of piece (BLACK or WHITE).
 * @property isGhostPiece Indicates if this is a ghost piece (typically used for UI highlighting).
 */
data class Piece(
    val coordinate: Coordinate,
    val value: PieceType,
    val isGhostPiece: Boolean = false
) {
    /**
     * Returns a new Piece with the opposite color at the same coordinate.
     *
     * @return A new Piece instance with swapped piece type.
     */
    fun swap(): Piece {
        return Piece(coordinate, value.swap())
    }
}