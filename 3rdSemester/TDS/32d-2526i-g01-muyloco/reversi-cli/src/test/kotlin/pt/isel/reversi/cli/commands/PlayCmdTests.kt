package pt.isel.reversi.cli.commands

import kotlinx.coroutines.test.runTest
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.game.startNewGame
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.utils.BASE_FOLDER
import pt.rafap.ktflag.cmd.CommandResultType
import java.io.File
import kotlin.test.*

class PlayCmdTests {

    @BeforeTest
    @AfterTest
    fun cleanup() {
        File(BASE_FOLDER).deleteRecursively()
    }


    @Test
    fun `parseCoordinateArgs accepts separated numbers`() {
        val c = PlayCmd.parseCoordinateArgs(listOf("3", "5"))
        assertEquals(3, c?.row)
        assertEquals(5, c?.col)
    }

    @Test
    fun `parseCoordinateArgs accepts combined numeric and letter (1A)`() {
        val c = PlayCmd.parseCoordinateArgs(listOf("1A"))
        assertEquals(1, c?.row)
        // 'A' -> col 1
        assertEquals(1, c?.col)
    }

    @Test
    fun `parseCoordinateArgs accepts combined digits (15)`() {
        val c = PlayCmd.parseCoordinateArgs(listOf("15"))
        assertEquals(1, c?.row)
        assertEquals(5, c?.col)
    }

    @Test
    fun `parseCoordinateArgs returns null for invalid inputs`() {
        assertNull(PlayCmd.parseCoordinateArgs(listOf("x")))
        assertNull(PlayCmd.parseCoordinateArgs(listOf("")))
        assertNull(PlayCmd.parseCoordinateArgs(listOf("1")))
    }

    @Test
    fun `Test PlayCmd execution`() = runTest {
        val result = PlayCmd.executeWrapper(
            "3", "4",
            context = startNewGame(
                side = 8,
                players = MatchPlayers(Player(PieceType.BLACK), Player(PieceType.WHITE)),
                firstTurn = PieceType.BLACK,
                service = EmptyGameService()
            )
        )
        assert(result.type == CommandResultType.SUCCESS) {
            "Expected SUCCESS but got ${result.type} with message: ${result.message}"
        }
    }

    @Test
    fun `Test PlayCmd execution fails on null game`() {
        val result = PlayCmd.executeWrapper("1", "4", context = null)
        assert(result.type == CommandResultType.ERROR) {
            "Expected ERROR but got ${result.type} with message: ${result.message}"
        }
    }

    @Test
    fun `Test PlayCmd fails execution by arguments`() {
        val result = PlayCmd.executeWrapper(context = null)
        assert(result.type == CommandResultType.INVALID_ARGS) {
            "Expected INVALID_ARGS but got ${result.type} with message: ${result.message}"
        }
    }
}