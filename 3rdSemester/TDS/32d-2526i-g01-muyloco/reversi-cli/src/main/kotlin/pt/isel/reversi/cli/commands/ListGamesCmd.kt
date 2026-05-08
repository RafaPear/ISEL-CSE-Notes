package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.game.Game
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.cmd.CommandResultType

/**
 * Debug command to list all available saved games.
 *
 * This is a development-only command that displays all game files in the saves folder.
 * Useful for debugging and manual testing to see which games are available to load.
 *
 * Usage: `listgames` or `lg` (available only with --debug flag)
 */
object ListGamesCmd : CommandImpl<Game>() {
    override val info: CommandInfo =
        CommandInfo(
            title = "List Games",
            description = "[DEBUG] Lists all available games.",
            longDescription = "[DEBUG] Displays a list of all available games that can be joined or viewed.",
            aliases = listOf("listgames", "lg"),
            usage = "listgames",
            minArgs = 0,
            maxArgs = 0
        )

    /**
     * Executes the list games command.
     *
     * @param args Command arguments (unused for this command).
     * @param context The current game context (used to access configuration).
     * @return A CommandResult with the list of available games or an error message.
     */
    override fun execute(
        vararg args: String,
        context: Game?
    ): CommandResult<Game> {
        if (context == null) {
            return CommandResultType.ERROR("Game context is not available. No config is loaded.", context)
        }
        val folder = java.io.File(context.config.savesPath)
        if (!folder.exists() || !folder.isDirectory) {
            return CommandResult.SUCCESS("No saved games found.", context)
        }

        val files = folder.listFiles()
        val gameNames = files?.map { it.nameWithoutExtension } ?: emptyList()
        val message = if (gameNames.isEmpty()) {
            "No saved games found."
        } else {
            "Available games:\n" + gameNames.joinToString(separator = "\n") { "- $it" }
        }
        return CommandResult.SUCCESS(message, context)
    }
}