package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import kotlin.test.Test
import kotlin.test.assertFails

class PieceSerializerTest {
    val testUnit = SerializerTestUnit(PieceSerializer()){
        val list = mutableListOf<Piece>()
        for (i in 1..50) {
            for (j in 1..50) {
                val coord = Coordinate(i, j)
                for (pieceType in PieceType.entries) {
                    list += Piece(coord, pieceType)
                }
            }
        }
        list
    }

    @Test
    fun `Test serialize and deserialize`() {
        testUnit.runTest()
    }

    @Test
    fun `Test serialize`() {
        val piece = Piece(Coordinate(5, 10), PieceType.BLACK)
        val serialized = PieceSerializer().serialize(piece)
        val expected = "${piece.coordinate.row},${piece.coordinate.col},${piece.value.symbol}"
        assert(serialized == expected) {
            "Serialization failed. Expected: $expected, got: $serialized"
        }
    }

    @Test
    fun `Test deserialize`() {
        val data = "7,14,@"
        val deserialized = PieceSerializer().deserialize(data)
        val expected = Piece(Coordinate(7, 14), PieceType.WHITE)
        assert(deserialized == expected) {
            "Deserialization failed. Expected: $expected, got: $deserialized"
        }
    }

    @Test
    fun `Deserialize bad data throws exception`() {
        val badData = "3,4,Z" // Invalid piece type
        val badData2 = "notARow,4,#" // Invalid row
        val badData3 = "3,notACol,#" // Invalid column

        assertFails {
            PieceSerializer().deserialize(badData)
        }
        assertFails {
            PieceSerializer().deserialize(badData2)
        }
        assertFails {
            PieceSerializer().deserialize(badData3)
        }
    }
}