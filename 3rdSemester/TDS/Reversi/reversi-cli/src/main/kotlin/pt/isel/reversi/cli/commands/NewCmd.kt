package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.Game
import pt.isel.reversi.core.Player
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.startNewGame
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.cmd.CommandResult.ERROR

/**
 * Command to create a new game with the specified first player.
 */
object NewCmd : CommandImpl<Game>() {

    private val pieceTypes = PieceType.entries.joinToString("|") { it.symbol.toString() }

    override val info: CommandInfo = CommandInfo(
        title = "New",
        description = "Creates a new game with the specified first player.",
        longDescription = "Creates a new board with the specified first player. \n" + "If a name is provided, the board will be saved with that name.",
        aliases = listOf("n", "new"),
        usage = "new ($pieceTypes) [<name>]",
        minArgs = 1,
        maxArgs = 2
    )

    override fun execute(vararg args: String, context: Game?): CommandResult<Game> {
        val playerType = PieceType.entries.find { it.symbol.toString() == args[0] } ?:
        return ERROR("First player must be one of: $pieceTypes")
        val player = Player(playerType)

        val name: String? = if (args.size == 2) args[1] else null

        val game: Game =
            if (name != null) {
                startNewGame(players = listOf(player), currGameName = name)
            } else {
                startNewGame(players = listOf(player, player.swap()))
            }

        return CommandResult.SUCCESS("Game created Successfully", game)
    }
}