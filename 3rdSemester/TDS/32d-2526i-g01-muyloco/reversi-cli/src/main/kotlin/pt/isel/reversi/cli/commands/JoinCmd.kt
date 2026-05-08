package pt.isel.reversi.cli.commands

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.cli.pieceTypes
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.game.loadAndEntryGame
import pt.isel.reversi.utils.TRACKER
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult

/**
 * Command to join an existing Reversi game from persistent storage.
 *
 * Loads a saved game by name and optionally specifies which player you control:
 * - `join gameName` — Joins the named game
 * - `join gameName #` — Joins as Black (#)
 * - `join gameName @` — Joins as White (@)
 *
 * If a game is already in progress, it is automatically saved before loading the new game.
 * If a piece type is specified, you will play as that color.
 * If no piece type is provided, the system will assign an available color.
 */
object JoinCmd : CommandImpl<Game>() {
    override val info: CommandInfo = CommandInfo(
        title = "Join",
        description = "Joins a game with the specified first player.",
        longDescription = "Joins a board with the specified first player. \n" + "If a name is provided, the board will be joined with that name.",
        aliases = listOf("j", "join"),
        usage = "join (<name>) [$pieceTypes]",
        minArgs = 1,
        maxArgs = 2
    )

    /**
     * Executes the join command.
     *
     * @param args The command arguments: <gameName> `[optionalPieceType]`
     * @param context The current game context (will be saved before joining new game).
     * @return A CommandResult with the loaded game or an error message.
     */
    override fun execute(vararg args: String, context: Game?): CommandResult<Game> {
        TRACKER.trackFunctionCall(
            customName = "JoinCmd.execute",
            details = "args=${args.joinToString()}",
            category = "CLI.Command"
        )
        if (context != null && context.currGameName != null) {
            runBlocking {
                context.saveEndGame()
            }
        }

        val service = context?.service ?: GameService()

        val name = args[0]
        val pTypeArg = args.getOrNull(1)?.firstOrNull()

        val pType = PieceType.fromSymbol(pTypeArg)

        if (pTypeArg != null && pType == null)
            return CommandResult.ERROR("Invalid piece type symbol provided: '$pTypeArg'.")

        val game = runBlocking {
            loadAndEntryGame(
                gameName = name,
                desiredType = pType,
                service = service,
            )
        }

        println(ShowCmd.executeWrapper(context = game).message)

        return CommandResult.SUCCESS("Joined game Successfully", game)
    }
}