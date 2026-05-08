package pt.isel.reversi.core.exceptions

/**
 * Thrown when a game state in the persisted game file is malformed or contains
 * values that cannot be converted into a valid game state.
 *
 * Typical causes:
 * - Missing or extra data
 * - Inconsistent game state information
 * - Invalid format or corrupted data
 */
class InvalidGameStateInFile(
    override val message: String = "The game state in the file is invalid",
    type: ErrorType
) : ReversiException(message, type)
