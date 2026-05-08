package pt.isel.reversi.cli.commands

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.utils.TRACKER
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.cmd.CommandResult.ERROR

/**
 * Command to execute a move at the specified board coordinates.
 *
 * Accepts coordinates in multiple formats:
 * - Separate arguments: `play 3 4`
 * - Combined with letter column: `play 3A` or `play 3a`
 * - Combined digits: `play 34` (row=3, col=4)
 *
 * Coordinates are 1-based (row 1-8, column 1-8 for standard 8x8 board).
 */
object PlayCmd : CommandImpl<Game>() {
    override val info: CommandInfo = CommandInfo(
        title = "Play",
        description = "Plays a move at the specified row and column.",
        longDescription = "Plays a move at the specified row and column in the current game. The coordinates can be provided as separate arguments or combined. For example, both 'play 3 4' and 'play 3A' are valid inputs.",
        aliases = listOf("p", "play"),
        usage = "play (row) (col) or play (rowcol)",
        minArgs = 1,
        maxArgs = 2
    )

    /**
     * Parses coordinate arguments in various formats.
     *
     * Supports the following formats:
     * - Two separate arguments: row and column (as strings/numbers)
     * - Combined format: digit(s) followed by a letter or digit
     *
     * @param args The coordinate arguments to parse.
     * @return The parsed Coordinate, or null if parsing fails.
     */
    fun parseCoordinateArgs(args: List<String>): Coordinate? {

        val arg = args.joinToString("").trim()

        when {
            arg.length >= 2 -> {
                val colChar = arg.getOrNull(1) ?: return null
                val row = arg.getOrNull(0).toString().toIntOrNull() ?: return null

                if (colChar.isDigit()) {
                    val col = colChar.toString().toIntOrNull() ?: return null
                    return Coordinate(row, col)
                } else if (colChar.isLetter()) return Coordinate(row, colChar)
                else return null
            }

            else -> return null
        }
    }

    /**
     * Executes a move at the parsed coordinates.
     *
     * @param args The coordinate arguments (see parseCoordinateArgs for formats).
     * @param context The current game context.
     * @return A CommandResult with the updated game state or an error message.
     */
    override fun execute(vararg args: String, context: Game?): CommandResult<Game> {
        TRACKER.trackFunctionCall(
            customName = "PlayCmd.execute",
            details = "args=${args.joinToString()}",
            category = "CLI.Command"
        )
        if (context == null) {
            return ERROR("Game is not defined. Cannot play.")
        }

        try {
            val coordinate = parseCoordinateArgs(args.toList()) ?: return ERROR("Invalid coordinates provided.")

            val game: Game = runBlocking { context.play(coordinate) }

            println(ShowCmd.executeWrapper(context = game).message)

            return CommandResult.SUCCESS(
                "Move played at (${coordinate.row}, ${coordinate.col})",
                game
            )
        } catch (e: Exception) {
            return ERROR("Error playing move: ${e.message}")
        }
    }
}