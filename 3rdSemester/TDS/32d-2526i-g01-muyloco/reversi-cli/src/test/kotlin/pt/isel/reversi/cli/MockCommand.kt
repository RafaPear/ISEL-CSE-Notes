package pt.isel.reversi.cli

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.core.storage.StorageParams
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandInfo
import pt.rafap.ktflag.cmd.CommandResult

object MockCommand : CommandImpl<Game>() {
    override val info: CommandInfo = CommandInfo(
        title = "mock",
        description = "A mock command for testing purposes",
        longDescription = "This command serves as a placeholder for testing the CLI framework.",
        aliases = listOf("m", "mock"),
        usage = "mock",
        minArgs = 0,
        maxArgs = 0
    )

    override fun execute(
        vararg args: String,
        context: Game?
    ): CommandResult<Game> {
        val gameService = GameService(
            storage = GameStorageType.FILE_STORAGE,
            params = StorageParams.FileStorageParams(folder = "data/saves")
        )
        val service = context?.service ?: gameService
        val newContext = runBlocking {
            startNewGame(
                side = 8,
                players = MatchPlayers(Player(PieceType.BLACK)),
                firstTurn = PieceType.BLACK,
                service = service
            )
        }
        return CommandResult.SUCCESS(
            result = newContext,
            message = "Mock command executed. New context: $newContext"
        )
    }

}