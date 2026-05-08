package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.stringifyBoard
import pt.isel.reversi.core.gameState.Player
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.style.Colors
import pt.rafap.ktflag.style.Colors.colorText

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
        vararg args: String, context: Game?
    ): CommandResult<Game> {
        if (context == null) return CommandResult.ERROR("Game is not defined. Cannot show game state.")

        val gs = context.gameState ?: return CommandResult.ERROR("Game is not initialized.")

        val board = gs.board
        val players = listOf(
            Player(PieceType.WHITE, points = board.totalWhitePieces),
            Player(PieceType.BLACK, points = board.totalBlackPieces)
        )
        val lastPLayer = gs.lastPlayer
        val name = context.currGameName
        val myPieceType = gs.players.firstOrNull()?.type

        val builder = StringBuilder()
        if (name != null) {
            builder.appendLine("------------------------------")
            builder.appendLine("Current Game Name: $name")
            builder.appendLine("------------------------------")
            builder.appendLine()
        }
        builder.appendLine(context.stringifyBoard())
        builder.appendLine("Player Scores:")
        players.forEach { player ->
            if ((player.type == myPieceType && name != null) || (lastPLayer.swap() == player.type && name == null)) {
                builder.appendLine(
                    colorText(
                        " - Player ${player.type.symbol}: ${player.points} points <- (You)",
                        Colors.GREEN,
                        Colors.BOLD
                    )
                )
            } else builder.appendLine(" - Player ${player.type.symbol}: ${player.points} points")
        }
        builder.appendLine("Player turn: ${lastPLayer.swap().symbol}")

        return CommandResult.SUCCESS(builder.toString(), context)
    }

}