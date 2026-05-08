package pt.isel.reversi.core.storage.serializers

import org.junit.Test
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import kotlin.test.assertEquals
import kotlin.test.assertFails

class BoardSerializerTest {

    private fun buildStringFromBoard(board: Board) =
        buildString {
            appendLine(board.side)
            for (piece in board) {
                appendLine("${piece.coordinate.row},${piece.coordinate.col},${piece.value.symbol}")
            }
        }.trimEnd()

    @Test
    fun `serialize 4x4 board`() {
        val board = Board(
            4, listOf(
                Piece(Coordinate(0, 0), PieceType.BLACK),
                Piece(Coordinate(0, 1), PieceType.WHITE),
                Piece(Coordinate(1, 0), PieceType.WHITE),
                Piece(Coordinate(1, 1), PieceType.BLACK)
            )
        )
        val serialized = BoardSerializer().serialize(board)
        val expected = buildStringFromBoard(board)

        assertEquals(expected, serialized, "Serialization failed for 4x4 board")
    }

    @Test
    fun `serialize 8x8 board`() {
        val board = Board(8)
        val serialized = BoardSerializer().serialize(board)
        val expected = buildStringFromBoard(board)

        assertEquals(expected, serialized, "Serialization failed for 8x8 board")
    }

    @Test
    fun `serialize empty board`() {
        val board = Board(4, emptyList())
        val serialized = BoardSerializer().serialize(board)
        val expected = buildStringFromBoard(board)

        assertEquals(expected, serialized, "Serialization failed for empty 4x4 board")
    }

    @Test
    fun `deserialize 4x4 board`() {
        val data = """
            4
            0,0,#
            0,1,@
            1,0,@
            1,1,#
        """.trimIndent()
        val deserialized = BoardSerializer().deserialize(data)
        val expected = Board(
            4, listOf(
                Piece(Coordinate(0, 0), PieceType.BLACK),
                Piece(Coordinate(0, 1), PieceType.WHITE),
                Piece(Coordinate(1, 0), PieceType.WHITE),
                Piece(Coordinate(1, 1), PieceType.BLACK)
            )
        )

        assertEquals(expected, deserialized, "Deserialization failed for 4x4 board")
    }

    @Test
    fun `deserialize empty board`() {
        val data = "4"
        val deserialized = BoardSerializer().deserialize(data)
        val expected = Board(4, emptyList())

        assertEquals(expected, deserialized, "Deserialization failed for empty 4x4 board")
    }

    @Test
    fun `deserialize 8x8 board with all blacks`() {
        val data = buildString {
            appendLine("8")
            for (i in 0 until 8) {
                for (j in 0 until 8) {
                    appendLine("$i,$j,#")
                }
            }
        }.trimEnd()
        val deserialized = BoardSerializer().deserialize(data)

        assertEquals(8, deserialized.side, "Deserialization failed: incorrect board size")
    }

    @Test
    fun `deserialize with invalid piece type throws exception`() {
        val badData = """
            4
            0,0,#
            0,1,Z
            1,0,@
            1,1,#
        """.trimIndent()

        assertFails {
            BoardSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid side throws exception`() {
        val badData = """
            notASide
            0,0,#
            0,1,@
            1,0,@
            1,1,#
        """.trimIndent()

        assertFails {
            BoardSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with negative side throws exception`() {
        val badData = """
            -4
            0,0,#
        """.trimIndent()

        assertFails {
            BoardSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid row throws exception`() {
        val badData = """
            4
            notARow,0,#
            0,1,@
            1,0,@
            1,1,#
        """.trimIndent()

        assertFails {
            BoardSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid column throws exception`() {
        val badData = """
            4
            0,notACol,#
            0,1,@
            1,0,@
            1,1,#
        """.trimIndent()

        assertFails {
            BoardSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with out of bounds row throws exception`() {
        val badData = """
            4
            0,0,#
            0,1,@
            5,0,@
            1,1,#
        """.trimIndent()

        assertFails {
            BoardSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with out of bounds column throws exception`() {
        val badData = """
            4
            0,0,#
            0,1,@
            1,0,@
            1,5,#
        """.trimIndent()

        assertFails {
            BoardSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with missing piece data throws exception`() {
        val badData = """
            4
            0,0
            0,1,@
        """.trimIndent()

        assertFails {
            BoardSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with empty string throws exception`() {
        val badData = ""

        assertFails {
            BoardSerializer().deserialize(badData)
        }
    }
}