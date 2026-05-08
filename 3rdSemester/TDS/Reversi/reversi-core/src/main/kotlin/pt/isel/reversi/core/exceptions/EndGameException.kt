package pt.isel.reversi.core.exceptions

class EndGameException(
    override val message: String = "The game has ended"
): Exception()