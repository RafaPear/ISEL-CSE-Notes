package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.InvalidBoardInFile
import pt.isel.reversi.storage.Serializer

/**
 * Serializer for the Board class, converting it to and from a String representation.
 */
internal class BoardSerializer : Serializer<Board, String> {
    private val pieceSerializer = PieceSerializer()

    override fun serialize(obj: Board): String {
        val sb = StringBuilder()

        sb.append(obj.side)

        for (piece in obj) {
            sb.appendLine()
            sb.append(pieceSerializer.serialize(piece))
        }

        return sb.toString()
    }

    override fun deserialize(obj: String): Board {
        try {
            val parts = obj.split("\n")
            val side = parts[0].toInt()
            val pieces = mutableListOf<Piece>()
            for (part in parts.drop(1)) {
                if (part.isNotEmpty()) {
                    val piece = pieceSerializer.deserialize(part)
                    if (piece.coordinate.row > side || piece.coordinate.col > side) {
                        throw InvalidBoardInFile(
                            message = "Piece coordinate out of bounds: ${piece.coordinate}",
                            type = ErrorType.ERROR
                        )
                    }
                    pieces += piece
                }
            }
            return Board(side, pieces)
        } catch (e: Exception) {
            throw InvalidBoardInFile(
                message = "Invalid board data. Error: ${e.message}",
                type = ErrorType.ERROR
            )
        }
    }
}