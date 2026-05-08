package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.Game
import pt.isel.reversi.core.board.Coordinate
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.cmd.CommandResult.ERROR

/**
 * Command to do a play in a game.
 */
object PlayCmd : CommandImpl<Game>() {
    override val info: CommandInfo = CommandInfo(
        title = "Play",
        description = "Plays a move at the specified row and column.",
        longDescription = "Plays a move at the specified row and column in the current game.",
        aliases = listOf("p", "play"),
        usage = "play (row) (col)",
        minArgs = 2,
        maxArgs = 2
    )

    override fun execute(vararg args: String, context: Game?): CommandResult<Game> {
        if (context == null) {
            return ERROR("Game is not defined. Cannot join a game.")
        }

        try {
            val row: Int = args[0].toIntOrNull() ?: return ERROR("Row must be an integer.")

            val col: Int = args[1].toIntOrNull() ?: return ERROR("Column must be an integer.")

            val game: Game = context.play(Coordinate(row, col))
            return CommandResult.SUCCESS(
                "Move played at ($row, $col)",
                game
            )
        } catch (e: Exception) {
            return ERROR("Error playing move: ${e.message}")
        }
    }
}