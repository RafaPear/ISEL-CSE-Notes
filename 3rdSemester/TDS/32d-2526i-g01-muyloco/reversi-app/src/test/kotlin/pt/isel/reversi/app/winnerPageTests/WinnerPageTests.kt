package pt.isel.reversi.app.winnerPageTests

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import kotlinx.coroutines.CoroutineScope
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.GameSession
import pt.isel.reversi.app.app.state.PagesState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.Page
import pt.isel.reversi.app.pages.winnerPage.WinnerPage
import pt.isel.reversi.app.pages.winnerPage.WinnerPageViewModel
import pt.isel.reversi.app.pages.winnerPage.testTagDrawCrown
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class WinnerPageTests {

    @BeforeTest
    @AfterTest
    fun cleanup() {
        File(BASE_FOLDER).deleteRecursively()
    }

    val game: Game = run {
        // Create a game with a winner by setting up a finished game state
        val gameState = GameState(
            players = MatchPlayers(
                Player(PieceType.BLACK, "Player 1"),
                Player(PieceType.WHITE, "Player 2")
            ),
            board = Board(4)
                .addPiece(Piece(Coordinate(1, 1), PieceType.BLACK))
                .addPiece(Piece(Coordinate(1, 2), PieceType.BLACK)),
            lastPlayer = PieceType.BLACK,
            winner = Player(PieceType.BLACK, "Player 1")
        )
        Game(
            target = false,
            gameState = gameState,
            currGameName = null,
            myPiece = PieceType.BLACK,
            service = EmptyGameService()
        )
    }

    val appState = AppState.empty(service = EmptyGameService()).copy(
        gameSession = GameSession(game, null),
        pagesState = PagesState(Page.GAME, Page.NONE, null)
    )

    fun winnerPageViewModel(scope: CoroutineScope) =
        WinnerPageViewModel(
            game = game,
            scope = scope,
            globalError = null,
            setGlobalError = { _, _ -> }
        )

    @Test
    fun `check if the Winner page displays the crown decoration`() = runComposeUiTest {
        val reversiScope = ReversiScope(appState)
        setContent {
            val scope = rememberCoroutineScope()
            val viewModel = winnerPageViewModel(scope)
            reversiScope.WinnerPage(
                viewModel = viewModel,
                onLeave = {}
            )
        }

        onNodeWithTag(testTagDrawCrown(), true).assertExists()
    }
}