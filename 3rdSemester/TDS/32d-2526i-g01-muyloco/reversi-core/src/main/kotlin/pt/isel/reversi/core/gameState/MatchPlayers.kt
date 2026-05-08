package pt.isel.reversi.core.gameState

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType

/**
 * Represents the players in a Reversi match, supporting zero to two players.
 *
 * @property player1 The first player, or null if not yet assigned.
 * @property player2 The second player, or null if not yet assigned.
 */
data class MatchPlayers(val player1: Player? = null, val player2: Player? = null) : Iterable<Player> {
    init {
        if (player1 != null && player2 != null) {
            require(player1.type != player2.type) { "Players must have different piece types" }
        }
    }

    /**
     * Checks if no players have been assigned.
     * @return True if both player slots are empty.
     */
    fun isEmpty(): Boolean = player1 == null && player2 == null

    /**
     * Checks if at least one player has been assigned.
     * @return True if at least one player slot is filled.
     */
    fun isNotEmpty(): Boolean = !isEmpty()

    /**
     * Checks if both player slots are filled.
     * @return True if both players are assigned.
     */
    fun isFull(): Boolean = player1 != null && player2 != null

    /**
     * Checks if exactly one player has been assigned.
     * @return True if exactly one player slot is filled.
     */
    fun hasOnlyOnePlayer(): Boolean = (player1 == null) != (player2 == null)

    /**
     * Gets a player by their piece type.
     * @param type The piece type to search for.
     * @return The player with the specified piece type, or null if not found.
     */
    fun getPlayerByType(type: PieceType): Player? =
        this.firstOrNull { it.type == type }

    /**
     * Returns a list of available piece types that can be assigned to new players.
     * If both players are already assigned, the list will be empty.
     * @return A list of available PieceType values.
     */
    fun getAvailableTypes(): List<PieceType> {
        val availableTypes = mutableListOf<PieceType>()
        if (player1?.type != PieceType.BLACK && player2?.type != PieceType.BLACK) {
            availableTypes.add(PieceType.BLACK)
        }
        if (player1?.type != PieceType.WHITE && player2?.type != PieceType.WHITE) {
            availableTypes.add(PieceType.WHITE)
        }
        return availableTypes
    }

    /**
     * Refreshes both players' point counts based on the current board state.
     * @param board The current game board.
     * @return A new MatchPlayers with updated player points.
     */
    fun refreshPlayers(board: Board): MatchPlayers {
        val refreshedPlayer1 = player1?.refresh(board)
        val refreshedPlayer2 = player2?.refresh(board)
        return MatchPlayers(refreshedPlayer1, refreshedPlayer2)
    }

    /**
     * Gets the next available piece type that has not yet been assigned.
     * @return The next free PieceType, or null if both types are already assigned.
     */
    fun getFreeType(): PieceType? =
        when {
            player1 == null && player2 == null -> PieceType.BLACK
            player1 != null && player2 == null -> player1.type.swap()
            player1 == null && player2 != null -> player2.type.swap()
            else -> null
        }

    /**
     * Adds a new player to the match if there is space available and the player type is not already taken.
     * @param newPlayerName The player to add.
     * @return A new MatchPlayers with the added player, or null if the player could not be added.
     */
    fun addPlayerOrNull(newPlayerName: Player) = when {
        this.isFull() -> null
        this.getPlayerByType(newPlayerName.type) != null -> null
        this.player1 == null -> MatchPlayers(newPlayerName, this.player2)
        this.player2 == null -> MatchPlayers(this.player1, newPlayerName)
        else -> null
    }

    /**
     * Gets a player by index (0 for player1, 1 for player2).
     * @param index The index of the player (0 or 1).
     * @return The player at the specified index, or null if not found.
     */
    operator fun get(index: Int): Player? =
        when (index) {
            0 -> player1
            1 -> player2
            else -> null
        }

    override fun iterator(): Iterator<Player> {
        val playersList = listOfNotNull(player1, player2)
        return playersList.iterator()
    }
}