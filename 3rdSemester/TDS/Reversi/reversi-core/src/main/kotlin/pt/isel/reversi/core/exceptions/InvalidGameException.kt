package pt.isel.reversi.core.exceptions

/**
 * Thrown when an operation is invoked on an invalid or uninitialized game.
 * Examples: calling `play` or `pass` when the game has not been started.
 */
class InvalidGameException(message: String) : Exception(message)