package pt.isel.reversi.app.pages

/**
 * Represents the different pages in the application along with their hierarchy levels.
 * @property level The hierarchy level of the page, where a higher number indicates a deeper level.
 */
enum class Page(val level: Int) {
    NONE(-1),
    MAIN_MENU(0),
    SETTINGS(1),
    ABOUT(1),
    NEW_GAME(1),
    LOBBY(1),
    GAME(2),
    WINNER(3)
}
