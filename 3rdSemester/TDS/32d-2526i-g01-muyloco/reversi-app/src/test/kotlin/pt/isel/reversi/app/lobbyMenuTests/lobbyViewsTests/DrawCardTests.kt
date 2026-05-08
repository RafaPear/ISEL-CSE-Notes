package pt.isel.reversi.app.lobbyMenuTests.lobbyViewsTests

import androidx.compose.ui.test.*
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.pages.game.utils.testTagBoard
import pt.isel.reversi.app.pages.game.utils.testTagCellView
import pt.isel.reversi.app.pages.lobby.LobbyLoadedState
import pt.isel.reversi.app.pages.lobby.lobbyViews.lobbyCarousel.drawCard.*
import pt.isel.reversi.core.board.Board
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
class DrawCardTests {
    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    val game = Game(
        currGameName = "TestGame",
        gameState = GameState(
            players = MatchPlayers(Player(PieceType.WHITE)),
            lastPlayer = PieceType.BLACK,
            board = Board(4).startPieces(),
        ),
        myPiece = PieceType.WHITE,
        service = EmptyGameService()
    )

    val reversiScope = ReversiScope(AppState.empty(EmptyGameService()))

    @Test
    fun `verify if drawCard is displayed`() = runComposeUiTest {
        val name = game.currGameName!!
        val lobbyState = LobbyLoadedState(game.gameState!!, name)

        setContent {
            reversiScope.GameCard(
                game = LobbyLoadedState(game.gameState!!, name),
                enabled = false,
                cardData = getCardStatus(lobbyState, name),
                onClick = {}
            )
        }

        onNodeWithTag(testTagCard(name)).assertExists()
    }

    @Test
    fun `verify if drawCard is displayed correctly`() = runComposeUiTest {
        val name = game.currGameName!!
        val lobbyState = LobbyLoadedState(game.gameState!!, name)

        setContent {
            reversiScope.GameCard(
                game = lobbyState,
                enabled = false,
                cardData = getCardStatus(lobbyState, name),
                onClick = {}
            )
        }

        onNodeWithTag(testTagCard(name)).assertExists()
        onNodeWithTag(testTagHeaderBadge(name), true).assertExists()
        onNodeWithTag(testTagBoard(), true).assertExists()
        onNodeWithTag(testTagScorePanel(name), true).assertExists()
    }

    @Test
    fun `verify if Card is enabled`() = runComposeUiTest {
        val name = game.currGameName!!
        val lobbyState = LobbyLoadedState(game.gameState!!, name)

        setContent {
            reversiScope.GameCard(
                game = lobbyState,
                enabled = true,
                cardData = getCardStatus(lobbyState, name),
                onClick = {}
            )
        }

        onNodeWithTag(testTagCard(name)).assertIsEnabled()
    }

    @Test
    fun `verify if Card is disabled`() = runComposeUiTest {
        val name = game.currGameName!!
        val lobbyState = LobbyLoadedState(game.gameState!!, name)

        setContent {
            reversiScope.GameCard(
                game = LobbyLoadedState(game.gameState!!, name),
                enabled = false,
                cardData = getCardStatus(lobbyState, name),
                onClick = {}
            )
        }

        onNodeWithTag(testTagCard(name)).assertIsNotEnabled()
    }

    @Test
    fun `veify id header badge displays correct game name and status`() = runComposeUiTest {
        val name = game.currGameName!!
        val lobbyState = LobbyLoadedState(game.gameState!!, name)
        val cardStatus = getCardStatus(lobbyState, name)

        setContent {
            reversiScope.GameCard(
                game = lobbyState,
                enabled = false,
                cardData = cardStatus,
                onClick = {}
            )
        }

        onNodeWithTag(testTagStatusText(name), true).assertTextContains(name)
        onNodeWithTag(testTagStatusBadge(name), true).assertTextContains(cardStatus.text)
    }

    @Test
    fun `verify if board is not clickable`() = runComposeUiTest {
        val name = game.currGameName!!
        val lobbyState = LobbyLoadedState(game.gameState!!, name)

        setContent {
            reversiScope.GameCard(
                game = lobbyState,
                enabled = false,
                cardData = getCardStatus(lobbyState, name),
                onClick = {}
            )
        }
        println(game.getAvailablePlays())
        val valideCoords = game.getAvailablePlays().first()
        onNodeWithTag(testTagBoard(), true).assertHasNoClickAction()
        onNodeWithTag(testTagCellView(valideCoords), true).assertHasNoClickAction()
    }

    @Test
    fun `veify if score panel displays correct scores and pieces`() = runComposeUiTest {
        val name = game.currGameName!!
        val lobbyState = LobbyLoadedState(game.gameState!!, name)

        setContent {
            reversiScope.GameCard(
                game = lobbyState,
                enabled = false,
                cardData = getCardStatus(lobbyState, name),
                onClick = {}
            )
        }

        val board = game.gameState!!.board
        val totalBlack = board.totalBlackPieces
        val totalWhite = board.totalWhitePieces

        val blackPieceTestTag = testTagScoreItemPiece(
            scorePainelTestTag = testTagScorePanel(gameId = name),
            pieceType = PieceType.BLACK
        )

        val whitePieceTestTag = testTagScoreItemPiece(
            scorePainelTestTag = testTagScorePanel(gameId = name),
            pieceType = PieceType.WHITE
        )

        val blackPiecesScoreTestTag = testTagScoreItemScore(
            scorePainelTestTag = testTagScorePanel(gameId = name),
            pieceType = PieceType.BLACK,
            score = totalBlack
        )

        val whitePiecesScoreTestTag = testTagScoreItemScore(
            scorePainelTestTag = testTagScorePanel(gameId = name),
            pieceType = PieceType.WHITE,
            score = totalWhite
        )

        onNodeWithTag(blackPieceTestTag, true).assertExists()
        onNodeWithTag(whitePieceTestTag, true).assertExists()
        onNodeWithTag(blackPiecesScoreTestTag, true).assertTextContains(totalBlack.toString())
        onNodeWithTag(whitePiecesScoreTestTag, true).assertTextContains(totalWhite.toString())
    }
}