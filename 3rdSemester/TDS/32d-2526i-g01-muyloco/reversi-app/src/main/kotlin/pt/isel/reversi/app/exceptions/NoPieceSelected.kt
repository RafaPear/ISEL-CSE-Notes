package pt.isel.reversi.app.exceptions

import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException

/**
 * Exception thrown when the user attempts to start a game without selecting a piece color.
 *
 * @param message Descriptive error message.
 * @param type The error severity type (default: INFO).
 */
class NoPieceSelected(
    message: String = "No piece selected",
    type: ErrorType = ErrorType.INFO,
) : ReversiException(message, type)