package pt.isel.reversi.core.exceptions

/**
 * Base exception class for Reversi game-related errors.
 *
 * @property type The type of error represented by this exception.
 * @param message The detail message for the exception.
 */
abstract class ReversiException(
    message: String,
    val type: ErrorType
) : Exception(message)