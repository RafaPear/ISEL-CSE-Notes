package pt.isel.reversi.core.gameTests

import kotlinx.coroutines.test.runTest
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.EndGame
import pt.isel.reversi.core.exceptions.InvalidGame
import pt.isel.reversi.core.exceptions.InvalidPlay
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.utils.BASE_FOLDER
import pt.isel.reversi.utils.LOGGER
import java.io.File
import kotlin.test.*

class GameTests {

    @BeforeTest
    @AfterTest
    fun cleanup() = runTest {
        File("test-game").deleteRecursively()
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `play with game not started yet`() = runTest {
        val game = Game(
            service = EmptyGameService()
        )

        assertFailsWith<InvalidGame> {
            game.play(Coordinate(1, 1))
        }
    }

    /*
          1 2 3 4
        1 . . . .
        2 . W B .
        3 . B W .
        4 . . . .
     */
    @Test
    fun `play with 1 player in local game fails`() = runTest {
        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )

        assertFails { (uut.play(Coordinate(1, 2))) }
    }

    @Test
            /*
          1 2 3 4
        1 . . . .
        2 . W B .
        3 . B W .
        4 . . . .
            */
    fun `play in local game with 2 players succeeds`() = runTest {

        val expectedBoard = Board(4).startPieces()
            .addPiece(Coordinate(1, 2), PieceType.BLACK)
            .changePiece(Coordinate(2, 2))

        val expectedGame = Game(
            gameState = GameState(
                lastPlayer = PieceType.BLACK,
                board = expectedBoard,
                players = MatchPlayers(
                    Player(PieceType.BLACK, points = 4),
                    Player(PieceType.WHITE, points = 1)
                )
            ),
            target = false,
            currGameName = null,
            service = EmptyGameService()
        )

        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK), Player(PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        ).play(Coordinate(1, 2))

        LOGGER.info { "Expected Game State:\n${expectedGame.gameState}\nUUT Game State:\n${uut.gameState}" }

        assert(expectedGame.gameState == uut.gameState)
    }

    @Test
            /*
          1 2 3 4
        1 . . . .
        2 . W W W
        3 . B B B
        4 . . . .
               */
    fun `play in local game verify players point update correctly and playerTurn after normal plays`() = runTest {
        val expectedBlackPoints = 3
        val expectedWhitePoints = 3
        val expectedPlayerTurn = PieceType.WHITE

        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK), Player(PieceType.WHITE)),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )
            .play(Coordinate(3, 4))
            .play(Coordinate(2, 4))

        assertEquals(expectedBlackPoints, uut.gameState?.players?.find { it.type == PieceType.BLACK }!!.points)
        assertEquals(expectedWhitePoints, uut.gameState.players.find { it.type == PieceType.WHITE }!!.points)
        assertEquals(expectedPlayerTurn, uut.gameState.lastPlayer)
    }

    @Test
    fun `play with players empty fails`() = runTest {

        val uut = Game(
            target = false,
            gameState = GameState(
                players = MatchPlayers(),
                lastPlayer = PieceType.BLACK,
                board = Board(4).startPieces(),
            ),
            currGameName = null,
            service = EmptyGameService()
        )

        assertFailsWith<InvalidGame> {
            uut.play(Coordinate(1, 2))
        }
    }

    @Test
    fun `pass in localGame when no availablePlays succeeds`() = runTest {

        var uut = Game(
            target = false,
            gameState = GameState(
                players = MatchPlayers(
                    Player(PieceType.BLACK, "Player 1"),
                    Player(PieceType.WHITE, "Player 2")
                ),
                board = Board(4)
                    .addPiece(Piece(Coordinate(1, 1), PieceType.BLACK))
                    .addPiece(Piece(Coordinate(1, 2), PieceType.BLACK)),
                lastPlayer = PieceType.BLACK,
            ),
            currGameName = null,
            myPiece = PieceType.BLACK,
            service = EmptyGameService()
        ).pass()

        val expectedCountPass = 1
        val expectedPlayerTurn = PieceType.WHITE

        assertEquals(expectedCountPass, uut.countPass)
        assertEquals(expectedPlayerTurn, uut.gameState?.lastPlayer)


        uut = uut.pass()


        assertEquals(Player(PieceType.BLACK, points = 2), uut.gameState?.winner)
    }

    @Test

    fun `pass with game not started yet fails`() = runTest {

        val game = Game(
            service = EmptyGameService()
        )
        assertFailsWith<InvalidGame> {
            game.pass()
        }
    }

    @Test
    fun `pass with players empty fails`() = runTest {

        val uut = Game(
            target = false,
            gameState = GameState(
                players = MatchPlayers(),
                lastPlayer = PieceType.BLACK,
                board = Board(4).startPieces()
            ),
            currGameName = null,
            service = EmptyGameService()
        )

        assertFailsWith<InvalidGame> {
            uut.pass()
        }
    }


    @Test
    fun `pass in local game with available plays fails`() = runTest {

        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(
                Player(PieceType.BLACK),
                Player(PieceType.WHITE)
            ),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )

        assertFailsWith<InvalidPlay> {
            uut.pass()
        }

        assertEquals(PieceType.WHITE, uut.gameState?.lastPlayer)
    }

    @Test
    fun `refresh in local game returns same game`() = runTest {

        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(
                Player(PieceType.BLACK),
                Player(PieceType.WHITE)
            ),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )

        val refreshedGame = uut.refresh()

        assertEquals(uut, refreshedGame)
    }

    @Test
    fun `checkTurnOnNotLocalGame succeeds`() = runTest {
        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK)),
            currGameName = "testGame",
            firstTurn = PieceType.BLACK,
            service = EmptyGameService(),
        )

        uut.checkTurnOnNotLocalGame(uut.gameState!!)
        assert(true)
    }

    @Test
    fun `checkTurnOnNotLocalGame with wrong turn fails`() = runTest {
        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(Player(PieceType.BLACK)),
            currGameName = "testGame",
            firstTurn = PieceType.WHITE,
            service = EmptyGameService(),
        ).copy(myPiece = PieceType.BLACK)

        assertFailsWith<InvalidPlay> {
            uut.checkTurnOnNotLocalGame(uut.gameState!!)
        }
    }


    @Test
    fun `gameEnded succeeds`() = runTest {
        val uut = startNewGame(
            side = 4,
            players = MatchPlayers(
                Player(PieceType.BLACK),
                Player(PieceType.WHITE)
            ),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )

        uut.gameEnded()
        assert(true)
    }

    @Test
    fun `gameEnded fails when no winner`() = runTest {
        var uut = startNewGame(
            side = 4,
            players = MatchPlayers(
                Player(PieceType.BLACK),
                Player(PieceType.WHITE)
            ),
            firstTurn = PieceType.BLACK,
            currGameName = null,
            service = EmptyGameService()
        )
        uut = uut.copy(
            gameState = uut.gameState?.copy(winner = Player(PieceType.WHITE)),
        )

        assertFailsWith<EndGame> {
            uut.gameEnded()
        }
    }
}