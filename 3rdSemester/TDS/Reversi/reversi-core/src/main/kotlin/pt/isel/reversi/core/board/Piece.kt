package pt.isel.reversi.core.board

/**
 * Represents a piece on the board.
 */
data class Piece(
    val coordinate: Coordinate,
    val value: PieceType
) {
    fun swap(): Piece {
        return Piece(coordinate, value.swap())
    }
}