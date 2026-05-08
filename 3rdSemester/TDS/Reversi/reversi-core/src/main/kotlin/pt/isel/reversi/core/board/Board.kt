package pt.isel.reversi.core.board

import pt.isel.reversi.core.SIDE_MAX
import pt.isel.reversi.core.SIDE_MIN

/**
 * Represents a board game grid.
 * @property pieces The list of pieces on the board.
 * @property side The size of the board (side x side).
 * @throws IllegalArgumentException if the side is not within the valid range or not even.
 */
data class Board(
    val side: Int,
    private val pieces: List<Piece> = emptyList(),
) : Iterable<Piece> {
    private val totalBlackPieces: Int
    private val totalWhitePieces: Int


    /**
     * Initializes the board and validates its properties.
     * Initializes the total number of black and white pieces on the board.
     */
    init {
        require(side in SIDE_MIN..SIDE_MAX) {
            "Side must be between $SIDE_MIN and $SIDE_MAX"
        }
        require(side % 2 == 0) {
            "Side must be even"
        }

        var countBlackPieces = 0
        var countWhitePieces = 0

        pieces.forEach { piece ->
            if (piece.value == PieceType.BLACK) {
                countBlackPieces++
            } else if (piece.value == PieceType.WHITE) {
                countWhitePieces++
            }
        }
        totalBlackPieces = countBlackPieces
        totalWhitePieces = countWhitePieces
    }

    /**
     * Gets the total number of black pieces on the board.
     * @return The total number of black pieces.
     */
    fun totalBlackPieces(): Int = totalBlackPieces

    /**
     * Gets the total number of white pieces on the board.
     * @return The total number of white pieces.
     */
    fun totalWhitePieces(): Int = totalWhitePieces

    /**
     * Converts a linear index to a Coordinate on the board.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
    fun Int.toCoordinate(): Coordinate {
        require(this in 0 until side * side) {
            "Index must be between 0 and ${side * side - 1}"
        }
        val row = this / side + 1
        val col = this % side + 1
        return Coordinate(row, col)
    }

    /**
     * Checks if the specified row and column are within the bounds of the board.
     * @throws IllegalArgumentException if the row or column are out of bounds.
     */
    fun checkPosition(coordinate: Coordinate) {
        require(coordinate.isValid(side)) {
            "Position ($coordinate is out of bounds)"
        }
    }

    /**
     * Gets the piece at the specified index like linear list.
     * @return The piece at the PieceType, or null if there is no piece.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
    operator fun get(idx: Int): PieceType? {
        val coordinate = idx.toCoordinate()
        return pieces.find { it.coordinate == coordinate }?.value
    }

    /**
     * Gets the piece at the specified row and column.
     * @return The piece at the specified position, or null if there is no piece.
     * @throws IllegalArgumentException if the row or column are out of bounds.
     */
    operator fun get(coordinate: Coordinate): PieceType? {
        checkPosition(coordinate)
        return pieces.find { it.coordinate == coordinate }?.value
    }

    /**
     * Changes the piece at the specified row and column from 'b' to 'w' or from 'w' to 'b'.
     * @return true if the piece was changed, false if there is no piece at the specified position.
     * @throws IllegalArgumentException if the row or column are out of bounds.
     */
    fun changePiece(coordinate: Coordinate): Board {
        checkPosition(coordinate)
        val value = this[coordinate]?.swap() ?: throw IllegalArgumentException("No piece at position $coordinate")

        return this.copy(
            pieces = pieces.map { piece -> // need preserve the original order
                if (piece.coordinate == coordinate)
                    Piece(coordinate, value)
                else
                    piece
            },
        )
    }

    /**
     * Changes the piece at the specified index like linear list from 'b' to 'w' or from 'w' to 'b'.
     * @param idx The index of the piece to change.
     * @return true if the piece was changed, false if there is no piece at the specified position.
     * @throws IllegalArgumentException if the index is out of bounds.
     */
    fun changePiece(idx: Int): Board {
        val coordinate = idx.toCoordinate()
        return changePiece(coordinate)
    }

    /**
     * Adds a piece to the board at the specified row and column.
     * @param coordinate The (row, column) coordinate where the piece will be added.
     * @param value The type of piece to add.
     * @return A new board with the added piece.
     * @throws IllegalArgumentException if the row or column are out of bounds.
     */
    fun addPiece(coordinate: Coordinate, value: PieceType): Board {
        checkPosition(coordinate)
        require(this[coordinate] == null) {
            "There is already a piece at position $coordinate"
        }
        return this.copy(
            pieces = pieces + Piece(coordinate, value)
        )
    }

    /**
     * Adds a piece to the board at the specified index like linear list.
     * @param idx The index where the piece will be added.
     * @param value The type of piece to add.
     * @return A new board with the added piece.
     * @throws IllegalArgumentException if the row or column are out of bounds.
     */
    fun addPiece(idx: Int, value: PieceType): Board {
        val coordinate = idx.toCoordinate()
        return addPiece(coordinate, value)
    }

    /**
     * Adds a piece to the board.
     * @param piece The piece to add.
     * @return A new board with the added piece.
     * @throws IllegalArgumentException if the row or column are out of bounds.
     */
    fun addPiece(piece: Piece): Board = addPiece(piece.coordinate, piece.value)

    /**
     * Starts the board with the initial pieces in the center.
     * @return A list of the initial pieces.
     */
    fun startPieces(): Board {
        val mid = side / 2
        return this.copy(
            pieces = listOf(
                Piece(Coordinate(mid, mid), PieceType.WHITE),
                Piece(Coordinate(mid + 1, mid + 1), PieceType.WHITE),
                Piece(Coordinate(mid, mid + 1), PieceType.BLACK),
                Piece(Coordinate(mid + 1, mid), PieceType.BLACK)
            ),
        )
    }

    /**
     * Returns an iterator of the value of pieces on the board.
     * @return An iterator of Piece.
     */
    override fun iterator(): Iterator<Piece> = object : Iterator<Piece> {
        private val it = pieces.iterator()
        override fun hasNext() = it.hasNext()
        override fun next() = it.next()
    }
}