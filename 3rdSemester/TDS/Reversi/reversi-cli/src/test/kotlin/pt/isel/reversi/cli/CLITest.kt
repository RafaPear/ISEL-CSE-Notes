package pt.isel.reversi.cli

import pt.isel.reversi.core.Game
import pt.isel.reversi.core.Player
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.startNewGame
import kotlin.test.Test

// Using the functions parseInput and parseStringToResult
// from CLI.kt specifically made for testing purposes
class CLITest {
    @Test
    fun `Test MockCommand execution with null game context`() {
        // send data to standard input and verify output
        cleanup {
            val cli = CLI(arrayOf(MockCommand)) // game is null
            val result = cli.parseInput("mock", null) // should execute MockCommand and game remains null
            assert(result == startNewGame(players = listOf(Player(PieceType.BLACK)))) {
                "Expected game context to be null, but got: $result"
            }
        }
    }

    @Test
    fun `Test MockCommand execution with valid game context`() {
        cleanup {
            val cli = CLI(arrayOf(MockCommand))
            val initialGame = Game()
            val expectedGame = startNewGame(players = listOf(Player(PieceType.BLACK)))
            val result = cli.parseInput("mock", initialGame) // should execute MockCommand and return new game context
            assert(result != null && result != initialGame && result == expectedGame) {
                "Expected a new game context, but got: $result"
            }
        }
    }
}