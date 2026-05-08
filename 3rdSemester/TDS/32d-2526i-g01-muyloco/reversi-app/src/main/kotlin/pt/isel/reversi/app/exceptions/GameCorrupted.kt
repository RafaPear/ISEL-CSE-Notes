package pt.isel.reversi.app.exceptions

import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException

/**
 * Exception thrown when a saved game file is corrupted or cannot be deserialized.
 *
 * @param message Descriptive error message.
 * @param type The error severity type (default: ERROR).
 */
class GameCorrupted(
    message: String = "The game data is corrupted and cannot be loaded.",
    type: ErrorType = ErrorType.ERROR
) : ReversiException(message, type)