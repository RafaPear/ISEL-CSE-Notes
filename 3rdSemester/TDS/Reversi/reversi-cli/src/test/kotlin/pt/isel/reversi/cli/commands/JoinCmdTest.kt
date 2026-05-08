package pt.isel.reversi.cli.commands

import pt.isel.reversi.cli.cleanup
import pt.rafap.ktflag.cmd.CommandResultType
import kotlin.test.Test
import kotlin.test.assertFails

class JoinCmdTest {

    @Test
    fun `Test JoinCmd execution`() {
        cleanup {
            val piece = "#"
            val name = "test_player"
            NewCmd.executeWrapper(piece, name, context = null)
            val result = JoinCmd.executeWrapper(name, context = null)
            assert(result.type == CommandResultType.SUCCESS) {
                "Expected SUCCESS but got ${result.type} with message: ${result.message}"
            }
        }
    }

    @Test
    fun `Test JoinCmd execution fails on already full game`() {
        cleanup {
            val piece = "#"
            val name = "test_player"

            NewCmd.executeWrapper(piece, name, context = null)
            JoinCmd.executeWrapper(name, context = null)

            assertFails("Expected an exception when joining a full game") {
                JoinCmd.executeWrapper(name, context = null)
            }
        }
    }

    @Test
    fun `Test JoinCmd fails execution by arguments`() {
        cleanup {
            val args = emptyArray<String>()
            val result = JoinCmd.executeWrapper(*args, context = null)
            assert(result.type == CommandResultType.INVALID_ARGS) {
                "Expected INVALID_ARGS but got ${result.type} with message: ${result.message}"
            }
        }
    }
}