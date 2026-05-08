package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.Game
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult

/**
 * Refreshes and displays the current game state.
 * It expects a `Game` context and returns an error when the game or board is not initialized.
 */
object RefreshCmd : CommandImpl<Game>() {
    override val info: CommandInfo = CommandInfo(
        title = "Refresh",
        description = "Refreshes and displays the current game state.",
        longDescription = "Refreshes the game state and displays the current board. \n" +
                          "Requires an initialized game context to function properly.",
        aliases = listOf("refresh"),
        usage = "refresh",
        minArgs = 0,
        maxArgs = 0
    )

    override fun execute(
        vararg args: String,
        context: Game?
    ): CommandResult<Game> {
        if (context == null)
            return CommandResult.ERROR("Game is not defined. Cannot show game state.")

        val refreshed = context.refresh()

        ShowCmd.executeWrapper(context = refreshed)

        return CommandResult.SUCCESS(
            "Refreshed Game State Successfully",
            refreshed
        )
    }

}