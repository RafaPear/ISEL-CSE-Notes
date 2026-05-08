package pt.isel.reversi.core.gameTests

import kotlinx.coroutines.test.runTest
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.InvalidPlay
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.game.loadAndEntryGame
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.core.storage.StorageParams
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.*

class GameAndServiceIntegrationTests {
    val gameService = GameService(
        storage = GameStorageType.FILE_STORAGE,
        params = StorageParams.FileStorageParams(folder = "test-saves")
    )

    @BeforeTest
    @AfterTest
    fun cleanup() {
        File("test-saves").deleteRecursively()
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `play in not local game and not my turn fails`() = runTest {
        startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK)),
            firstTurn = PieceType.BLACK,
            currGameName = "testGame",
            service = gameService,
        )

        val uutW = loadAndEntryGame(
            gameName = "testGame",
            desiredType = null,
            service = gameService,
        )

        val coordinate = uutW.copy(
            gameState = uutW.gameState?.copy(
                lastPlayer = uutW.gameState.lastPlayer.swap()
            )
        ).getAvailablePlays().first()

        assertFailsWith<InvalidPlay> {
            uutW.play(coordinate)
        }
    }

    @Test
    fun `pass no local game with 2 players succeeds`() = runTest {

        var uut = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK)),
            currGameName = "testGame",
            firstTurn = PieceType.BLACK,
            service = gameService,
        )
        val gameState = uut.gameState!!.copy(
            board = Board(4)
                .addPiece(Piece(Coordinate(1, 1), PieceType.BLACK))
                .addPiece(Piece(Coordinate(1, 2), PieceType.BLACK))
        )
        uut.saveOnlyBoard(gameState)
        uut = uut.refresh()

        var uut2 = loadAndEntryGame(
            gameName = uut.currGameName!!,
            desiredType = null,
            service = gameService,
        )

        uut.pass()
        uut2 = uut2.refresh()


        uut2 = uut2.pass()

        assertEquals(Player(PieceType.BLACK, points = 2), uut2.gameState?.winner)

        uut = uut.refresh()

        assertEquals(Player(PieceType.BLACK, points = 2), uut.gameState?.winner)
    }

    @Test
    fun `pass in not local game with available plays fails`() = runTest {

        var uutB = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK)),
            firstTurn = PieceType.BLACK,
            currGameName = "testGame",
            service = gameService,
        )

        var uutW = loadAndEntryGame(
            gameName = "testGame",
            desiredType = null,
            service = gameService,
        )

        uutB = uutB.refresh()
        assertFailsWith<InvalidPlay> {
            uutB.pass()
        }

        assertEquals(PieceType.WHITE, uutB.gameState?.lastPlayer)

        uutW = uutW.refresh()
        assertFailsWith<InvalidPlay> {
            uutW.pass()
        }

        assertEquals(PieceType.WHITE, uutW.gameState?.lastPlayer)
    }
}