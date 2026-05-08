package pt.isel.reversi.cli.commands

import pt.isel.reversi.utils.BASE_FOLDER
import pt.rafap.ktflag.cmd.CommandResultType
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class NewCmdTests {

    @BeforeTest
    @AfterTest
    fun cleanup() {
        File(BASE_FOLDER).deleteRecursively()
    }


    @Test
    fun `Test NewCmd simple execution`() {
        val args = "#".split(" ").toTypedArray()
        val result = NewCmd.executeWrapper(*args, context = null)
        assert(result.type == CommandResultType.SUCCESS) {
            "Expected SUCCESS but got ${result.type} with message: ${result.message}"
        }
    }

    @Test
    fun `Test NewCmd with name execution`() {
        val args = "# lalala".split(" ").toTypedArray()
        val result = NewCmd.executeWrapper(*args, context = null)
        assert(result.type == CommandResultType.SUCCESS) {
            "Expected SUCCESS but got ${result.type} with message: ${result.message}"
        }
    }

    @Test
    fun `Test NewCmd fails execution by arguments`() {
        val args = emptyArray<String>()
        val result = NewCmd.executeWrapper(*args, context = null)
        assert(result.type == CommandResultType.INVALID_ARGS) {
            "Expected INVALID_ARGS but got ${result.type} with message: ${result.message}"
        }
    }
}