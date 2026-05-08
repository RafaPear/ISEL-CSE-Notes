package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.Player
import pt.isel.reversi.core.board.PieceType
import kotlin.test.Test
import kotlin.test.assertFails

class PlayerSerializerTest {
    val testUnit = SerializerTestUnit(PlayerSerializer()) {
        listOf(
            Player(PieceType.BLACK, 10),
            Player(PieceType.WHITE, 20),
            Player(PieceType.BLACK, 0),
            Player(PieceType.WHITE, 100)
        )
    }

    @Test
    fun `Test serialize and deserialize`() {
        testUnit.runTest()
    }

    @Test
    fun `Test serialize`() {
        val player = Player(PieceType.BLACK, 15)
        val serialized = PlayerSerializer().serialize(player)

        val expected = "${player.type.symbol},${player.points}"

        assert(serialized == expected) {
            "Serialization failed. Expected: $expected, got: $serialized"
        }
    }

    @Test
    fun `Test deserialize`() {
        val data = "@,25"
        val deserialized = PlayerSerializer().deserialize(data)
        val expected = Player(PieceType.WHITE, 25)
        assert(deserialized == expected) {
            "Deserialization failed. Expected: $expected, got: $deserialized"
        }
    }

    @Test
    fun `Deserialize bad data throws exception`() {
        val badData = "B,notANumber" // Invalid points

        assertFails {
            PlayerSerializer().deserialize(badData)
        }
    }
}