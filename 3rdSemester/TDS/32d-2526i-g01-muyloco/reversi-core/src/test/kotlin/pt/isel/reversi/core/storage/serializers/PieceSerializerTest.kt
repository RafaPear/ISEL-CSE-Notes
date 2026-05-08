package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class PieceSerializerTest {

    @Test
    fun `serialize BLACK piece at coordinate 5,10`() {
        val piece = Piece(Coordinate(5, 10), PieceType.BLACK)
        val serialized = PieceSerializer().serialize(piece)
        val expected = "${piece.coordinate.row},${piece.coordinate.col},${piece.value.symbol}"

        assertEquals(expected, serialized, "Serialization failed for BLACK piece at (5,10)")
    }

    @Test
    fun `serialize WHITE piece at coordinate 0,0`() {
        val piece = Piece(Coordinate(0, 0), PieceType.WHITE)
        val serialized = PieceSerializer().serialize(piece)
        val expected = "${piece.coordinate.row},${piece.coordinate.col},${piece.value.symbol}"

        assertEquals(expected, serialized, "Serialization failed for WHITE piece at (0,0)")
    }

    @Test
    fun `serialize BLACK piece at large coordinate 15,15`() {
        val piece = Piece(Coordinate(15, 15), PieceType.BLACK)
        val serialized = PieceSerializer().serialize(piece)
        val expected = "${piece.coordinate.row},${piece.coordinate.col},${piece.value.symbol}"

        assertEquals(expected, serialized, "Serialization failed for BLACK piece at (15,15)")
    }

    @Test
    fun `deserialize WHITE piece at coordinate 7,14`() {
        val data = "7,14,@"
        val deserialized = PieceSerializer().deserialize(data)
        val expected = Piece(Coordinate(7, 14), PieceType.WHITE)

        assertEquals(expected, deserialized, "Deserialization failed for WHITE piece at (7,14)")
    }

    @Test
    fun `deserialize BLACK piece at coordinate 0,0`() {
        val data = "0,0,#"
        val deserialized = PieceSerializer().deserialize(data)
        val expected = Piece(Coordinate(0, 0), PieceType.BLACK)

        assertEquals(expected, deserialized, "Deserialization failed for BLACK piece at (0,0)")
    }

    @Test
    fun `deserialize WHITE piece at large coordinate 15,15`() {
        val data = "15,15,@"
        val deserialized = PieceSerializer().deserialize(data)
        val expected = Piece(Coordinate(15, 15), PieceType.WHITE)

        assertEquals(expected, deserialized, "Deserialization failed for WHITE piece at (15,15)")
    }

    @Test
    fun `deserialize with invalid piece type throws exception`() {
        val badData = "3,4,Z"
        assertFails {
            PieceSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid row throws exception`() {
        val badData = "notARow,4,#"
        assertFails {
            PieceSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid column throws exception`() {
        val badData = "3,notACol,#"
        assertFails {
            PieceSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with negative row throws exception`() {
        val badData = "-1,5,#"
        assertFails {
            PieceSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with negative column throws exception`() {
        val badData = "5,-1,@"
        assertFails {
            PieceSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with missing column throws exception`() {
        val badData = "3,#"
        assertFails {
            PieceSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with missing piece type throws exception`() {
        val badData = "3,4"
        assertFails {
            PieceSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with empty string throws exception`() {
        val badData = ""
        assertFails {
            PieceSerializer().deserialize(badData)
        }
    }
}