package pt.isel.reversi.core.gameState

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.PieceType
import kotlin.test.Test
import kotlin.test.assertEquals

class GameStateTests {
    @Test
    fun `GameState changeName updates the correct player's name`() {
        val player1 = Player(type = PieceType.BLACK, name = "Alice")
        val player2 = Player(type = PieceType.WHITE, name = "Bob")
        val expectedPlayer1 = player1.copy(name = "Charlie")
        val expectedPlayer2 = player2.copy(name = "Diana")

        val initialPlayers = MatchPlayers(
            player1 = player1,
            player2 = player2
        )


        val gameState = GameState(
            players = initialPlayers,
            lastPlayer = PieceType.BLACK,
            board = Board(4).startPieces()
        )

        val updatedGameStateBlack = gameState.changeName(
            newName = expectedPlayer1.name,
            pieceType = expectedPlayer1.type
        )

        assertEquals(expectedPlayer1, updatedGameStateBlack.players.player1)
        assertEquals(player2, updatedGameStateBlack.players.player2)

        val updatedGameStateWhite = gameState.changeName(
            newName = expectedPlayer2.name,
            pieceType = expectedPlayer2.type
        )

        assertEquals(expectedPlayer2, updatedGameStateWhite.players.player2)
        assertEquals(player1, updatedGameStateWhite.players.player1)
    }

    @Test
    fun `GameState changeName with non-existing piece type does not change players`() {
        val player1 = Player(type = PieceType.BLACK, name = "Alice")

        val initialPlayers = MatchPlayers(
            player1 = player1,
        )

        val gameState = GameState(
            players = initialPlayers,
            lastPlayer = PieceType.BLACK,
            board = Board(4).startPieces()
        )

        val updatedGameState = gameState.changeName(
            newName = "Charlie",
            pieceType = PieceType.WHITE // Non-existing piece type in this context
        )

        assertEquals(initialPlayers, updatedGameState.players)
    }

    @Test
    fun `GameState refreshPlayers updates players based on current board state`() {
        val player1 = Player(type = PieceType.BLACK, name = "Alice", points = 0)
        val player2 = Player(type = PieceType.WHITE, name = "Bob", points = 0)

        val initialPlayers = MatchPlayers(
            player1 = player1,
            player2 = player2
        )

        val board = Board(4)
            .addPiece(Coordinate(1, 1), PieceType.BLACK)
            .addPiece(Coordinate(1, 2), PieceType.BLACK)
            .addPiece(Coordinate(2, 1), PieceType.WHITE)

        val gameState = GameState(
            players = initialPlayers,
            lastPlayer = PieceType.BLACK,
            board = board
        )

        val refreshedGameState = gameState.refreshPlayers()

        val expectedPlayer1Points = 2 // BLACK pieces
        val expectedPlayer2Points = 1 // WHITE pieces

        assertEquals(expectedPlayer1Points, refreshedGameState.players.player1?.points)
        assertEquals(expectedPlayer2Points, refreshedGameState.players.player2?.points)
    }
}