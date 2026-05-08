package pt.isel.reversi.cli.commands

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.core.game.Game
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult

/**
 * Command to refresh and display the current game state.
 *
 * Re-reads the underlying game state from storage (useful in multi-process scenarios
 * where another instance may have updated the game state) and displays the updated board.
 *
 * Usage: `refresh` or `r`
 */
object RefreshCmd : CommandImpl<Game>() {
    override val info: CommandInfo = CommandInfo(
        title = "Refresh",
        description = "Refreshes and displays the current game state.",
        longDescription = "Refreshes the game state and displays the current board. \n" +
                "Requires an initialized game context to function properly.",
        aliases = listOf("r", "refresh"),
        usage = "refresh",
        minArgs = 0,
        maxArgs = 0
    )

    /**
     * Executes the refresh command.
     *
     * @param args Command arguments (unused for this command).
     * @param context The current game context.
     * @return A CommandResult with the refreshed game state or an error message.
     */
    override fun execute(
        vararg args: String,
        context: Game?
    ): CommandResult<Game> {
        if (context == null)
            return CommandResult.ERROR("Game is not defined. Cannot show game state.")

        val refreshed = runBlocking { context.refresh() }

        val show = ShowCmd.executeWrapper(context = refreshed).message

        return CommandResult.SUCCESS(
            "Refreshed Game State Successfully\n$show",
            refreshed
        )
    }

}