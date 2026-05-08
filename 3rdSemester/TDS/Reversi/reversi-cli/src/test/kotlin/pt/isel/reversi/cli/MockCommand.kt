package pt.isel.reversi.cli

import pt.isel.reversi.core.Game
import pt.isel.reversi.core.Player
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.startNewGame
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult

object MockCommand: CommandImpl<Game>() {
    override val info: CommandInfo = CommandInfo(
        title = "mock",
        description = "A mock command for testing purposes",
        longDescription = "This command serves as a placeholder for testing the CLI framework.",
        aliases = listOf("m", "mock"),
        usage = "mock",
        minArgs = 0,
        maxArgs = 0
    )

    override fun execute(
        vararg args: String,
        context: Game?
    ): CommandResult<Game> {
        val newContext = startNewGame(players = listOf(Player(PieceType.BLACK)))
        return CommandResult.SUCCESS(
            result = newContext,
            message = "Mock command executed. New context: $newContext"
        )
    }

}