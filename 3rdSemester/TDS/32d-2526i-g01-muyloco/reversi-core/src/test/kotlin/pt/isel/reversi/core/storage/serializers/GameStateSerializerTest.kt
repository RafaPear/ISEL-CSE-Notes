package pt.isel.reversi.core.storage.serializers

import org.junit.Test
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import kotlin.test.assertEquals
import kotlin.test.assertFails

class GameStateSerializerTest {

    val testingGameState = GameState(
        players = MatchPlayers(
            Player(PieceType.BLACK, "Alice"),
            Player(PieceType.WHITE, "Bob")
        ),
        lastPlayer = PieceType.BLACK,
        board = Board(8).startPieces(),
        winner = null
    )

    private fun buildStringFromGameState(state: GameState): String {
        return buildString {
            for (player in state.players) {
                append("${player.type.symbol},${player.name},${player.points};")
            }
            appendLine()
            appendLine(state.lastPlayer.symbol)
            if (state.winner != null) appendLine("${state.winner.type.symbol},${state.winner.points}")
            else appendLine()

            appendLine(state.board.side)
            for (piece in state.board) {
                appendLine("${piece.coordinate.row},${piece.coordinate.col},${piece.value.symbol}")
            }
        }.trimEnd()
    }

    @Test
    fun `serialize game state with two players`() {
        val serialized = GameStateSerializer().serialize(testingGameState)
        val expected = buildStringFromGameState(testingGameState)

        assertEquals(expected, serialized, "Serialization failed for game state with two players")
    }

    @Test
    fun `serialize game state with empty players`() {
        val gamState = testingGameState.copy(players = MatchPlayers())
        val expected = buildStringFromGameState(gamState)
        val serialized = GameStateSerializer().serialize(gamState)

        assertEquals(expected, serialized, "Serialization failed for game state with empty players")
    }

    @Test
    fun `serialize game state with one player`() {
        val gameState = testingGameState.copy(
            players = MatchPlayers(Player(PieceType.BLACK, "Alice"))
        )
        val expected = buildStringFromGameState(gameState)
        val serialized = GameStateSerializer().serialize(gameState)

        assertEquals(expected, serialized, "Serialization failed for game state with one player")
    }

    @Test
    fun `deserialize game state with two players`() {
        val data = buildStringFromGameState(testingGameState)
        val deserialized = GameStateSerializer().deserialize(data)
        val expected = testingGameState

        assertEquals(expected, deserialized, "Deserialization failed for game state with two players")
    }

    @Test
    fun `deserialize game state with empty players`() {
        val expectedGameState = testingGameState.copy(players = MatchPlayers())
        val data = buildStringFromGameState(expectedGameState)
        val deserialized = GameStateSerializer().deserialize(data)

        assertEquals(deserialized, expectedGameState, "Deserialization failed for empty players")
    }

    @Test
    fun `deserialize game state with one player`() {
        val expectedGameState = testingGameState.copy(
            players = MatchPlayers(testingGameState.players.last())
        )
        val data = buildStringFromGameState(expectedGameState)
        val deserialized = GameStateSerializer().deserialize(data)

        assertEquals(deserialized, expectedGameState, "Deserialization failed for one player")
    }

    @Test
    fun `deserialize with invalid last player throws exception`() {
        val badData = "B,10;W,20\nX\n8\n0,0,#"
        assertFails("Should fail with invalid last player") {
            GameStateSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid board side throws exception`() {
        val badData = "B,10;W,20\n@\nnotANumber\n0,0,#"
        assertFails("Should fail with invalid board side") {
            GameStateSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid piece type throws exception`() {
        val badData = "B,10;W,20\n@\n8\n0,0,Z"
        assertFails("Should fail with invalid piece type") {
            GameStateSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid player points throws exception`() {
        val badData = "B,notANumber;W,20\n@\n8\n0,0,#"
        assertFails("Should fail with invalid player points") {
            GameStateSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid piece row throws exception`() {
        val badData = "B,10;W,20\n@\n8\nnotARow,0,#"
        assertFails("Should fail with invalid piece row") {
            GameStateSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid piece column throws exception`() {
        val badData = "B,10;W,20\n@\n8\n0,notACol,#"
        assertFails("Should fail with invalid piece column") {
            GameStateSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with missing board data throws exception`() {
        val badData = "B,10;W,20\n@\n8\n"
        assertFails("Should fail with missing board data") {
            GameStateSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with malformed player data throws exception`() {
        val badData = "B,10\n@\n8\n0,0,#"
        assertFails("Should fail with malformed player data") {
            GameStateSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with empty string throws exception`() {
        val badData = ""
        assertFails("Should fail with empty string") {
            GameStateSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with negative player points throws exception`() {
        val badData = "B,-10;W,20\n@\n8\n0,0,#"
        assertFails("Should fail with negative player points") {
            GameStateSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with out of bounds piece coordinates throws exception`() {
        val badData = "B,10;W,20\n@\n8\n10,10,#"
        assertFails("Should fail with out of bounds coordinates") {
            GameStateSerializer().deserialize(badData)
        }
    }
}