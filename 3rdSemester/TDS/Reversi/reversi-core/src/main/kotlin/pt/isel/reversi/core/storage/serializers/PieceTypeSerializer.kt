package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.InvalidPieceTypeInFileException
import pt.isel.reversi.storage.Serializer

/**
 * Serializer for the PieceType enum, converting it to and from a Char representation.
 */
class PieceTypeSerializer: Serializer<PieceType, Char> {
    override fun serialize(obj: PieceType): Char {
        return obj.symbol
    }

    override fun deserialize(obj: Char): PieceType {
        return PieceType.fromSymbol(obj) ?:
        throw InvalidPieceTypeInFileException()
    }
}