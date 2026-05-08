package pt.isel.reversi.app.exceptions

import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException

/**
 * Exception thrown when a required text input field is empty.
 *
 * @param message Descriptive error message.
 * @param type The error severity type (default: INFO).
 */
class CouldNotLoadAsset(
    message: String = "Could not load asset.",
    type: ErrorType = ErrorType.WARNING,
) : ReversiException(message, type)