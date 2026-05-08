package pt.isel.reversi.core.exceptions

/**
 * Thrown when a pieceType line in the persisted game file is malformed or contains
 * values that cannot be converted into a valid [pt.isel.reversi.core.board.PieceType].
 *
 * Typical causes:
 * - Unknown piece type symbol
 */
class InvalidPieceTypeInFileException(
    override val message: String = "The piece type in the file is invalid"
) : Exception()
