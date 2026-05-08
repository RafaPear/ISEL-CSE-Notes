package pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.drawCard

import androidx.compose.ui.graphics.Color
import pt.isel.reversi.app.pages.lobby.LobbyLoadedState

/**
 * Enumeration of possible status states for game cards in the lobby carousel.
 * Each status has a display label and associated color for visual indication.
 *
 * @property text The user-facing status label.
 * @property color The color associated with the status.
 */
enum class CardStatus(val text: String, val color: Color) {
    /** Game is empty with no players. */
    EMPTY("Vazio", Color.Green),

    /** Game is waiting for additional players to join. */
    WAITING_FOR_PLAYERS("Aguardando Jogadores", Color.Yellow),

    /** Game is full with all players present. */
    FULL("Cheio", Color.Blue),

    /** Game file is corrupted or invalid. */
    CORRUPTED("Corrompido", Color.Red),

    /** Game is currently being played. */
    CURRENT_GAME("Jogo Atual", Color.Cyan)
}

/**
 * Determines the status of a game based on its state and comparison with the current game.
 *
 * @param game The game to evaluate.
 * @param currentGameName The name of the currently active game.
 * @return The CardStatus representing the game's state.
 */
fun getCardStatus(game: LobbyLoadedState, currentGameName: String?): CardStatus {
    val gameState = game.gameState

    return when {
        currentGameName == game.name ->
            CardStatus.CURRENT_GAME

        gameState.players.isFull() -> CardStatus.FULL
        gameState.players.isEmpty() -> CardStatus.EMPTY
        gameState.players.isNotEmpty() -> CardStatus.WAITING_FOR_PLAYERS
        else -> CardStatus.CORRUPTED
    }
}