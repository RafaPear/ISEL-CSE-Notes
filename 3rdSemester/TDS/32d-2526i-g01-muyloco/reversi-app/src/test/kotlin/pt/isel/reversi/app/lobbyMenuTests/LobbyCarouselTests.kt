package pt.isel.reversi.app.lobbyMenuTests

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.*
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.lobby.LobbyLoadedState
import pt.isel.reversi.app.pages.lobby.LobbyViewModel
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.LobbyCarousel
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.drawCard.testTagCard
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.testTagLobbyCarouselPager
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils.testTagPageIndicatorText
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils.testTagPageIndicators
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils.testTagSearch
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils.textPageIndicator
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
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
class LobbyCarouselTests {
    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    val reversiScope = ReversiScope(AppState.empty(service = EmptyGameService()))
    val board = Board(4).startPieces()

    val games = listOf(
        LobbyLoadedState(
            gameState = GameState(
                players = MatchPlayers(
                    Player(PieceType.BLACK),
                    Player(PieceType.WHITE)
                ),
                board = board,
                lastPlayer = PieceType.BLACK
            ),
            name = "Hgame0"
        ),
        LobbyLoadedState(
            gameState = GameState(
                players = MatchPlayers(
                    Player(PieceType.BLACK)
                ),
                board = board,
                lastPlayer = PieceType.BLACK
            ),
            name = "Hgame1"
        ),
        LobbyLoadedState(
            gameState = GameState(
                players = MatchPlayers(),
                lastPlayer = PieceType.BLACK,
                board = board,
            ),
            name = "Bgame2"
        )
    )

    fun duplicateGames(games: List<LobbyLoadedState>): List<LobbyLoadedState> {
        val newList = games.toMutableList()
        games.forEach {
            newList.add(
                LobbyLoadedState(
                    gameState = it.gameState,
                    name = "game_${newList.size}"
                )
            )
        }
        return newList
    }

    @Test
    fun `verify lobbyCarousel display correctly`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            Column {
                LobbyCarousel(
                    currentGameName = null,
                    games = emptyList(),
                    viewModel = LobbyViewModel(
                        reversiScope.appState,
                        scope,
                        {},
                        null,
                        { _, _ -> },
                    ),
                    reversiScope = reversiScope,
                    onGameClick = { }
                )
            }
        }

        onNodeWithTag(testTagSearch()).assertExists()
        onNodeWithTag(testTagLobbyCarouselPager()).assertExists()
        onNodeWithTag(testTagPageIndicators()).assertExists()
    }

    @Test
    fun `verify precise search functionality in lobbyCarousel`() = runComposeUiTest {
        val duplicateGames = duplicateGames(games)
        setContent {
            val scope = rememberCoroutineScope()
            Column {
                LobbyCarousel(
                    currentGameName = null,
                    games = duplicateGames,
                    viewModel = LobbyViewModel(
                        reversiScope.appState,
                        scope,
                        {},
                        null,
                        { _, _ -> },
                    ),
                    reversiScope = reversiScope,
                    onGameClick = { }
                )
            }
        }

        val nameGame = duplicateGames.last().name

        //show firsts that do not match
        onNodeWithTag(testTagCard(duplicateGames[0].name)).assertExists()
        onNodeWithTag(testTagCard(nameGame)).assertDoesNotExist()

        onNodeWithTag(testTagSearch()).performTextInput(nameGame)

        onNodeWithTag(testTagCard(duplicateGames[0].name)).assertDoesNotExist()
        onNodeWithTag(testTagCard(nameGame)).assertExists()
    }

    @Test
    fun `verify broad search functionality in lobbyCarousel`() = runComposeUiTest {
        val duplicateGames = duplicateGames(games)
        setContent {
            val scope = rememberCoroutineScope()
            Column {
                LobbyCarousel(
                    currentGameName = null,
                    games = duplicateGames,
                    viewModel = LobbyViewModel(
                        reversiScope.appState,
                        scope,
                        {},
                        null,
                        { _, _ -> },

                    ),
                    reversiScope = reversiScope,
                    onGameClick = { }
                )
            }
        }

        val searchTerm = "H"

        val gameWithH = duplicateGames.filter { it.name.contains("H") }

        //show firsts that do not match
        onNodeWithTag(testTagCard(duplicateGames[0].name)).assertExists()
        onNodeWithTag(testTagCard(duplicateGames[1].name)).assertExists()

        onNodeWithTag(testTagSearch()).performTextInput(searchTerm)

        gameWithH.forEach {
            onNodeWithTag(testTagCard(it.name)).assertExists()
        }

        games.filter { !it.name.contains("H") }.forEach {
            onNodeWithTag(testTagCard(it.name)).assertDoesNotExist()
        }
    }

    @Test
    fun `indicator shows correct number of pages and current page`() = runComposeUiTest {
        val duplicateGames = duplicateGames(games)
        setContent {
            val scope = rememberCoroutineScope()
            Column {
                LobbyCarousel(
                    currentGameName = null,
                    games = duplicateGames,
                    viewModel = LobbyViewModel(
                        reversiScope.appState,
                        scope,
                        {},
                        null,
                        { _, _ -> },
                    ),
                    reversiScope = reversiScope,
                    onGameClick = { }
                )
            }
        }

        val totalPages = duplicateGames.size

        val currentPage = 3

        onNodeWithTag(testTagLobbyCarouselPager()).performScrollToIndex(currentPage)

        onNodeWithTag(testTagPageIndicators()).assertExists()
        onNodeWithTag(testTagPageIndicatorText()).assertTextEquals(textPageIndicator(currentPage, totalPages))
    }

    @Test
    fun `indicator shows 0 de 0 when there are no games`() = runComposeUiTest {
        setContent {
            val scope = rememberCoroutineScope()
            Column {
                LobbyCarousel(
                    currentGameName = null,
                    games = emptyList(),
                    viewModel = LobbyViewModel(
                        reversiScope.appState,
                        scope,
                        {},
                        null,
                        { _, _ -> },
                    ),
                    reversiScope = reversiScope,
                    onGameClick = { }
                )
            }
        }

        onNodeWithTag(testTagPageIndicators()).assertExists()
        onNodeWithTag(testTagPageIndicatorText()).assertTextEquals(textPageIndicator(0,0))
    }

    @Test
    fun `search for non existing game shows no results`() = runComposeUiTest {
        val duplicateGames = duplicateGames(games)
        setContent {
            val scope = rememberCoroutineScope()
            Column {
                LobbyCarousel(
                    currentGameName = null,
                    games = duplicateGames,
                    viewModel = LobbyViewModel(
                        reversiScope.appState,
                        scope,
                        {},
                        null,
                        { _, _ -> },
                    ),
                    reversiScope = reversiScope,
                    onGameClick = { }
                )
            }
        }

        val searchTerm = "NonExistingGame"

        onNodeWithTag(testTagSearch()).performTextInput(searchTerm)

        duplicateGames.forEach {
            onNodeWithTag(testTagCard(it.name)).assertDoesNotExist()
        }

        onNodeWithTag(testTagPageIndicators()).assertExists()
        onNodeWithTag(testTagPageIndicatorText()).assertTextEquals(textPageIndicator(0,0))
    }
}