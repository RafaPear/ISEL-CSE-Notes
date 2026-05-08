package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.game.Game
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult

/**
 * Command to toggle target mode for visual feedback during gameplay.
 *
 * Target mode enables highlighting of available moves or other targeting assistance.
 * Accepts true/false values in various formats:
 * - True: "true", "yes", "1", "on"
 * - False: "false", "no", "0", "off"
 *
 * Usage: `target true` or `target false` or just `target` to check current state
 */
object TargetCmd : CommandImpl<Game>() {
    override val info: CommandInfo =
        CommandInfo(
            title = "Target Command",
            description = "Toggles target mode for visual feedback.",
            longDescription = "Enables or disables target mode to highlight available moves during gameplay.",
            aliases = listOf("target", "tg"),
            usage = "target [true|false|yes|no|1|0|on|off]",
            minArgs = 0,
            maxArgs = 1
        )

    private val trueAlias = listOf("true", "yes", "1", "on")
    private val falseAlias = listOf("false", "no", "0", "off")

    /**
     * Executes the target command.
     *
     * @param args Optional argument to enable/disable target mode.
     * @param context The current game context.
     * @return A CommandResult with the updated game state or an error message.
     */
    override fun execute(
        vararg args: String,
        context: Game?
    ): CommandResult<Game> {
        if (context == null)
            return CommandResult.ERROR("No game context available.")

        val arg = args.getOrNull(0)?.lowercase()
        val newGame = when {
            trueAlias.contains(arg) -> context.setTargetMode(true)

            falseAlias.contains(arg) -> context.setTargetMode(false)

            else -> context
        }

        return CommandResult.SUCCESS("Targeting is currently ${newGame.target}.", newGame)
    }
}