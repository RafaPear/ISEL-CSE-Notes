package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.Game
import pt.isel.reversi.core.loadGame
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult

/**
 * Command to join a game.
 */
object JoinCmd : CommandImpl<Game>() {
    override val info: CommandInfo = CommandInfo(
        title = "Join",
        description = "Joins a game with the specified first player.",
        longDescription = "Joins a board with the specified first player. \n" + "If a name is provided, the board will be joined with that name.",
        aliases = listOf("j", "join"),
        usage = "join (<name>)",
        minArgs = 1,
        maxArgs = 1
    )

    override fun execute(vararg args: String, context: Game?): CommandResult<Game> {
        val name: String = args[0]
        return CommandResult.SUCCESS("Joined game Successfully", loadGame(name))
    }
}