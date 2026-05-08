package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.exceptions.InvalidBoardInFileException
import pt.isel.reversi.storage.Serializer

/**
 * Serializer for the Board class, converting it to and from a String representation.
 */
class BoardSerializer: Serializer<Board, String> {
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
                if (part.isNotEmpty())
                    pieces += pieceSerializer.deserialize(part)
            }
            return Board(side, pieces)
        } catch (e: Exception) {
            throw InvalidBoardInFileException("Invalid board data. Error: ${e.message}")
        }
    }
}