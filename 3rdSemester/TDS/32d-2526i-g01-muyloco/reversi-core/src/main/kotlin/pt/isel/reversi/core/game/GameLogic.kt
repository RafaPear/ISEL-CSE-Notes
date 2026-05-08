package pt.isel.reversi.core.game

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.InvalidPlay
import pt.isel.reversi.utils.TRACKER

/**
 * Contains the core game logic for Reversi, including methods to play a piece,
 * check for valid moves, and find capturable pieces.
 */
internal object GameLogic {
    /**
     * Plays a piece on the board and returns the new state of the board.
     * The play is valid if it captures at least one of the opponent's pieces.
     * @param board The current state of the board.
     * @param myPiece The piece being placed on the board.
     * @return The new state of the board after the play.
     * @throws IllegalArgumentException if the position is out of bounds
     * @throws pt.isel.reversi.core.exceptions.InvalidPlay if the play is not valid (when no pieces are captured or position is occupied)
     */
    fun play(
        board: Board,
        myPiece: Piece,
    ): Board {
        TRACKER.trackFunctionCall(customName = "GameLogic.play", category = "Core.Logic")
        board.checkPosition(myPiece.coordinate)
        board[myPiece.coordinate]?.let {
            //if != null, position is occupied
            throw InvalidPlay(
                message = "Invalid play, position already occupied: $myPiece",
                type = ErrorType.INFO
            )
        }
        //Get all opponent pieces around (1 cell away in any direction) of myPiece
        val opponentPieces = findAround(board, myPiece, myPiece.value.swap())

        //For each opponent piece, get the direction from myPiece to opponentPiece,
        //and get all capturable pieces in that direction or empty list if none
        val capturablePieces = opponentPieces.flatMap { opponentPiece: Coordinate ->
            val direction = opponentPiece - myPiece.coordinate
            getCapturablePieces(board, myPiece, direction)
        }

        //If no capturable pieces, the play is invalid
        if (capturablePieces.isEmpty())
            throw InvalidPlay(
                message = "Invalid play: ${myPiece.coordinate.row} ${myPiece.coordinate.col}",
                type = ErrorType.INFO
            )

        //Create a new board with myPiece added and all capturable pieces swapped
        var newBoard: Board = board.changePiece(capturablePieces[0])
        capturablePieces.drop(n = 1).forEach { coordinate ->
            newBoard = newBoard.changePiece(coordinate)
        }

        return newBoard.addPiece(myPiece)
    }

    /**
     * Gets a list of available plays for the player with the specified piece type.
     * A play is considered available if placing a piece of the given type at that position
     * would result in capturing at least one of the opponent's pieces.
     * @param board The current state of the board.
     * @param myPieceType The type of piece for the player (e.g., BLACK or WHITE).
     * @return A list of coordinates where the player can place their piece.
     */
    fun getAvailablePlays(
        board: Board,
        myPieceType: PieceType,
    ): List<Coordinate> {
        TRACKER.trackFunctionCall(customName = "GameLogic.getAvailablePlays", category = "Core.Logic")

        val possibleMoves = board.flatMap { piece -> //get all possible moves around each enemy piece
            if (piece.value != myPieceType)//get empty spaces around opponent pieces
                findAround(board, piece, null)
            else emptyList()
        }.distinct() //remove duplicates

        //return only valid moves
        return possibleMoves.filter { coordinate ->
            isValidMove(board, myPiece = Piece(coordinate = coordinate, value = myPieceType))
        }
    }

    /**
     * Checks if placing a piece at the specified coordinates is a valid move.
     * A move is considered valid if it results in capturing at least one of the opponent's pieces.
     * @param board The current state of the board.
     * @param myPiece The piece being placed on the board.
     * @return True if the move is valid, false otherwise.
     * @throws IllegalArgumentException if the position is out of bounds
     */
    fun isValidMove(
        board: Board,
        myPiece: Piece,
    ): Boolean {
        board.checkPosition(myPiece.coordinate)
        board[myPiece.coordinate]?.let {
            return false //if != null, position is occupied
        }
        //get all opponent pieces around (1 cell away in any direction) of myPiece
        val opponentPiecesAround = findAround(board, myPiece, myPiece.value.swap())

        if (opponentPiecesAround.isNotEmpty()) {
            opponentPiecesAround.forEach { coordinate ->
                val direction = coordinate - myPiece.coordinate
                if (getCapturablePieces(board, myPiece, direction).isNotEmpty())
                    return true
            }
        }
        return false
    }

    /**
     * Finds all coordinates with 1 cell distance from the given piece that contain
     * the specified type of piece.
     * @param board The current state of the board.
     * @param myPiece The piece around which to search.
     * @param findThis The type of piece to search for. If null, it will find empty spaces.
     * @return A list of coordinates where the specified type of piece is found around the given piece.
     * @throws IllegalArgumentException if the position is out of bounds.
     */
    fun findAround(
        board: Board,
        myPiece: Piece,
        findThis: PieceType?
    ): List<Coordinate> {

        board.checkPosition(myPiece.coordinate)

        val coordinates = myPiece.coordinate
        // Check all 8 directions
        return buildList {
            Coordinate.Companion.allDirection.forEach { direction ->
                val newCord = coordinates + direction

                if (
                    newCord.isValid(board.side)
                    && board[newCord] == findThis
                ) {
                    add(newCord)
                }
            }
        }
    }

    /**
     * Gets a list of capturable pieces in a specified direction from the given piece.
     * A piece is considered capturable if it is of the opposite type and is followed
     * by a piece of the same type as `myPiece` in the specified direction.
     * @param board The current state of the board.
     * @param myPiece The piece being placed on the board.
     * @param direction The direction to check in (should be one of the 8 possible directions).
     * @return A list of coordinates of capturable pieces in the specified direction.
     * @throws IllegalArgumentException if the position is out of bounds
     */
    fun getCapturablePieces(
        board: Board,
        myPiece: Piece,
        direction: Coordinate
    ): List<Coordinate> {
        board.checkPosition(myPiece.coordinate)
        var newCord =
            if ((myPiece.coordinate + direction).isValid(board.side))
                myPiece.coordinate + direction
            else return emptyList()

        // Start checking from the next piece in the specified direction
        var nextPiece = Piece(
            newCord,
            board[newCord] ?: return emptyList()
        )
        val capturablePieces = mutableListOf<Coordinate>()

        // Continue in the direction while the pieces are of the opposite type
        while (nextPiece.value == myPiece.value.swap()) {
            capturablePieces += nextPiece.coordinate
            newCord = nextPiece.coordinate + direction

            //return empty if out of bounds
            if (!newCord.isValid(board.side)) return emptyList()

            nextPiece = Piece(
                newCord,
                // return empty if the next position is empty
                board[newCord] ?: return emptyList()
            )

        }
        return capturablePieces
    }
}