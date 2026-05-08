package pt.isel.reversi.app.exceptions

import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.ReversiException

/**
 * Exception thrown when attempting to join a multiplayer game that already has all players.
 *
 * @param message Descriptive error message.
 * @param type The error severity type (default: INFO).
 */
class GameIsFull(
    message: String = "The game is full",
    type: ErrorType = ErrorType.INFO,
) : ReversiException(message, type)