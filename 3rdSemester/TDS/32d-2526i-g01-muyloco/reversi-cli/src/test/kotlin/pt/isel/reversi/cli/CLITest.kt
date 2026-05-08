package pt.isel.reversi.cli

import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.core.storage.StorageParams
import kotlin.test.Test

// Using the functions parseInput and parseStringToResult
// from CLI.kt specifically made for testing purposes
class CLITest {
    val gameService = GameService(
        storage = GameStorageType.FILE_STORAGE,
        params = StorageParams.FileStorageParams(folder = "data/saves")
    )

    @Test
    fun `Test MockCommand execution with null game context`() {
        // send data to standard input and verify output
        cleanup {
            val cli = CLI(arrayOf(MockCommand)) // game is null
            val result = cli.parseInput("mock", null)// should execute MockCommand and game remains null
            val newGame = startNewGame(
                side = 8,
                players = MatchPlayers(Player(PieceType.BLACK)),
                firstTurn = PieceType.BLACK,
                service = gameService
            )
            assert(
                result != null && result.gameState == newGame.gameState
            ) {
                "Expected game context to be null, but got: $result"
            }
        }
    }

    @Test
    fun `Test MockCommand execution with valid game context`() {
        cleanup {
            val cli = CLI(arrayOf(MockCommand))
            val initialGame = Game(service = gameService)
            val expectedGame = startNewGame(
                side = 8,
                players = MatchPlayers(Player(PieceType.BLACK)),
                firstTurn = PieceType.BLACK,
                service = gameService
            )

            val result = cli.parseInput("mock", initialGame)// should execute MockCommand and return new game context
            assert(
                result != null
                        && result.gameState != initialGame.gameState
                        && result.gameState == expectedGame.gameState
            ) {
                "Expected a new game context, but got: $result"
            }
        }
    }
}