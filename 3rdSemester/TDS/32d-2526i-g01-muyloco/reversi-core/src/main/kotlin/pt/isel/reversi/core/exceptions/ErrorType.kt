package pt.isel.reversi.core.exceptions

/**
 * Enum representing different types of error levels for exceptions in the Reversi game.
 */
enum class ErrorType(val level: String) {
    INFO("INFO"),
    WARNING("WARNING"),
    ERROR("ERROR"),
    CRITICAL("CRITICAL");

    companion object {
        fun Exception.toReversiException(type: ErrorType?): ReversiException {
            return object : ReversiException(
                message = message ?: "An unknown error occurred.",
                type = type ?: CRITICAL,
            ) {}
        }
    }
}