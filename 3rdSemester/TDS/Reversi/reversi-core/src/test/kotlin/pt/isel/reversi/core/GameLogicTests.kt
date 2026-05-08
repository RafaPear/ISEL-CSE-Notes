package pt.isel.reversi.core

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType.BLACK
import pt.isel.reversi.core.board.PieceType.WHITE
import pt.isel.reversi.core.exceptions.InvalidPlayException
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GameLogicTests {
    @Test
    fun `getCapturablePieces should return empty list when no capturable pieces are found`() {
        var board = Board(4).addPiece(Coordinate(1, 1), BLACK)
        repeat(3) {
            board = board.addPiece(Coordinate(1, it + 2), WHITE)
        }
        val uut = GameLogic.getCapturablePieces(
            board,
            Piece(Coordinate(1, 1), BLACK),
            Coordinate(0, 1)
        )
        assert(uut.isEmpty())

        board = board.addPiece(Coordinate(2, 1), BLACK)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(2, 4), BLACK)

        val uut2 = GameLogic.getCapturablePieces(
            board,
            Piece(Coordinate(2, 1), BLACK),
            Coordinate(0, 1)
        )
        assert(uut2.isEmpty())

        board = board.addPiece(Coordinate(3, 1), BLACK)
        board = board.addPiece(Coordinate(3, 2), BLACK)
        board = board.addPiece(Coordinate(3, 3), WHITE)
        board = board.addPiece(Coordinate(3, 4), BLACK)

        val uut3 = GameLogic.getCapturablePieces(
            board,
            Piece(Coordinate(3, 1), BLACK),
            Coordinate(0, 1)
        )
        assert(uut2.isEmpty())

        val uut4 = GameLogic.getCapturablePieces(
            board,
            Piece(Coordinate(1, 1), BLACK),
            Coordinate(1, 1)
        )

        assert(uut4.isEmpty())
    }

    @Test
            /*
                  1 2 3 4
                1 B W W B
                2 . W . .
                3 . . W .
                4 . . . B
             */
    fun `getCapturablePieces should return list of capturable pieces when found`() {
        val coordinate = Coordinate(1, 1)
        var board = Board(4).addPiece(coordinate, BLACK)

        board = board.addPiece(Coordinate(1, 2), WHITE)
        board = board.addPiece(Coordinate(1, 3), WHITE)
        board = board.addPiece(Coordinate(1, 4), BLACK)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(3, 3), WHITE)
        board = board.addPiece(Coordinate(4, 4), BLACK)

        val uut = GameLogic.getCapturablePieces(
            board,
            Piece(coordinate, BLACK),
            Coordinate(1, 1)
        )
        assert(uut.size == 2)
        assert(
            uut.containsAll(
                listOf(
                    Coordinate(3, 3),
                    Coordinate(2, 2),
                )
            )
        )

        val uut2 = GameLogic.getCapturablePieces(
            board,
            Piece(coordinate, BLACK),
            Coordinate(0, 1)
        )
        assert(uut2.size == 2)
        assert(
            uut2.containsAll(
                listOf(
                    Coordinate(1, 2),
                    Coordinate(1, 3)
                )
            )
        )
    }

    @Test
    fun `getCapturablePieces when myPiece does not exist`() {
        var board = Board(4)

        board = board.addPiece(Coordinate(1, 1), WHITE)
        board = board.addPiece(Coordinate(1, 2), BLACK)

        assertFailsWith<IllegalArgumentException> {
            val uut = GameLogic.getCapturablePieces(
                board,
                Piece(Coordinate(0, 0), BLACK),
                Coordinate(0, 1)
            )
        }
    }

    @Test
    fun `getCapturablePieces when myPiece is out of bounds`() {
        val board = Board(4)
        assertFailsWith<IllegalArgumentException> {
            GameLogic.getCapturablePieces(board, Piece(Coordinate(0, 5), BLACK), Coordinate(0, 1))
        }
    }

    @Test
    fun `getCapturablePieces when my piece is at the edge and direction is out of bounds`() {
        var board = Board(4).addPiece(Coordinate(1, 1), BLACK)
        board = board.addPiece(Coordinate(1, 2), WHITE)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(3, 3), WHITE)
        board = board.addPiece(Coordinate(4, 4), BLACK)

        val uut = GameLogic.getCapturablePieces(
            board,
            Piece(Coordinate(1, 1), BLACK),
            Coordinate(-1, -1)
        )
        assert(uut.isEmpty())
    }

    @Test
            /*
                  1 2 3 4
                1 B W W B
                2 . W . .
                3 . . B W
                4 . . B B
             */
    fun `findAround should return list of coordinates around myPiece that contain findThis`() {
        val coordinate = Coordinate(3, 3)
        var board = Board(4).addPiece(coordinate, BLACK)

        board = board.addPiece(Coordinate(1, 1), BLACK)
        board = board.addPiece(Coordinate(1, 2), WHITE)
        board = board.addPiece(Coordinate(1, 3), WHITE)
        board = board.addPiece(Coordinate(1, 4), BLACK)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(3, 4), WHITE)
        board = board.addPiece(Coordinate(4, 3), BLACK)
        board = board.addPiece(Coordinate(4, 4), BLACK)

        val uut = GameLogic.findAround(
            board,
            Piece(coordinate, BLACK),
            WHITE
        )
        assert(uut.size == 2)
        assert(
            uut.containsAll(
                listOf(
                    Coordinate(2, 2),
                    Coordinate(3, 4),
                )
            )
        )

        val uut2 = GameLogic.findAround(
            board,
            Piece(coordinate, BLACK),
            null
        )
        assert(uut2.size == 4)
        assert(
            uut2.containsAll(
                listOf(
                    Coordinate(2, 3),
                    Coordinate(4, 2),
                    Coordinate(2, 4),
                    Coordinate(3, 2)
                )
            )
        )
    }

    @Test
    fun `findAround when myPiece is out of bounds`() {
        val board = Board(4)
        assertFailsWith<IllegalArgumentException> {
            GameLogic.findAround(board, Piece(Coordinate(0, 5), BLACK), WHITE)
        }
    }

    @Test
            /*
                  1 2 3 4
                1 B W W .
                2 . W . W
                3 . . B W
                4 . . B B
             */

    fun `isValidMove should return true when there are capturable pieces`() {
        val coordinate = Coordinate(3, 3)
        var board = Board(4).addPiece(coordinate, BLACK)

        board = board.addPiece(Coordinate(1, 1), BLACK)
        board = board.addPiece(Coordinate(1, 2), WHITE)
        board = board.addPiece(Coordinate(1, 3), WHITE)
        board = board.addPiece(Coordinate(2, 4), WHITE)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(3, 4), WHITE)
        board = board.addPiece(Coordinate(4, 3), BLACK)
        board = board.addPiece(Coordinate(4, 4), BLACK)

        val piece = Piece(Coordinate(1, 4), BLACK)
        val uut = GameLogic.isValidMove(board, piece)

        assert(uut)
    }

    @Test
            /*
                1 2 3 4
              1 B W W .
              2 . W . W
              3 . . B W
              4 . . B B
           */
    fun `isValidMove should return false when there are no capturable pieces`() {
        val coordinate = Coordinate(3, 3)
        var board = Board(4).addPiece(coordinate, BLACK)

        board = board.addPiece(Coordinate(1, 1), BLACK)
        board = board.addPiece(Coordinate(1, 2), WHITE)
        board = board.addPiece(Coordinate(1, 3), WHITE)
        board = board.addPiece(Coordinate(2, 4), WHITE)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(3, 4), WHITE)
        board = board.addPiece(Coordinate(4, 3), BLACK)
        board = board.addPiece(Coordinate(4, 4), BLACK)

        val piece = Piece(Coordinate(1, 4), WHITE)
        val uut = GameLogic.isValidMove(board, piece)

        assert(!uut)
    }

    @Test
            /*
                  1 2 3 4
                1 B W B .
                2 . W . W
                3 . . B W
                4 . . B B
             */
    fun `isValidMove should return false when myPiece have occupied position`() {
        val coordinate = Coordinate(1, 4)
        var board = Board(4).addPiece(Coordinate(3, 3), BLACK)

        board = board.addPiece(Coordinate(1, 1), BLACK)
        board = board.addPiece(Coordinate(1, 2), WHITE)
        board = board.addPiece(Coordinate(1, 3), BLACK)
        board = board.addPiece(Coordinate(2, 4), WHITE)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(3, 4), WHITE)
        board = board.addPiece(Coordinate(4, 3), BLACK)
        board = board.addPiece(Coordinate(4, 4), BLACK)
        board = board.addPiece(Coordinate(1, 4), BLACK)

        val uut = GameLogic.isValidMove(board, Piece(coordinate, BLACK))
        assert(!uut)
        val uut2 = GameLogic.isValidMove(board, Piece(coordinate, WHITE))
        assert(!uut2)
    }

    @Test
            /*
                1 2 3 4
              1 B W W .
              2 . W . W
              3 . . B W
              4 . . B B
           */
    fun `isValidMove when myPiece is out of bounds`() {
        val board = Board(4)
        assertFailsWith<IllegalArgumentException> {
            GameLogic.isValidMove(board, Piece(Coordinate(10, 10), BLACK))
        }
    }

    @Test
    fun `isValidMove should return false when no pieces around`() {
        val board = Board(4)

        val uut = GameLogic.isValidMove(board, Piece(Coordinate(2, 2), BLACK))

        assert(!uut)
    }

    @Test
            /*
              1 2 3 4
            1 . W . .
            2 . W . W
            3 . . . .
            4 . . B B
           */
    fun `getAvailablePlays should return empty list when no moves are possible`() {
        var board = Board(4).addPiece(Coordinate(1, 2), WHITE)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(2, 4), WHITE)
        board = board.addPiece(Coordinate(4, 3), BLACK)
        board = board.addPiece(Coordinate(4, 4), BLACK)

        val uut = GameLogic.getAvailablePlays(board, WHITE)
        assert(uut == emptyList<Coordinate>())
        val uut2 = GameLogic.getAvailablePlays(board, BLACK)
        assert(uut2 == emptyList<Coordinate>())
    }

    @Test
            /*
              1 2 3 4
            1 B W . .
            2 . W . W
            3 . B . .
            4 . . B B
           */
    fun `getAvailablePlays should return correct coordinates for both players`() {
        var board = Board(4).addPiece(Coordinate(1, 2), WHITE)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(2, 4), WHITE)
        board = board.addPiece(Coordinate(4, 3), BLACK)
        board = board.addPiece(Coordinate(4, 4), BLACK)
        board = board.addPiece(Coordinate(1, 1), BLACK)
        board = board.addPiece(Coordinate(3, 2), BLACK)

        val expectedMyPieceIsBlack = listOf(
            Coordinate(1, 3),
            Coordinate(3, 3)
        )
        val expectedMyPieceIsWhite = listOf(
            Coordinate(4, 2)
        )

        val uut = GameLogic.getAvailablePlays(board, BLACK)
        assert(uut == expectedMyPieceIsBlack)
        val uut2 = GameLogic.getAvailablePlays(board, WHITE)
        assert(uut2 == expectedMyPieceIsWhite)
    }

    @Test
            /*
              1 2 3 4
            1 B W . .
            2 . W . W
            3 . B . .
            4 . . B B
           */
    fun `play should throw InvalidPlay exception when move is invalid`() {
        var board = Board(4).addPiece(Coordinate(1, 2), WHITE)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(2, 4), WHITE)
        board = board.addPiece(Coordinate(4, 3), BLACK)
        board = board.addPiece(Coordinate(4, 4), BLACK)
        board = board.addPiece(Coordinate(1, 1), BLACK)
        board = board.addPiece(Coordinate(3, 2), BLACK)


        assertFailsWith<InvalidPlayException> {
            val uut = GameLogic.play(board, Piece(Coordinate(3, 3), WHITE))
            val uut2 = GameLogic.play(board, Piece(Coordinate(1, 4), BLACK))
        }
    }

    @Test
            /*
             1 2 3 4
           1 . W W B
           2 . W . W
           3 . B B .
           4 . . B B
          */
    fun `play should throw InvalidPlay exception when position is already occupied`() {
        var board = Board(4).addPiece(Coordinate(1, 2), WHITE)
        board = board.addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(2, 4), WHITE)
        board = board.addPiece(Coordinate(4, 3), BLACK)
        board = board.addPiece(Coordinate(4, 4), BLACK)
        board = board.addPiece(Coordinate(1, 3), WHITE)
        board = board.addPiece(Coordinate(3, 2), BLACK)
        board = board.addPiece(Coordinate(1, 4), BLACK)
        board = board.addPiece(Coordinate(3, 3), BLACK)

        val myPiece = Piece(Coordinate(1, 1), BLACK)

        board = board.addPiece(myPiece.coordinate, myPiece.value)

        assertFailsWith<InvalidPlayException> {
            val uut = GameLogic.play(board, myPiece)
        }
    }

    @Test
            /*
              1 2 3 4
            1 . . . .
            2 . W W B
            3 . W W .
            4 . . B .
           */
    fun `play with valid move updates sequence of pieces correctly`() {
        val myPiece = Piece(Coordinate(2, 1), BLACK)
        val correctSequence = listOf(
            Coordinate(2, 2),
            Coordinate(2, 3),
            Coordinate(3, 2),
            Coordinate(3, 3),
            Coordinate(2, 4),
            Coordinate(4, 3),
            myPiece.coordinate,
        )

        var board = Board(4)

        (0..3).forEach {
            board = board.addPiece(correctSequence[it], WHITE)
        }
        (4..5).forEach {
            board = board.addPiece(correctSequence[it], BLACK)
        }

        val uut = GameLogic.play(board, myPiece)

        uut.forEachIndexed { idx, piece ->

            assert(piece.coordinate == correctSequence[idx])
        }
    }

    @Test
            /*
              1 2 3 4
            1 . W W B
            2 . W . W
            3 . B B .
            4 . . B B
           */
    fun `play should return updated board when move is valid`() {
        var board = Board(4).addPiece(Coordinate(2, 2), WHITE)
        board = board.addPiece(Coordinate(2, 4), WHITE)
        board = board.addPiece(Coordinate(4, 3), BLACK)
        board = board.addPiece(Coordinate(4, 4), BLACK)
        board = board.addPiece(Coordinate(1, 3), WHITE)
        board = board.addPiece(Coordinate(3, 2), BLACK)
        board = board.addPiece(Coordinate(1, 4), BLACK)
        board = board.addPiece(Coordinate(3, 3), BLACK)
        board = board.addPiece(Coordinate(1, 2), WHITE)

        val myPiece = Piece(Coordinate(1, 1), BLACK)

        var expectedBoard = board.changePiece(Coordinate(1, 2))
        expectedBoard = expectedBoard.changePiece(Coordinate(1, 3))
        expectedBoard = expectedBoard.changePiece(Coordinate(2, 2))
        expectedBoard = expectedBoard.addPiece(myPiece.coordinate, myPiece.value)

        val uut = GameLogic.play(board, myPiece)

        assert(uut == expectedBoard)
    }
}