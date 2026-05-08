package pt.isel.reversi.core.storage.serializers

import org.junit.Test
import pt.isel.reversi.core.Player
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.storage.GameState
import kotlin.test.assertFails

class GameStateSerializerTest {
    val testUnit = SerializerTestUnit(GameStateSerializer()) {
        val list = mutableListOf<GameState>()
        val sides = listOf(4, 6, 8, 10, 12, 14, 16)
        for (side in sides) {
            val pieces = mutableListOf<Piece>()
            for (i in 0 until side) {
                for (j in 0 until side) {
                    val cord = Coordinate(i, j)
                    val pieceType = PieceType.entries.random()
                    pieces += Piece(cord, pieceType)
                }
            }
            val board = Board(side, pieces)
            val currentPlayer = PieceType.entries.random()
            val players = listOf(
                Player(PieceType.BLACK),
                Player(PieceType.WHITE)
            )
            val gameState = GameState(players, currentPlayer, board)
            list += gameState
        }
        list
    }

    val testingGameState = GameState(
        listOf(
            Player(PieceType.BLACK),
            Player(PieceType.WHITE)
        ),
        PieceType.BLACK,
        Board(8).startPieces()
    )

    private fun buildStringFromGameState(state: GameState): String {
        return buildString {
            for (player in state.players) {
                append("${player.type.symbol},${player.points};")
            }
            appendLine()
            appendLine(state.lastPlayer.symbol)
            appendLine(state.board.side)
            for (piece in state.board) {
                appendLine("${piece.coordinate.row},${piece.coordinate.col},${piece.value.symbol}")
            }
        }.trimEnd()
    }

    @Test
    fun `Test serialize and deserialize`() {
        testUnit.runTest()
    }

    @Test
    fun `Test serialize`() {
        val serialized = GameStateSerializer().serialize(testingGameState)
        val expected = buildStringFromGameState(testingGameState)
        assert(serialized == expected)
    }

    @Test
    fun `Test deserialize`() {
        val data = buildStringFromGameState(testingGameState)
        val deserialized = GameStateSerializer().deserialize(data)
        val expected = testingGameState
        assert(deserialized == expected)
    }

    @Test
    fun `Deserialize bad data throws exception`() {
        val badData1 = "B,10;W,20\nX\n8\n0,0,#" // Invalid last player
        val badData2 = "B,10;W,20\n@\nnotANumber\n0,0,#" // Invalid board side
        val badData3 = "B,10;W,20\n@\n8\n0,0,Z" // Invalid piece type
        val badData4 = "B,notANumber;W,20\n@\n8\n0,0,#" // Invalid player points
        val badData5 = "B,10;W,20\n@\n8\nnotARow,0,#" // Invalid piece row
        val badData6 = "B,10;W,20\n@\n8\n0,notACol,#" // Invalid piece column
        val badData7 = "B,10;W,20\n@\n8\n" // Missing pieces
        val badData8 = "B,10\n@\n8\n0,0,#" // Missing players
        val badDataList = listOf(badData1, badData2, badData3, badData4, badData5, badData6, badData7, badData8)

        for (badData in badDataList) {
            assertFails {
                GameStateSerializer().deserialize(badData)
            }
        }
    }
}