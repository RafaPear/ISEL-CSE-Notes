package pt.isel.reversi.core.exceptions

/**
 * Thrown when attempting to create a game with a name that already exists in storage.
 * @property message Details about the naming conflict.
 * @property type The severity level of this exception (defaults to WARNING).
 */
class InvalidNameAlreadyExists(
    message: String = "The provided name already exists",
    type: ErrorType = ErrorType.WARNING
) : ReversiException(message, type)
