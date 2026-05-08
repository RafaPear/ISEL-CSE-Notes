package pt.isel.reversi.core.exceptions

/**
 * Thrown when an operation on a file or storage entity fails.
 * This can occur when loading, saving, or deleting game states from persistent storage.
 * @property message Details about the file operation failure.
 * @property type The severity level of this exception.
 */
class InvalidFile(
    message: String = "The provided file is invalid or corrupted",
    type: ErrorType
) : ReversiException(message, type)
