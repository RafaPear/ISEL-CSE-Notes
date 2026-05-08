package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.exceptions.InvalidPieceInFileException
import pt.isel.reversi.storage.Serializer

/**
 * Serializer for the Piece class, converting it to and from a String representation.
 */
class PieceSerializer: Serializer<Piece, String> {
    private val pieceTypeSerializer = PieceTypeSerializer()

    override fun serialize(obj: Piece): String {
        val (row, col) = obj.coordinate
        val symbol = pieceTypeSerializer.serialize(obj.value)
        return "$row,$col,$symbol"
    }

    override fun deserialize(obj: String): Piece {
        try {
            val (row, col, symbol) = obj.trim().split(",")
            val coordinate = Coordinate(row.toInt(), col.toInt())
            val value = pieceTypeSerializer.deserialize(symbol.first())
            return Piece(coordinate, value)
        } catch (e: Exception) {
            throw InvalidPieceInFileException("Invalid piece line. Error: ${e.message}")
        }
    }
}