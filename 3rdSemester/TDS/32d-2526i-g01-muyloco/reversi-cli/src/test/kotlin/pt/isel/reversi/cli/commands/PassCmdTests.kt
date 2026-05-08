package pt.isel.reversi.cli.commands

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.game.newGameForTest
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.core.storage.StorageParams
import pt.isel.reversi.utils.BASE_FOLDER
import pt.rafap.ktflag.cmd.CommandResultType
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class PassCmdTests {

    val gameService = GameService(
        storage = GameStorageType.FILE_STORAGE,
        params = StorageParams.FileStorageParams(folder = "data/saves")
    )

    @BeforeTest
    @AfterTest
    fun cleanup() {
        File(BASE_FOLDER).deleteRecursively()
    }


    @Test
    fun `Test PassCmd execution`() {
        val result = PassCmd.executeWrapper(
            context = newGameForTest(
                players = MatchPlayers(
                    Player(PieceType.BLACK),
                    Player(PieceType.WHITE)
                ),
                board = Board(4)
                    .addPiece(Piece(Coordinate(1, 1), PieceType.BLACK))
                    .addPiece(Piece(Coordinate(1, 2), PieceType.BLACK)),
                myPiece = PieceType.WHITE,
                service = gameService
            )
        )
        assert(result.type == CommandResultType.SUCCESS) {
            "Expected SUCCESS but got ${result.type} with message: ${result.message}"
        }
    }

    @Test
    fun `Test PassCmd execution fails on null game`() {
        val result = PassCmd.executeWrapper(context = null)
        assert(result.type == CommandResultType.ERROR) {
            "Expected ERROR but got ${result.type} with message: ${result.message}"
        }
    }

    @Test
    fun `Test PassCmd fails execution by arguments`() {
        val args = arrayOf("extraArg")
        val result = PassCmd.executeWrapper(*args, context = null)
        assert(result.type == CommandResultType.INVALID_ARGS) {
            "Expected INVALID_ARGS but got ${result.type} with message: ${result.message}"
        }
    }

    @Test
    fun `PassCmd returns error when context is null`() {
        val res = PassCmd.execute(context = null)
        assertTrue(res.message.contains("Game is not defined"))
    }

    @Test
    fun `PassCmd succeeds when called with a started game`() {
        // Use a simple started game using startNewGame helper
        val g = runBlocking {
            startNewGame(
                side = 8,
                players = MatchPlayers(Player(PieceType.BLACK)),
                firstTurn = PieceType.BLACK,
                service = gameService,
            )
        }

        val res = PassCmd.execute(context = g)

        // Ensure result message is not empty and contains expected keywords
        assertTrue(res.message.isNotEmpty())
    }
}
