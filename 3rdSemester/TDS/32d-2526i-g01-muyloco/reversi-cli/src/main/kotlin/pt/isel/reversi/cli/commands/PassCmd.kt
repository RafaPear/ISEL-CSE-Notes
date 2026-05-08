package pt.isel.reversi.cli.commands

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.utils.TRACKER
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.cmd.CommandResult.ERROR

/**
 * Command to pass the current player's turn.
 *
 * Skips the current player's turn when they have no legal moves available.
 * If both players pass consecutively, the game ends and a winner is determined.
 *
 * Usage: `pass`
 */
object PassCmd : CommandImpl<Game>() {
    override val info: CommandInfo = CommandInfo(
        title = "Pass",
        description = "Passes the current player's turn.",
        longDescription = "Passes the current player's turn in the current game.",
        aliases = listOf("pass"),
        usage = "pass",
        minArgs = 0,
        maxArgs = 0
    )

    /**
     * Executes the pass command.
     *
     * @param args Command arguments (unused for this command).
     * @param context The current game context.
     * @return A CommandResult indicating success or failure, with the updated game state.
     */
    override fun execute(vararg args: String, context: Game?): CommandResult<Game> {
        TRACKER.trackFunctionCall(customName = "PassCmd.execute", category = this)
        if (context == null) {
            return ERROR("Game is not defined. Cannot join a game.")
        }

        try {
            val game = runBlocking { context.pass() }

            return if (game.gameState?.winner == null) {
                CommandResult.SUCCESS(
                    "Turn passed successfully",
                    game
                )
            } else {
                CommandResult.SUCCESS(
                    "Game over! The winner is ${game.gameState?.winner}",
                    game
                )
            }
        } catch (e: Exception) {
            return ERROR(e.message ?: "Error passing turn")
        }
    }
}