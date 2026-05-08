package pt.isel.reversi.core.exceptions

/**
 * Thrown when the board representation in the persisted game file is malformed or contains
 * values that cannot be converted into a valid [pt.isel.reversi.core.board.Board].
 *
 * Typical causes:
 * - Incorrect board dimensions
 * - Invalid characters representing pieces
 * - Piece serialization errors
 * - Missing or extra data
 */
class InvalidBoardInFileException(
    override val message: String = "The board in the file is invalid"
) : Exception()
