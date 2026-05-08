package pt.isel.reversi.core.board

/**
 * Represents a coordinate on the board.
 * @property row The row index (1-based).
 * @property col The column index (1-based).
 */
data class Coordinate(val row: Int, val col: Int) {
    companion object {
        val allDirection = listOf(
            Coordinate(-1, -1), // UP LEFT
            Coordinate(-1, 0), // UP
            Coordinate(-1, 1), // UP RIGHT
            Coordinate(0, -1), // LEFT
            Coordinate(0, 1), // RIGHT
            Coordinate(1, -1), // DOWN LEFT
            Coordinate(1, 0), // DOWN
            Coordinate(1, 1) // DOWN RIGHT
        )
    }

    /**
     * Creates a coordinate from a row and a column character.
     * @param row The row index (1-based).
     * @param col The column character (a-z or A-Z).
     */
    constructor(row: Int, col: Char) : this(
        row,
        col.lowercase()[0] - 'a' + 1
    ) {
        require(col in 'a'..'z' || col in 'A'..'Z') {
            "Column must be a letter from a to z or A to Z"
        }
    }

    /**
     * Adds two coordinates together.
     * @param other The coordinate to add.
     * @return The resulting coordinate.
     */
    operator fun plus(other: Coordinate): Coordinate =
        Coordinate(this.row + other.row, this.col + other.col)

    /**
     * Subtracts one coordinate from another.
     * @param other The coordinate to subtract.
     * @return The resulting coordinate.
     */
    operator fun minus(other: Coordinate): Coordinate =
        Coordinate(this.row - other.row, this.col - other.col)

    /**
     * Checks if the coordinate is valid for the given board size.
     * @param boardSide The size of the board (number of rows/columns).
     * @return True if the coordinate is within the bounds of the board.
     */
    fun isValid(boardSide: Int): Boolean =
        row in 1..boardSide && col in 1..boardSide
}