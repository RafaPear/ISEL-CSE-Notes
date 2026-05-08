package pt.isel.reversi.core.gameState

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import kotlin.test.Test
import kotlin.test.assertFailsWith

class MatchPlayersTests {
    @Test
    fun `test that creating MatchPlayers with same piece types throws exception`() {
        val player1 = Player(name = "Alice", type = PieceType.BLACK)
        val player2 = Player(name = "Bob", type = PieceType.BLACK)

        assertFailsWith<IllegalArgumentException> {
            MatchPlayers(player1, player2)
        }
    }

    @Test
    fun `test isEmpty method`() {
        val emptyPlayers = MatchPlayers()
        val onePlayer = MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), null)
        val fullPlayers =
            MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), Player(name = "Bob", type = PieceType.WHITE))

        assert(emptyPlayers.isEmpty())
        assert(!onePlayer.isEmpty())
        assert(!fullPlayers.isEmpty())
    }

    @Test
    fun `test isFull method`() {
        val emptyPlayers = MatchPlayers()
        val onePlayer = MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), null)
        val fullPlayers =
            MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), Player(name = "Bob", type = PieceType.WHITE))

        assert(!emptyPlayers.isFull())
        assert(!onePlayer.isFull())
        assert(fullPlayers.isFull())
    }

    @Test
    fun `test hasOnlyOnePlayer method`() {
        val emptyPlayers = MatchPlayers()
        val onePlayer = MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), null)
        val fullPlayers =
            MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), Player(name = "Bob", type = PieceType.WHITE))

        assert(!emptyPlayers.hasOnlyOnePlayer())
        assert(onePlayer.hasOnlyOnePlayer())
        assert(!fullPlayers.hasOnlyOnePlayer())
    }

    @Test
    fun `test getPlayerByType method`() {
        val player1 = Player(name = "Alice", type = PieceType.BLACK)
        val player2 = Player(name = "Bob", type = PieceType.WHITE)
        val matchPlayers = MatchPlayers(player2, player1)

        val retrievedPlayer1 = matchPlayers.getPlayerByType(PieceType.BLACK)
        val retrievedPlayer2 = matchPlayers.getPlayerByType(PieceType.WHITE)

        assert(retrievedPlayer1 == player1)
        assert(retrievedPlayer2 == player2)
    }

    @Test
    fun `test getPlayerByType returns null for non-existing type`() {
        val player1 = Player(name = "Alice", type = PieceType.BLACK)
        val matchPlayers = MatchPlayers(player1, null)

        val retrievedPlayer = matchPlayers.getPlayerByType(PieceType.WHITE)
        assert(retrievedPlayer == null)
    }

    @Test
    fun `test refreshPlayers method`() {
        val player1 = Player(name = "Alice", type = PieceType.BLACK)
        val player2 = Player(name = "Bob", type = PieceType.WHITE)
        val matchPlayers = MatchPlayers(player1, player2)

        val board = Board(4).startPieces()
        val expectedBlackPieces = board.totalBlackPieces
        val expectedWhitePieces = board.totalWhitePieces

        val refreshedPlayers = matchPlayers.refreshPlayers(board)

        assert(refreshedPlayers.player1?.points == expectedBlackPieces)
        assert(refreshedPlayers.player2?.points == expectedWhitePieces)
    }

    @Test
    fun `test refreshPlayers with null players`() {
        val matchPlayers = MatchPlayers(null, null)

        val board = Board(4).startPieces()
        val refreshedPlayers = matchPlayers.refreshPlayers(board)

        assert(refreshedPlayers.player1 == null)
        assert(refreshedPlayers.player2 == null)
    }

    @Test
    fun `test refreshPlayers with one null player`() {
        val player1 = Player(name = "Alice", type = PieceType.BLACK)
        val matchPlayers = MatchPlayers(player1, null)

        val board = Board(4).startPieces()
        val expectedBlackPieces = board.totalBlackPieces

        val refreshedPlayers = matchPlayers.refreshPlayers(board)

        assert(refreshedPlayers.player1?.points == expectedBlackPieces)
        assert(refreshedPlayers.player2 == null)
    }

    @Test
    fun `test isNotEmpty method`() {
        val emptyPlayers = MatchPlayers()
        val onePlayer = MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), null)
        val fullPlayers =
            MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), Player(name = "Bob", type = PieceType.WHITE))

        assert(!emptyPlayers.isNotEmpty())
        assert(onePlayer.isNotEmpty())
        assert(fullPlayers.isNotEmpty())
    }

    @Test
    fun `test getFreeType method`() {
        val emptyPlayers = MatchPlayers()
        val onePlayerBlack = MatchPlayers(null, Player(name = "Alice", type = PieceType.BLACK))
        val onePlayerWhite = MatchPlayers(null, Player(name = "Bob", type = PieceType.WHITE))
        val fullPlayers =
            MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), Player(name = "Bob", type = PieceType.WHITE))

        assert(emptyPlayers.getFreeType() == PieceType.BLACK)
        assert(onePlayerBlack.getFreeType() == PieceType.WHITE)
        assert(onePlayerWhite.getFreeType() == PieceType.BLACK)
        assert(fullPlayers.getFreeType() == null)
    }

    @Test
    fun `test addPlayerOrNull when full returns null`() {
        val fullPlayers =
            MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), Player(name = "Bob", type = PieceType.WHITE))
        val newPlayer = Player(name = "Charlie", type = PieceType.BLACK)

        val result = fullPlayers.addPlayerOrNull(newPlayer)
        assert(result == null)
    }

    @Test
    fun `test addPlayerOrNull with mismatched type returns null`() {
        val onePlayerBlack = MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), null)
        val newPlayer = Player(name = "Bob", type = PieceType.BLACK)

        val result = onePlayerBlack.addPlayerOrNull(newPlayer)
        assert(result == null)
    }

    @Test
    fun `test addPlayerOrNull adds player with one slot available returns updated MatchPlayers`() {
        val onePlayerBlack = MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), null)
        val newPlayer = Player(name = "Bob", type = PieceType.WHITE)

        val result = onePlayerBlack.addPlayerOrNull(newPlayer)
        assert(result != null)
        assert(result!!.player1 == onePlayerBlack.player1)
        assert(result.player2 == newPlayer)
    }

    @Test
    fun `test addPlayerOrNull adds player to empty MatchPlayers returns updated MatchPlayers`() {
        val emptyPlayers = MatchPlayers()
        val newPlayer = Player(name = "Alice", type = PieceType.BLACK)
        val result = emptyPlayers.addPlayerOrNull(newPlayer)

        assert(result != null)
        assert(result!!.player1 == newPlayer)
        assert(result.player2 == null)

        val newPlayer2 = Player(name = "Bob", type = PieceType.WHITE)
        val result2 = result.addPlayerOrNull(newPlayer2)

        assert(result2 != null)
        assert(result2!!.player1 == newPlayer)
        assert(result2.player2 == newPlayer2)
    }

    @Test
    fun `test iterator method`() {
        val player1 = Player(name = "Alice", type = PieceType.BLACK)
        val player2 = Player(name = "Bob", type = PieceType.WHITE)
        val uut = MatchPlayers(player1, player2)

        uut.forEach {
            assert(it == player1 || it == player2)
        }
    }

    @Test
    fun `test addPlayerOrNull adds player when both slots are empty`() {
        val emptyPlayers = MatchPlayers()
        val newPlayerBlack = Player(name = "Alice", type = PieceType.BLACK)

        val result = emptyPlayers.addPlayerOrNull(newPlayerBlack)
        assert(result != null)
        assert(result!!.player1 == newPlayerBlack)
        assert(result.player2 == null)

        val newPlayerWhite = Player(name = "Bob", type = PieceType.WHITE)
        val result2 = emptyPlayers.addPlayerOrNull(newPlayerWhite)
        assert(result2 != null)
        assert(result2!!.player1 == newPlayerWhite)
        assert(result2.player2 == null)
    }

    @Test
    fun `test getAvailable players with empty match players (all available)`() {
        val emptyPlayers = MatchPlayers()
        val availableTypesEmpty = emptyPlayers.getAvailableTypes()
        assert(availableTypesEmpty.contains(PieceType.BLACK))
    }

    @Test
    fun `test getAvailable players with first player black (one white available)`() {
        val onePlayerBlack = MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), null)
        val availableTypesOneBlack = onePlayerBlack.getAvailableTypes()
        assert(availableTypesOneBlack.size == 1)
        assert(availableTypesOneBlack.contains(PieceType.WHITE))
    }

    @Test
    fun `test getAvailable players with first player white (one black available)`() {
        val onePlayerWhite = MatchPlayers(Player(name = "Bob", type = PieceType.WHITE), null)
        val availableTypesOneWhite = onePlayerWhite.getAvailableTypes()
        assert(availableTypesOneWhite.size == 1)
        assert(availableTypesOneWhite.contains(PieceType.BLACK))
    }

    @Test
    fun `test getAvailable players with both players present first black second white (none available)`() {
        val fullPlayers =
            MatchPlayers(Player(name = "Alice", type = PieceType.BLACK), Player(name = "Bob", type = PieceType.WHITE))
        val availableTypesFull = fullPlayers.getAvailableTypes()
        assert(availableTypesFull.isEmpty())
    }

    @Test
    fun `test getAvailable players with both players present first white second black (none available)`() {
        val fullPlayers =
            MatchPlayers(Player(name = "Bob", type = PieceType.WHITE), Player(name = "Alice", type = PieceType.BLACK))
        val availableTypesFull = fullPlayers.getAvailableTypes()
        assert(availableTypesFull.isEmpty())
    }
}