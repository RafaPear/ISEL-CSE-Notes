package pt.isel.reversi.core.exceptions

/**
 * Thrown when a piece line in the persisted game file is malformed or contains
 * values that cannot be converted into a valid [pt.isel.reversi.core.board.Piece].
 *
 * Typical causes:
 * - Wrong number of tokens in the line
 * - Non-integer row/column values
 * - Unknown piece symbol
 *
 */
class InvalidPieceInFileException(
    override val message: String = "The piece in the file is invalid"
) : Exception()
