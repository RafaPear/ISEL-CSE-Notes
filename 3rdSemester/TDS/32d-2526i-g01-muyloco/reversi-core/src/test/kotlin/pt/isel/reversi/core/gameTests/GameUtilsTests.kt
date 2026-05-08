package pt.isel.reversi.core.gameTests

import kotlinx.coroutines.test.runTest
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.InvalidGame
import pt.isel.reversi.core.exceptions.InvalidNameAlreadyExists
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.core.storage.StorageParams
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.*

class GameUtilsTests {
    val gameService = GameService(
        storage = GameStorageType.FILE_STORAGE,
        params = StorageParams.FileStorageParams(folder = "test-saves")
    )

    @BeforeTest
    @AfterTest
    fun cleanup() {
        kotlin.io.path.Path("test-saves").toFile().deleteRecursively()
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `startNewGame in local game with 2 players succeeds`() = runTest {

        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK), Player(PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )

        val expectedBoard = Board(4).startPieces()
        val expectedPlayers = MatchPlayers(Player(PieceType.BLACK, points = 2), Player(PieceType.WHITE, points = 2))
        val expectedLastPlayer = PieceType.WHITE

        assertEquals(expectedBoard, uut.gameState?.board)
        assertEquals(expectedPlayers, uut.gameState?.players)
        assertEquals(expectedLastPlayer, uut.gameState?.lastPlayer)
    }

    @Test
    fun `startNewGame with empty players fails`() = runTest {

        assertFailsWith<InvalidGame> {
            startNewGame(
                side = 4,
                players = MatchPlayers(),
                firstTurn = PieceType.BLACK,
                currGameName = null,
                service = EmptyGameService()
            )
        }
    }

    @Test
    fun `startNewGame in local game with 1 player succeeds`() = runTest {

        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )

        val expectedBoard = Board(4).startPieces()
        val expectedPlayers = MatchPlayers(Player(PieceType.BLACK, points = 2))
        val expectedLastPlayer = PieceType.WHITE

        assertEquals(expectedBoard, uut.gameState?.board)
        assertEquals(expectedPlayers, uut.gameState?.players)
        assertEquals(expectedLastPlayer, uut.gameState?.lastPlayer)
    }

    @Test
    fun `startNewGame with already existing name in storage fails`() = runTest {
        startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK)),
            firstTurn = PieceType.BLACK,
            currGameName = "existingGame",
            service = gameService,
        )

        assertFailsWith<InvalidNameAlreadyExists> {
            startNewGame(
                side = 4,
                players = MatchPlayers(Player(PieceType.WHITE)),
                firstTurn = PieceType.WHITE,
                currGameName = "existingGame",
                service = gameService,
            )
        }
    }

    @Test
    fun `startNewGame and load game with 1 player succeeds`() = runTest {

        val player1 = Player(PieceType.BLACK, points = 2)
        val expectedPlayers = MatchPlayers(player1)
        val expectedBoard = Board(4).startPieces()
        val expectedLastPlayer = PieceType.WHITE

        val game = startNewGame(
            side = 4,
            players = MatchPlayers(player1),
            firstTurn = PieceType.BLACK,
            currGameName = "existingGame",
            service = gameService,
        )

        val loadedGame = game.service.hardLoad("existingGame")?.let {
            Game(
                target = false,
                gameState = it,
                currGameName = "existingGame",
                service = gameService,
            )
        }!!

        assertEquals(expectedBoard, loadedGame.gameState?.board)
        assertEquals(expectedPlayers, loadedGame.gameState?.players)
        assertEquals(expectedLastPlayer, loadedGame.gameState?.lastPlayer)
    }

}