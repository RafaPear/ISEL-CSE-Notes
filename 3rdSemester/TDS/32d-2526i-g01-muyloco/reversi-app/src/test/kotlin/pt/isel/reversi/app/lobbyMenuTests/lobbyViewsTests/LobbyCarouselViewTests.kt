package pt.isel.reversi.app.lobbyMenuTests.lobbyViewsTests

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.*
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.lobby.LobbyLoadedState
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.EMPTY_LOBBY_CAROUSEL_TEXT
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.LobbyCarouselView
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.drawCard.testTagCard
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.testTagLobbyCarouselPager
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.testTagLobbyCarrouselEmpty
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.utils.testTagNavButton
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
import kotlin.test.assertEquals

@OptIn(ExperimentalTestApi::class)
class LobbyCarouselViewTests {
    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }


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
            name = "game0"
        ),
        LobbyLoadedState(
            gameState = GameState(
                players = MatchPlayers(
                    Player(PieceType.BLACK)
                ),
                board = board,
                lastPlayer = PieceType.BLACK
            ),
            name = "game1"
        ),
        LobbyLoadedState(
            gameState = GameState(
                players = MatchPlayers(),
                lastPlayer = PieceType.BLACK,
                board = board,
            ),
            name = "game2"
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

    val reversiScope = ReversiScope(
        appState = AppState.Companion.empty(service = EmptyGameService())
    )

    @Test
    fun `verify lobby carousel view is displayed correctly with 2 buttons`() = runComposeUiTest {

        setContent {
            BoxWithConstraints(modifier = Modifier.Companion.fillMaxSize()) {
                LobbyCarouselView(
                    currentGameName = null,
                    pagerState = PagerState(pageCount = { games.size }, currentPage = 1),
                    games = games,
                    reversiScope = reversiScope,
                    onNavButtonClick = { },
                    onGameClick = { _, _ -> }
                )
            }
        }

        games.forEach {
            onNodeWithTag(testTagCard(it.name)).assertExists()
        }

        onNodeWithTag(testTagNavButton("left")).assertExists()
        onNodeWithTag(testTagNavButton("right")).assertExists()
    }


    @Test
    fun `verify cards render in games order`() = runComposeUiTest {

        setContent {
            BoxWithConstraints(modifier = Modifier.Companion.fillMaxSize()) {
                LobbyCarouselView(
                    currentGameName = null,
                    pagerState = PagerState(pageCount = { games.size }),
                    games = games,
                    reversiScope = reversiScope,
                    onNavButtonClick = { },
                    onGameClick = { _, _ -> }
                )
            }
        }

        val expectedOrder = games.map { testTagCard(it.name) }
        //forEach on children of the pager and check if the tags are in the expected order
        onNodeWithTag(testTagLobbyCarouselPager())
            .onChildren()
            .fetchSemanticsNodes()
            .forEachIndexed { index, node ->
                val tag = node.config.getOrNull(SemanticsProperties.TestTag) // get the test tag of the child node
                assertEquals(expectedOrder[index], tag)
            }
    }

    @Test
    fun `verify scroll to game works correctly`() = runComposeUiTest {

        val duplicatedGames = duplicateGames(games)

        val pagerState = PagerState(pageCount = { duplicatedGames.size })

        setContent {
            BoxWithConstraints(modifier = Modifier.Companion.fillMaxSize()) {
                LobbyCarouselView(
                    currentGameName = null,
                    pagerState = pagerState,
                    games = duplicatedGames,
                    reversiScope = reversiScope,
                    onNavButtonClick = { },
                    onGameClick = { _, _ -> }
                )
            }
        }

        // Scroll to end
        pagerState.scrollToPage(duplicatedGames.size - 1)

        onNodeWithTag(testTagCard(duplicatedGames.last().name)).assertExists()
        onNodeWithTag(testTagCard(duplicatedGames[duplicatedGames.size - 2].name)).assertExists()
        onNodeWithTag(testTagCard(duplicatedGames[duplicatedGames.size - 3].name)).assertDoesNotExist()
    }

    @Test
    fun `verify if display only right nav button on first page`() = runComposeUiTest {
        setContent {
            BoxWithConstraints(modifier = Modifier.Companion.fillMaxSize()) {
                LobbyCarouselView(
                    currentGameName = null,
                    pagerState = PagerState(pageCount = { games.size }, currentPage = 0),
                    games = games,
                    reversiScope = reversiScope,
                    onNavButtonClick = { },
                    onGameClick = { _, _ -> }
                )
            }
        }

        onNodeWithTag(testTagNavButton("left")).assertDoesNotExist()
        onNodeWithTag(testTagNavButton("right")).assertExists()
    }

    @Test
    fun `verify if display only left nav button on last page`() = runComposeUiTest {
        setContent {
            BoxWithConstraints(modifier = Modifier.Companion.fillMaxSize()) {
                LobbyCarouselView(
                    currentGameName = null,
                    pagerState = PagerState(pageCount = { games.size }, currentPage = games.size - 1),
                    games = games,
                    reversiScope = reversiScope,
                    onNavButtonClick = { },
                    onGameClick = { _, _ -> }
                )
            }
        }

        onNodeWithTag(testTagNavButton("left")).assertExists()
        onNodeWithTag(testTagNavButton("right")).assertDoesNotExist()
    }

    @Test
    fun `verify if no nav buttons are displayed when only one page exists`() = runComposeUiTest {
        setContent {
            BoxWithConstraints(modifier = Modifier.Companion.fillMaxSize()) {
                LobbyCarouselView(
                    currentGameName = null,
                    pagerState = PagerState(pageCount = { 1 }, currentPage = 0),
                    games = listOf(games[0]),
                    reversiScope = reversiScope,
                    onNavButtonClick = { },
                    onGameClick = { _, _ -> }
                )
            }
        }

        onNodeWithTag(testTagNavButton("left")).assertDoesNotExist()
        onNodeWithTag(testTagNavButton("right")).assertDoesNotExist()
    }

    @Test
    fun `verify onGameClick is called when a card is clicked`() = runComposeUiTest {
        var clickedGameName: String? = null

        setContent {
            BoxWithConstraints(modifier = Modifier.Companion.fillMaxSize()) {
                LobbyCarouselView(
                    currentGameName = null,
                    pagerState = PagerState(pageCount = { games.size }, currentPage = 0),
                    games = games,
                    reversiScope = reversiScope,
                    onNavButtonClick = { },
                    onGameClick = { game, _ ->
                        clickedGameName = game.name
                    }
                )
            }
        }

        val gameToClick = games[0]
        onNodeWithTag(testTagCard(gameToClick.name)).performClick()

        assertEquals(gameToClick.name, clickedGameName)
    }

    @Test
    fun `verify if nav buttons call onNavButtonClick with correct page`() = runComposeUiTest {
        var clickedNavButton = 0

        setContent {
            BoxWithConstraints(modifier = Modifier.Companion.fillMaxSize()) {
                LobbyCarouselView(
                    currentGameName = null,
                    pagerState = PagerState(pageCount = { games.size }, currentPage = 1),
                    games = games,
                    reversiScope = reversiScope,
                    onNavButtonClick = { _ ->
                        clickedNavButton++
                    },
                    onGameClick = { _, _ -> }
                )
            }
        }

        onNodeWithTag(testTagNavButton("left")).performClick()
        assertEquals(1, clickedNavButton)

        onNodeWithTag(testTagNavButton("right")).performClick()
        assertEquals(2, clickedNavButton)
    }

    @Test
    fun `verify lobby carousel view with no games`() = runComposeUiTest {
        setContent {
            BoxWithConstraints(modifier = Modifier.Companion.fillMaxSize()) {
                LobbyCarouselView(
                    currentGameName = null,
                    pagerState = PagerState(pageCount = { 0 }),
                    games = emptyList(),
                    reversiScope = reversiScope,
                    onNavButtonClick = { },
                    onGameClick = { _, _ -> }
                )
            }
        }

        onNodeWithTag(testTagLobbyCarouselPager()).onChildren().assertCountEquals(0)
        onNodeWithTag(testTagNavButton("left")).assertDoesNotExist()
        onNodeWithTag(testTagNavButton("right")).assertDoesNotExist()
        onNodeWithTag(testTagLobbyCarrouselEmpty()).assertExists().assertTextEquals(EMPTY_LOBBY_CAROUSEL_TEXT)
    }
}