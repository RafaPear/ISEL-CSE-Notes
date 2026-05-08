package pt.isel.reversi.core.storage.serializers

import org.junit.Test
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import kotlin.test.assertFails

class BoardSerializerTest {
    val testUnit = SerializerTestUnit(BoardSerializer()) {
        val list = mutableListOf<Board>()
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
            list += Board(side, pieces)
        }
        list
    }

    private fun buildStringFromBoard(board: Board) =
        buildString {
            appendLine(board.side)
            for (piece in board) {
                appendLine("${piece.coordinate.row},${piece.coordinate.col},${piece.value.symbol}")
            }
        }.trimEnd()


    @Test
    fun `Test serialize and deserialize`() {
        testUnit.runTest()
    }

    @Test
    fun `Test serialize`() {
        val board = Board(4, listOf(
            Piece(Coordinate(0, 0), PieceType.BLACK),
            Piece(Coordinate(0, 1), PieceType.WHITE),
            Piece(Coordinate(1, 0), PieceType.WHITE),
            Piece(Coordinate(1, 1), PieceType.BLACK)
        ))
        val serialized = BoardSerializer().serialize(board)
        val expected = buildStringFromBoard(board)

        assert(serialized == expected) {
            "Serialization failed. Expected: $expected, got: $serialized"
        }
    }

    @Test
    fun `Test deserialize`() {
        val data = """
            4
            0,0,#
            0,1,@
            1,0,@
            1,1,#
        """.trimIndent()
        val deserialized = BoardSerializer().deserialize(data)
        val expected = Board(4, listOf(
            Piece(Coordinate(0, 0), PieceType.BLACK),
            Piece(Coordinate(0, 1), PieceType.WHITE),
            Piece(Coordinate(1, 0), PieceType.WHITE),
            Piece(Coordinate(1, 1), PieceType.BLACK)
        ))

        assert(deserialized == expected) {
            "Deserialization failed. Expected: $expected, got: $deserialized"
        }
    }

    @Test
    fun `Deserialize bad data throws exception`() {
        val badData = """
            4
            0,0,#
            0,1,Z
            1,0,@
            1,1,#
        """.trimIndent() // Invalid piece type

        val badData2 = """
            notASide
            0,0,#
            0,1,@
            1,0,@
            1,1,#
        """.trimIndent() // Invalid side

        val badData3 = """
            4
            notARow,0,#
            0,1,@
            1,0,@
            1,1,#
        """.trimIndent() // Invalid row

        val badData4 = """
            4
            0,notACol,#
            0,1,@
            1,0,@
            1,1,#
        """.trimIndent() // Invalid column

        assertFails {
            BoardSerializer().deserialize(badData)
        }
        assertFails {
            BoardSerializer().deserialize(badData2)
        }
        assertFails {
            BoardSerializer().deserialize(badData3)
        }
        assertFails {
            BoardSerializer().deserialize(badData4)
        }
    }
}