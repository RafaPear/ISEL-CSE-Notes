package pt.isel.reversi.core.exceptions

/**
 * Thrown when an attempted play is invalid according to Reversi rules.
 * Reasons include: position already occupied, play does not capture any opponent pieces,
 * or the coordinate is out of bounds.
 */
class InvalidPlayException(message: String) : Exception(message)