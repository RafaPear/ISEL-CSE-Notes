package pt.isel.reversi.core.exceptions

/**
 * Thrown when a player line in the persisted game file is malformed or contains
 * values that cannot be converted into a valid [pt.isel.reversi.core.Player].
 *
 * Typical causes:
 * - Unknown player identifier
 * - Points is not an integer
 */
class InvalidPlayerInFileException(
    override val message: String = "The player in the file is invalid"
) : Exception()
