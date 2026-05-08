package pt.isel.reversi.cli

import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.Test

class CliConfigTests {

    private fun cleanup(func: () -> Unit) {
        File(BASE_FOLDER).deleteRecursively()
        func()
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `CliConfig defaults when map empty`() {
        cleanup {

            val cfg = CliConfig(emptyMap())

            assert(cfg.WELCOME_MESSAGE == "Welcome to Reversi!")
            assert(cfg.PROMPT == "> ")
            // Colors are strings from Colors constants; just ensure non-empty
            assert(cfg.PROMPT_COLOR.isNotEmpty())
            assert(cfg.TEXT_COLOR.isNotEmpty())
            assert(cfg.ERROR_COLOR.isNotEmpty())
            assert(cfg.WARNING_COLOR.isNotEmpty())
            assert(cfg.INFO_COLOR.isNotEmpty())
            assert(cfg.HELP_USAGE_COLOR.isNotEmpty())
            assert(cfg.HELP_ALIAS_COLOR.isNotEmpty())
            assert(cfg.HELP_DESC_COLOR.isNotEmpty())
        }
    }

    @Test
    fun `CliConfig respects provided map values`() {
        cleanup {

            val m = mapOf(
                "WELCOME_MESSAGE" to "Hi",
                "PROMPT" to "$ ",
                "PROMPT_COLOR" to "P",
                "TEXT_COLOR" to "T",
                "ERROR_COLOR" to "E",
                "WARNING_COLOR" to "W",
                "INFO_COLOR" to "I",
                "HELP_USAGE_COLOR" to "U",
                "HELP_ALIAS_COLOR" to "A",
                "HELP_DESC_COLOR" to "D"
            )
            val cfg = CliConfig(m)

            assert(cfg.WELCOME_MESSAGE == "Hi")
            assert(cfg.PROMPT == "$ ")
            assert(cfg.PROMPT_COLOR == "P")
            assert(cfg.TEXT_COLOR == "T")
            assert(cfg.ERROR_COLOR == "E")
            assert(cfg.WARNING_COLOR == "W")
            assert(cfg.INFO_COLOR == "I")
            assert(cfg.HELP_USAGE_COLOR == "U")
            assert(cfg.HELP_ALIAS_COLOR == "A")
            assert(cfg.HELP_DESC_COLOR == "D")
        }
    }
}
