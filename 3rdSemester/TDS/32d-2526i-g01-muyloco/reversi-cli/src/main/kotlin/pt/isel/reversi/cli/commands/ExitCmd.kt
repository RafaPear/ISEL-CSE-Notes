package pt.isel.reversi.cli.commands

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.core.game.Game
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import kotlin.system.exitProcess

/**
 * Command to exit the Reversi CLI application.
 *
 * Before exiting, the current game is automatically saved if:
 * - The game has a name assigned, OR
 * - The user chooses to provide a name when prompted
 *
 * Usage: `exit` or `quit` or `q`
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

    /**
     * Prompts the user to provide a name for saving the current game.
     *
     * @return The provided game name, or null if the user provides an empty input.
     */
    fun askNameToSave(): String? {
        print("Enter a name to save the current game (or leave empty to not save): ")
        val input = readln()
        return input.ifBlank { null }
    }

    /**
     * Executes the exit command, saving the game if necessary and closing the application.
     *
     * @param args Command arguments (unused for this command).
     * @param context The current game context.
     * @return This method never returns; it calls exitProcess(0) to terminate the application.
     */
    override fun execute(vararg args: String, context: Game?): CommandResult<Game> {
        if (context != null) {
            val saveName = context.currGameName ?: askNameToSave()
            if (saveName != null) {
                println("Saving game as '$saveName' before exit...")
                runBlocking {
                    context.copy(currGameName = saveName).saveEndGame()
                    context.closeStorage()
                }
            }
        }
        println("By byyyy")
        exitProcess(0)
    }
}