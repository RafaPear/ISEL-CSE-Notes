package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.PieceType
import kotlin.test.Test
import kotlin.test.assertFails

class PieceTypeSerializerTest {
    val testUnit = SerializerTestUnit(PieceTypeSerializer()) {
        PieceType.entries
    }

    @Test
    fun `Test serialize and deserialize`() {
        testUnit.runTest()
    }

    @Test
    fun `Test serialize`() {
        val pieceType = PieceType.BLACK
        val serialized = PieceTypeSerializer().serialize(pieceType)

        val expected = pieceType.symbol

        assert(serialized == expected) {
            "Serialization failed. Expected: $expected, got: $serialized"
        }
    }

    @Test
    fun `Test deserialize`() {
        val data = '@'
        val deserialized = PieceTypeSerializer().deserialize(data)
        val expected = PieceType.WHITE
        assert(deserialized == expected) {
            "Deserialization failed. Expected: $expected, got: $deserialized"
        }
    }

    @Test
    fun `Deserialize bad data throws exception`() {
        val badData = 'Z' // Invalid piece type

        assertFails {
            PieceTypeSerializer().deserialize(badData)
        }
    }
}