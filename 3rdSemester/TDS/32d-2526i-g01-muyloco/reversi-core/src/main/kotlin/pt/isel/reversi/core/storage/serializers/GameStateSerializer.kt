package pt.isel.reversi.core.storage.serializers

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.InvalidGameStateInFile
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.storage.Serializer

/**
 * Serializer for the GameState class, converting it to and from a String representation.
 */
internal class GameStateSerializer : Serializer<GameState, String> {
    private val pieceTypeSerializer = PieceTypeSerializer()
    private val boardSerializer = BoardSerializer()
    private val playerSerializer = PlayerSerializer()

    private val playersLine = 0
    private val lastPlayerLine = playersLine + 1
    private val winnerLine = lastPlayerLine + 1
    private val boardStartLine = winnerLine + 1

    override fun serialize(obj: GameState): String {
        requireNotNull(obj.lastPlayer) { "lastPlayer cannot be null" }
        requireNotNull(obj.board) { "board cannot be null" }

        val sb = StringBuilder()

        for (player in obj.players) {
            sb.append(playerSerializer.serialize(player))
            sb.append(";")
        }
        sb.appendLine()


        sb.appendLine(pieceTypeSerializer.serialize(obj.lastPlayer))

        if (obj.winner == null) sb.appendLine()
        else sb.appendLine(playerSerializer.serialize(obj.winner))

        sb.append(boardSerializer.serialize(obj.board))

        return sb.toString()
    }

    private fun getPlayers(parts: List<String>): MatchPlayers {
        if (parts.size + 1 < playersLine) return MatchPlayers()
        val playersLineContent = parts[playersLine]
        if (playersLineContent.isBlank() || playersLineContent.first().isWhitespace()) return MatchPlayers()

        val playerStrings = playersLineContent.split(";")
        var players = MatchPlayers()

        for (player in playerStrings) {
            if (player.isNotBlank()) {
                val newPlayers =
                    players.addPlayerOrNull(playerSerializer.deserialize(player)) ?: players
                players = newPlayers
            }
        }
        return players
    }

    private fun getLastPlayerPart(parts: List<String>): PieceType {
        val firstLine = parts[lastPlayerLine]
        return pieceTypeSerializer.deserialize(firstLine.first())
    }

    private fun getWinnerPart(parts: List<String>): Player? {
        val winnerLineContent = parts[winnerLine]
        if (winnerLineContent.isBlank()) return null
        return playerSerializer.deserialize(winnerLineContent)
    }

    private fun getBoardPart(parts: List<String>): Board {
        val boardPart = parts.drop(boardStartLine).joinToString("\n")
        return boardSerializer.deserialize(boardPart)
    }

    override fun deserialize(obj: String): GameState {
        try {
            val parts = obj.split("\n")

            val players = getPlayers(parts)
            val lastPlayer = getLastPlayerPart(parts)
            val winner = getWinnerPart(parts)
            val board = getBoardPart(parts)

            return GameState(
                players = players,
                lastPlayer = lastPlayer,
                board = board,
                winner = winner
            )
        } catch (e: Exception) {
            throw InvalidGameStateInFile(
                message = "Invalid game state data. Error: ${e.message}",
                type = ErrorType.ERROR
            )
        }
    }
}