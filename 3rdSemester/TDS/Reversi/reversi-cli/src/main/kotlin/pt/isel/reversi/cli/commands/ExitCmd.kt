package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.Game
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import kotlin.system.exitProcess

/**
 * Command to exit the application.
 */
object ExitCmd : CommandImpl<Game>() {
    override val info = CommandInfo(
        title = "Exit",
        description = "Exits the application.",
        aliases = listOf("exit", "quit", "q"),
        usage = "exit",
        minArgs = 0,
        maxArgs = 0
    )

    override fun execute(vararg args: String, context: Game?): CommandResult<Game> {
        context?.saveGame()
        println("By byyyy")
        exitProcess(0)
    }
}