package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.PieceType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class PieceTypeSerializerTest {

    @Test
    fun `serialize BLACK piece type`() {
        val pieceType = PieceType.BLACK
        val serialized = PieceTypeSerializer().serialize(pieceType)
        val expected = pieceType.symbol

        assertEquals(expected, serialized, "Serialization failed for BLACK piece type")
    }

    @Test
    fun `serialize WHITE piece type`() {
        val pieceType = PieceType.WHITE
        val serialized = PieceTypeSerializer().serialize(pieceType)
        val expected = pieceType.symbol

        assertEquals(expected, serialized, "Serialization failed for WHITE piece type")
    }

    @Test
    fun `deserialize BLACK piece type`() {
        val data = '#'
        val deserialized = PieceTypeSerializer().deserialize(data)
        val expected = PieceType.BLACK

        assertEquals(expected, deserialized, "Deserialization failed for BLACK piece type")
    }

    @Test
    fun `deserialize WHITE piece type`() {
        val data = '@'
        val deserialized = PieceTypeSerializer().deserialize(data)
        val expected = PieceType.WHITE

        assertEquals(expected, deserialized, "Deserialization failed for WHITE piece type")
    }

    @Test
    fun `deserialize with invalid character throws exception`() {
        val badData = 'Z'
        assertFails {
            PieceTypeSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with lowercase black symbol throws exception`() {
        val badData = 'b'
        assertFails {
            PieceTypeSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with lowercase white symbol throws exception`() {
        val badData = 'w'
        assertFails {
            PieceTypeSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with space character throws exception`() {
        val badData = ' '
        assertFails {
            PieceTypeSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with numeric character throws exception`() {
        val badData = '1'
        assertFails {
            PieceTypeSerializer().deserialize(badData)
        }
    }
}