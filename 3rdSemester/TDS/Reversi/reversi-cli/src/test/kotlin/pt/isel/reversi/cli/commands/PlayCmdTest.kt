package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.Player
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.startNewGame
import pt.rafap.ktflag.cmd.CommandResultType
import kotlin.test.Test

class PlayCmdTest {

    @Test
    fun `Test PlayCmd execution`() {
        val result = PlayCmd.executeWrapper(
            "3", "4",
            context = startNewGame(players = listOf(Player(PieceType.BLACK)))
        )
        assert(result.type == CommandResultType.SUCCESS) {
            "Expected SUCCESS but got ${result.type} with message: ${result.message}"
        }
    }

    @Test
    fun `Test PlayCmd execution fails on null game`() {
        val result = PlayCmd.executeWrapper("1", "4",context = null)
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