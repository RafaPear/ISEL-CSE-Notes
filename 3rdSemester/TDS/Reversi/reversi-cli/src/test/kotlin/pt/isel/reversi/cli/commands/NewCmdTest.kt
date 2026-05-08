package pt.isel.reversi.cli.commands

import pt.isel.reversi.cli.cleanup
import pt.rafap.ktflag.cmd.CommandResultType
import kotlin.test.Test

class NewCmdTest {

    @Test
    fun `Test NewCmd simple execution`() {
        cleanup {
            val args = "#".split(" ").toTypedArray()
            val result = NewCmd.executeWrapper(*args, context = null)
            assert(result.type == CommandResultType.SUCCESS) {
                "Expected SUCCESS but got ${result.type} with message: ${result.message}"
            }
        }
    }

    @Test
    fun `Test NewCmd with name execution`() {
        cleanup {
            val args = "# lalala".split(" ").toTypedArray()
            val result = NewCmd.executeWrapper(*args, context = null)
            assert(result.type == CommandResultType.SUCCESS) {
                "Expected SUCCESS but got ${result.type} with message: ${result.message}"
            }
        }
    }

    @Test
    fun `Test NewCmd fails execution by arguments`() {
        cleanup {
            val args = emptyArray<String>()
            val result = NewCmd.executeWrapper(*args, context = null)
            assert(result.type == CommandResultType.INVALID_ARGS) {
                "Expected INVALID_ARGS but got ${result.type} with message: ${result.message}"
            }
        }
    }
}