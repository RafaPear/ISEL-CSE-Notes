package pt.isel.reversi.core.board

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BoardTests {

    // Board tests

    @Test
    fun `toCoordinate with index outside range fails`() {
        val indexes = listOf(-1, 64)

        indexes.forEach {
            assertFailsWith<IllegalArgumentException> {
                Board(8).run { it.toCoordinate() }
            }
        }
    }

    @Test
    fun `toCoordinate with valid index succeeds`() {
        val indexes = listOf(0, 63)
        val expected = listOf(Coordinate(1, 1), Coordinate(8, 8))

        assertContentEquals(
            expected,
            indexes.map { Board(8).run { it.toCoordinate() } }
        )
    }

    @Test
    fun `Create Board with side outside range fails`() {
        val sizes = listOf(-1, 0, 2, 33)

        sizes.forEach {
            assertFailsWith<IllegalArgumentException> {
                Board(it)
            }
        }
    }

    @Test
    fun `Create Board with odd side within range fails`() {
        val sizes = listOf(7, 15, 25)

        sizes.forEach {
            assertFailsWith<IllegalArgumentException> {
                Board(it)
            }
        }
    }

    @Test
    fun `Create Board with startPieces succeeds`() {
        val board = Board(16)

        val uut = board.startPieces()

        assertEquals(PieceType.WHITE, uut[Coordinate(8, 'h')])
        assertEquals(PieceType.BLACK, uut[Coordinate(8, 'i')])
        assertEquals(PieceType.BLACK, uut[Coordinate(9, 'h')])
        assertEquals(PieceType.WHITE, uut[Coordinate(9, 'i')])
    }

    @Test
    fun `get function with index outside range fails`() {
        val indexes = listOf(-1, 64)

        indexes.forEach {
            assertFailsWith<IllegalArgumentException> {
                Board(8)[it]
            }
        }
    }

    @Test
    fun `get function with row outside range fails`() {
        val rows = listOf(-1, 0, 9)

        rows.forEach {
            assertFailsWith<IllegalArgumentException> {
                Board(8)[Coordinate(it, 1)]
            }
        }
    }

    @Test
    fun `get function with col (Int) outside range fails`() {
        val cols = listOf(-1, 0, 9)

        cols.forEach {
            assertFailsWith<IllegalArgumentException> {
                Board(8)[Coordinate(1, it)]
            }
        }
    }

    @Test
    fun `get function with col (Char) outside range fails`() {
        val cols = listOf('@', '[')

        cols.forEach {
            assertFailsWith<IllegalArgumentException> {
                Board(8)[Coordinate(1, it)]
            }
        }
    }

    @Test
    fun `get function with no piece at index returns null`() {
        assert(Board(8)[0] == null)
    }

    @Test
    fun `get and addPiece function with valid row and col (Int) succeeds`() {
        val board = Board(4).addPiece(Coordinate(1, 1), PieceType.WHITE)

        assert(board[Coordinate(1, 1)] == PieceType.WHITE)
    }

    @Test
    fun `get and addPiece function with valid row and col (Char) succeeds`() {
        val board = Board(4).addPiece(Coordinate(1, 'a'), PieceType.WHITE)

        assert(board[Coordinate(1, 'a')] == PieceType.WHITE)
    }

    @Test
    fun `get and addPiece function with valid index succeeds`() {
        val board = Board(4).addPiece(Coordinate(1, 'a'), PieceType.WHITE)

        assert(board[0] == PieceType.WHITE)
    }

    @Test
    fun `changePiece function with index outside range fails`() {
        assertFailsWith<IllegalArgumentException> {
            Board(8).changePiece(-1)
        }
    }

    @Test
    fun `changePiece function with row outside range fails`() {
        val rows = listOf(-1, 0, 9)

        rows.forEach {
            assertFailsWith<IllegalArgumentException> {
                Board(8).changePiece(Coordinate(it, 1))
            }
        }
    }

    @Test
    fun `changePiece function with col (Int) outside range fails`() {
        val cols = listOf(-1, 0, 9)

        cols.forEach {
            assertFailsWith<IllegalArgumentException> {
                Board(8).changePiece(Coordinate(1, it))
            }
        }
    }

    @Test
    fun `changePiece function with col (Char) outside range fails`() {
        val cols = listOf('@', '[')

        cols.forEach {
            assertFailsWith<IllegalArgumentException> {
                Board(8).changePiece(Coordinate(1, it))
            }
        }
    }

    @Test
    fun `changePiece function with no piece at position fails`() {
        assertFailsWith<IllegalArgumentException> {
            Board(8).changePiece(Coordinate(1, 'a'))
        }
    }

    @Test
    fun `changePiece function with valid row and col (Char) succeeds`() {
        val board = Board(4).startPieces()
        val updatedBoard = board.changePiece(Coordinate(2, 'b'))
        val expected = listOf(
            Piece(Coordinate(2, 'b'), PieceType.BLACK),
            Piece(Coordinate(3, 'c'), PieceType.WHITE),
            Piece(Coordinate(2, 'c'), PieceType.BLACK),
            Piece(Coordinate(3, 'b'), PieceType.BLACK)
        )

        assertEquals(updatedBoard.toList(), expected)
    }

    @Test
    fun `changePiece function with valid row and col (Int) succeeds`() {
        val board = Board(4).startPieces()
        val updatedBoard = board.changePiece(Coordinate(2, 2))
        val expected = listOf(
            Piece(Coordinate(2, 2), PieceType.BLACK),
            Piece(Coordinate(3, 3), PieceType.WHITE),
            Piece(Coordinate(2, 3), PieceType.BLACK),
            Piece(Coordinate(3, 2), PieceType.BLACK)
        )

        assertEquals(updatedBoard.toList(), expected)
    }

    @Test
    fun `addPiece function with row outside range fails`() {
        val board = Board(8)
        val rows = listOf(-1, 0, 9)

        rows.forEach {
            assertFailsWith<IllegalArgumentException> {
                board.addPiece(Coordinate(it, 1), PieceType.BLACK)
            }
        }
    }

    @Test
    fun `addPiece function with col (Int) outside range fails`() {
        val board = Board(8)
        val cols = listOf(-1, 0, 9)

        cols.forEach {
            assertFailsWith<IllegalArgumentException> {
                board.addPiece(Coordinate(1, it), PieceType.BLACK)
            }
        }
    }

    @Test
    fun `addPiece function with col (Char) outside range fails`() {
        val board = Board(8)
        val cols = listOf('@', '[')

        cols.forEach {
            assertFailsWith<IllegalArgumentException> {
                board.addPiece(Coordinate(1, it), PieceType.BLACK)
            }
        }
    }

    @Test
    fun `addPiece function with index outside range fails`() {
        assertFailsWith<IllegalArgumentException> {
            Board(8).addPiece(-1, PieceType.BLACK)
            Board(8).addPiece(64, PieceType.BLACK)
        }
    }

    @Test
    fun `addPiece function with piece already at position fails`() {
        val board = Board(4).addPiece(Coordinate(1, 'a'), PieceType.WHITE)

        assertFailsWith<IllegalArgumentException> {
            board.addPiece(Coordinate(1, 'a'), PieceType.BLACK)
        }
    }

    @Test
    fun `Last Piece added is at the end of the Piece list succeeds`() {
        val lastPieceExpected = Piece(Coordinate(3, 'c'), PieceType.BLACK)
        val board = Board(4)
            .addPiece(Coordinate(1, 'a'), PieceType.WHITE)
            .addPiece(lastPieceExpected)

        assert(lastPieceExpected == board.last())
    }

    @Test
    fun `Sequence of Pieces on Board matches added Pieces succeeds`() {
        val piece1 = Piece(Coordinate(1, 'a'), PieceType.WHITE)
        val piece2 = Piece(Coordinate(2, 'b'), PieceType.BLACK)
        val piece3 = Piece(Coordinate(3, 'c'), PieceType.WHITE)
        val board = Board(4)
            .addPiece(piece1)
            .addPiece(piece2)
            .addPiece(piece3)
        val expectedPieces = listOf(piece1, piece2, piece3)


        board.forEachIndexed { index, piece ->
            assert(piece == expectedPieces[index])
        }
    }

    @Test
    fun `Sequence of Pieces on Board after changes matches expected Pieces succeeds`() {
        val piece1 = Piece(Coordinate(1, 'a'), PieceType.WHITE)
        val piece2 = Piece(Coordinate(4, 'd'), PieceType.BLACK)
        val board = Board(4)
            .addPiece(piece1)
            .addPiece(piece2)
            .changePiece(Coordinate(4, 'd'))
        val expectedPieces = listOf(piece1, piece2)

        board.forEachIndexed { index, piece ->
            assert(piece.coordinate == expectedPieces[index].coordinate)
        }
    }

    @Test
    fun `totalBlackPieces and totalWhitePieces are correct after startPieces`() {
        val board = Board(4).startPieces()

        assert(board.totalBlackPieces() == 2)
        assert(board.totalWhitePieces() == 2)
    }

    @Test
    fun `totalBlackPieces and totalWhitePieces are correct after adding pieces`() {
        var board = Board(8)
        val expectedBlackPieces = 5
        val expectedWhitePieces = 2

        (1..(expectedBlackPieces)).forEach {
            board = board.addPiece(it, PieceType.BLACK)
        }
        (expectedBlackPieces + 1..(expectedBlackPieces + expectedWhitePieces)).forEach {
            board = board.addPiece(it, PieceType.WHITE)
        }

        assert(board.totalBlackPieces() == expectedBlackPieces)
        assert(board.totalWhitePieces() == expectedWhitePieces)
    }

    @Test
    fun `totalBlackPieces and totalWhitePieces are correct after changing pieces`() {
        var board = Board(8)
        val initialBlackPieces = 5
        val initialWhitePieces = 2

        (1..(initialBlackPieces)).forEach {
            board = board.addPiece(it, PieceType.BLACK)
        }
        (initialBlackPieces + 1..(initialBlackPieces + initialWhitePieces)).forEach {
            board = board.addPiece(it, PieceType.WHITE)
        }
        val piecesToChangeFromBlackToWhite = 2
        val piecesToChangeFromWhiteToBlack = 1
        (1..piecesToChangeFromBlackToWhite).forEach {
            board = board.changePiece(it)
        }
        ((initialBlackPieces + 1)..(initialBlackPieces + piecesToChangeFromWhiteToBlack)).forEach {
            board = board.changePiece(it)
        }
        val expectedBlackPieces = initialBlackPieces - piecesToChangeFromBlackToWhite + piecesToChangeFromWhiteToBlack
        val expectedWhitePieces = initialWhitePieces - piecesToChangeFromWhiteToBlack + piecesToChangeFromBlackToWhite

        assert(board.totalBlackPieces() == expectedBlackPieces)
        assert(board.totalWhitePieces() == expectedWhitePieces)
    }
}