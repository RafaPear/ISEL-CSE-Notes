package pt.isel.reversi.cli.commands

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.cli.pieceTypes
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.utils.TRACKER
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult
import pt.rafap.ktflag.cmd.CommandResult.ERROR

/**
 * Command to create a new Reversi game.
 *
 * Creates a new game with the specified starting player:
 * - `new # [name]` — Creates a game where Black (#) goes first
 * - `new @ [name]` — Creates a game where White (@) goes first
 *
 * If a game name is provided, the game is saved to persistent storage.
 * If no name is given, the game exists only in memory (local game).
 * The name must be unique; attempting to create a game with an existing name will fail.
 *
 * If a game is already in progress, it is automatically saved before creating the new game.
 */
object NewCmd : CommandImpl<Game>() {

    override val info: CommandInfo = CommandInfo(
        title = "New",
        description = "Creates a new game with the specified first player.",
        longDescription = "Creates a new board with the specified first player. \n" + "If a name is provided, the board will be saved with that name.",
        aliases = listOf("n", "new"),
        usage = "new ($pieceTypes) [<name>]",
        minArgs = 1,
        maxArgs = 2
    )

    /**
     * Executes the new game creation command.
     *
     * @param args The command arguments: `[playerSymbol] [optionalGameName]`
     * @param context The current game context (will be saved before creating new game).
     * @return A CommandResult with the newly created game or an error message.
     */
    override fun execute(vararg args: String, context: Game?): CommandResult<Game> {
        TRACKER.trackFunctionCall(
            customName = "NewCmd.execute",
            details = "args=${args.joinToString()}",
            category = "CLI.Command"
        )
        if (context != null && context.currGameName != null) {
            runBlocking { context.saveEndGame() }
        }

        val service = context?.service ?: GameService()

        val playerType = PieceType.entries.find { it.symbol.toString() == args[0] }
            ?: return ERROR("First player must be one of: $pieceTypes")
        val player = Player(playerType)

        val name: String? = if (args.size == 2) args[1] else null

        val game: Game = runBlocking {
            if (name != null) {
                startNewGame(
                    side = 8,
                    players = MatchPlayers(player),
                    currGameName = name,
                    firstTurn = playerType,
                    service = service
                )
            } else {
                startNewGame(
                    side = 8,
                    players = MatchPlayers(
                        player,
                        player.swap()
                    ),
                    firstTurn = playerType,
                    service = service
                )
            }
        }

        println(ShowCmd.executeWrapper(context = game).message)

        return CommandResult.SUCCESS(
            "Game created Successfully",
            game
        )
    }
}