package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.gameState.Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class PlayerSerializerTest {

    @Test
    fun `serialize BLACK player with 15 points`() {
        val player = Player(PieceType.BLACK, points = 15)
        val serialized = PlayerSerializer().serialize(player)
        val expected = "${player.type.symbol},${player.name},${player.points}"

        assertEquals(expected, serialized, "Serialization failed for BLACK player with 15 points")
    }

    @Test
    fun `serialize WHITE player with 0 points`() {
        val player = Player(PieceType.WHITE, points = 0)
        val serialized = PlayerSerializer().serialize(player)
        val expected = "${player.type.symbol},${player.name},${player.points}"

        assertEquals(expected, serialized, "Serialization failed for WHITE player with 0 points")
    }

    @Test
    fun `serialize BLACK player with 100 points`() {
        val player = Player(PieceType.BLACK, points = 100)
        val serialized = PlayerSerializer().serialize(player)
        val expected = "${player.type.symbol},${player.name},${player.points}"

        assertEquals(expected, serialized, "Serialization failed for BLACK player with 100 points")
    }

    @Test
    fun `deserialize WHITE player with 25 points`() {
        val data = "@,WHITE,25"
        val deserialized = PlayerSerializer().deserialize(data)
        val expected = Player(PieceType.WHITE, points = 25)

        assertEquals(expected, deserialized, "Deserialization failed for WHITE player with 25 points")
    }

    @Test
    fun `deserialize BLACK player with 0 points`() {
        val data = "#,BLACK,0"
        val deserialized = PlayerSerializer().deserialize(data)
        val expected = Player(PieceType.BLACK, points = 0)

        assertEquals(expected, deserialized, "Deserialization failed for BLACK player with 0 points")
    }

    @Test
    fun `deserialize WHITE player with 64 points`() {
        val data = "@,WHITE,64"
        val deserialized = PlayerSerializer().deserialize(data)
        val expected = Player(PieceType.WHITE, points = 64)

        assertEquals(expected, deserialized, "Deserialization failed for WHITE player with 64 points")
    }

    @Test
    fun `deserialize with invalid points format throws exception`() {
        val badData = "B,notANumber"
        assertFails {
            PlayerSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with missing pieces throws exception`() {
        val badData = "B,10"
        assertFails {
            PlayerSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with invalid piece type throws exception`() {
        val badData = "Z,10,20"
        assertFails {
            PlayerSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with negative points throws exception`() {
        val badData = "#,BLACK,-5"
        assertFails {
            PlayerSerializer().deserialize(badData)
        }
    }

    @Test
    fun `deserialize with empty string throws exception`() {
        val badData = ""
        assertFails {
            PlayerSerializer().deserialize(badData)
        }
    }
}