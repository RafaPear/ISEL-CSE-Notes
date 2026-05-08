package pt.isel.reversi.app.app.state

import pt.isel.reversi.app.app.AppTheme
import pt.isel.reversi.app.app.AppThemes
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.core.exceptions.ReversiException
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.GameServiceImpl
import pt.isel.reversi.utils.audio.AudioPool

/**
 * Represents the overall state of the Reversi application.
 * @property gameSession The current game session including game state and player identity.
 * @property pagesState The current page navigation state and any global errors.
 * @property audioThemeState The audio and theme configuration for the application.
 * @property service The game service used for game operations.
 */
data class AppState(
    override val gameSession: GameSession,
    override val pagesState: PagesState,
    override val audioThemeState: AudioThemeState,
    private val serviceC: GameServiceImpl = gameSession.game.service
) : AppStateImpl {
    override val service get() = game.service

    companion object {
        // Empty AppState for initialization
        fun empty(service: GameServiceImpl): AppState = AppState(
            gameSession = GameSession(Game(service = service), null),
            pagesState = PagesState(Page.MAIN_MENU, Page.NONE, null),
            audioThemeState = AudioThemeState(
                audioPool = AudioPool(emptyList()),
                theme = AppThemes.DARK.appTheme
            )
        )
    }
}

/**
 * Holds the current page navigation state and any global errors.
 * @property page The currently active page.
 * @property backPage The previous page for navigation purposes.
 * @property globalError Any global error that occurred, or null if none.
 */
data class PagesState(
    val page: Page,
    val backPage: Page,
    val globalError: ReversiException?,
)

/**
 * Represents the current game session with game state and player identity.
 * @property game The active game instance.
 * @property playerName The name of the player in this session, if set.
 */
data class GameSession(val game: Game, val playerName: String?)

/**
 * Holds audio and theme configuration for the application.
 * @property audioPool The audio pool managing game sounds.
 * @property theme The currently applied visual theme.
 */
data class AudioThemeState(val audioPool: AudioPool, val theme: AppTheme)

