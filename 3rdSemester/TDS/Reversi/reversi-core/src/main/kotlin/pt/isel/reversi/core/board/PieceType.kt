package pt.isel.reversi.core.board

/**
 * Represents the type of piece on the board.
 */
enum class PieceType(val symbol: Char) {
    BLACK('#'),
    WHITE('@');

    /**
     * Swaps the piece type to the opposite color.
     * @return The opposite PieceType.
     */
    fun swap(): PieceType =
        if (this == BLACK) WHITE
        else BLACK

    companion object {
        /**
         * Retrieves the PieceType corresponding to the given symbol.
         * @param symbol The character symbol representing the piece type.
         * @return The matching PieceType, or null if no match is found.
         */
        fun fromSymbol(symbol: Char): PieceType? = entries.firstOrNull { it.symbol == symbol }
    }
}
