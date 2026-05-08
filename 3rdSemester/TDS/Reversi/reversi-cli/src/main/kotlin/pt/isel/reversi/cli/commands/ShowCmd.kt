package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.Game
import pt.isel.reversi.core.board.Coordinate
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult

/**
 * ShowCmd prints the current game state: board layout and player scores.
 * It expects a `Game` context and returns an error when the game or board is not initialized.
 */
object ShowCmd : CommandImpl<Game>() {
    override val info: CommandInfo = CommandInfo(
        title = "Show",
        description = "Shows the current game state.",
        longDescription = "Displays the current board, player scores, and other relevant game information.",
        aliases = listOf("s", "show"),
        usage = "show",
        minArgs = 0,
        maxArgs = 0
    )

    override fun execute(
        vararg args: String,
        context: Game?
    ): CommandResult<Game> {
        if (context == null)
            return CommandResult.ERROR("Game is not defined. Cannot show game state.")

        val gs = context.gameState ?: return CommandResult.ERROR("Game is not initialized.")


        val board = gs.board
        val players = gs.players


        val builder = StringBuilder()
        builder.appendLine("Current Game State:")
        for (row in 0..board.side) {
            for (col in 0..board.side) {

                when {
                    row == 0 && col == 0 -> builder.append("  ")
                    row == 0             -> builder.append("$col ")
                    col == 0             -> builder.append("$row ")
                    else                 -> {
                        val cords = Coordinate(row, col)
                        val piece = board[cords]
                        val symbol = piece?.symbol ?: '.'

                        builder.append("$symbol ")
                    }
                }

            }
            builder.appendLine()
        }
        builder.appendLine("Player Scores:")
        players.forEach { player ->
            builder.appendLine("Player ${player.type.symbol}: ${player.points} points")
        }

        println(builder.toString())

        return CommandResult.SUCCESS(builder.toString(), context)
    }

}