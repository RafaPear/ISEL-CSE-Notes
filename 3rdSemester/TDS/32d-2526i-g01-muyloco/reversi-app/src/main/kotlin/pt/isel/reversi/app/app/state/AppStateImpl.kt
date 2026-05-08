package pt.isel.reversi.app.app.state

import pt.isel.reversi.app.app.AppTheme
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.GameServiceImpl
import pt.isel.reversi.utils.audio.AudioPool

/**
 * Interface defining the contract for application state implementations.
 * Provides read-only access to game session data, page navigation state, audio/theme settings,
 * and game services.
 */
interface AppStateImpl {
    /** Current game session containing game state and player information. */
    val gameSession: GameSession

    /** Current page and navigation state. */
    val pagesState: PagesState

    /** Game service for storage and persistence operations. */
    val service: GameServiceImpl

    /** Audio and theme configuration state. */
    val audioThemeState: AudioThemeState

    /** Convenience accessor for the current game instance. */
    val game: Game get() = this.gameSession.game

    /** Convenience accessor for the current player's name. */
    val playerName: String? get() = this.gameSession.playerName

    /** Convenience accessor for the audio pool. */
    val audioPool: AudioPool get() = this.audioThemeState.audioPool

    /** Convenience accessor for the current theme. */
    val theme: AppTheme get() = this.audioThemeState.theme

    /** Convenience accessor for any global error state. */
    val globalError: ReversiException? get() = this.pagesState.globalError
}