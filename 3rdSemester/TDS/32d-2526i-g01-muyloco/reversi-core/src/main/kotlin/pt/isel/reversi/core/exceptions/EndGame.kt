package pt.isel.reversi.core.exceptions

/**
 * Thrown when the game has ended, either due to a winner being determined
 * or both players passing consecutively.
 * @property message Details about how the game ended.
 * @property type The severity level of this exception.
 */
class EndGame(
    message: String = "The game has ended",
    type: ErrorType
) : ReversiException(message, type)