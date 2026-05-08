package pt.isel.reversi.app.exceptions

import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException

/**
 * Exception thrown when an operation requires an active game that hasn't been started yet.
 *
 * @param message Descriptive error message.
 * @param type The error severity type.
 */
class GameNotStartedYet(
    message: String = "The game has not started yet",
    type: ErrorType = ErrorType.WARNING,
) : ReversiException(message, type)