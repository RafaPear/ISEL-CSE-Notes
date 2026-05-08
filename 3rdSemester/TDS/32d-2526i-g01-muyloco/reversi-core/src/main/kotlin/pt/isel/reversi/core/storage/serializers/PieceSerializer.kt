package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.InvalidPieceInFile
import pt.isel.reversi.storage.Serializer

/**
 * Serializer for the Piece class, converting it to and from a String representation.
 */
internal class PieceSerializer : Serializer<Piece, String> {
    private val pieceTypeSerializer = PieceTypeSerializer()

    override fun serialize(obj: Piece): String {
        val (row, col) = obj.coordinate
        val symbol = pieceTypeSerializer.serialize(obj.value)
        return "$row,$col,$symbol"
    }

    override fun deserialize(obj: String): Piece {
        try {
            val (row, col, symbol) = obj.trim().split(",")
            if (row.isEmpty() || col.isEmpty() || symbol.isEmpty()) {
                throw InvalidPieceInFile(
                    message = "Piece line has empty fields: '$obj'",
                    type = ErrorType.ERROR
                )
            }
            if (symbol.length != 1) {
                throw InvalidPieceInFile(
                    message = "Invalid piece symbol length in line: '$obj'",
                    type = ErrorType.ERROR
                )
            }
            if (!row.all { it.isDigit() } || !col.all { it.isDigit() }) {
                throw InvalidPieceInFile(
                    message = "Row and column must be integers in line: '$obj'",
                    type = ErrorType.ERROR
                )
            }
            if (row.toInt() < 0 || col.toInt() < 0) {
                throw InvalidPieceInFile(
                    message = "Row and column must be non-negative in line: '$obj'",
                    type = ErrorType.ERROR
                )
            }
            val coordinate = Coordinate(row.toInt(), col.toInt())
            val value = pieceTypeSerializer.deserialize(symbol.first())
            return Piece(coordinate, value)
        } catch (e: Exception) {
            throw InvalidPieceInFile(
                message = "Invalid piece line. Error: ${e.message}",
                type = ErrorType.ERROR
            )
        }
    }
}