package pt.isel.reversi.cli

import pt.isel.reversi.utils.Config
import pt.rafap.ktflag.style.Colors

/**
 * Configuration holder for CLI-specific settings including welcome messages and text colors.
 * Manages display colors for different message types and command prompt styling.
 *
 * @property map The underlying configuration map with string keys and values.
 */
class CliConfig(override val map: Map<String, String>) : Config {

    val WELCOME_MESSAGE = map["WELCOME_MESSAGE"] ?: "Welcome to Reversi!"
    val PROMPT = map["PROMPT"] ?: "> "
    val PROMPT_COLOR = map["PROMPT_COLOR"] ?: Colors.PURPLE
    val TEXT_COLOR = map["TEXT_COLOR"] ?: Colors.WHITE
    val ERROR_COLOR = map["ERROR_COLOR"] ?: Colors.RED
    val WARNING_COLOR = map["WARNING_COLOR"] ?: Colors.YELLOW
    val INFO_COLOR = map["INFO_COLOR"] ?: Colors.CYAN
    val HELP_USAGE_COLOR = map["HELP_USAGE_COLOR"] ?: Colors.GREEN
    val HELP_ALIAS_COLOR = map["HELP_ALIAS_COLOR"] ?: Colors.BLUE
    val HELP_DESC_COLOR = map["HELP_DESC_COLOR"] ?: Colors.WHITE

    /**
     * Returns the default configuration entries for CLI settings.
     *
     * @return A map of default CLI configuration key-value pairs.
     */
    override fun getDefaultConfigFileEntries(): Map<String, String> {
        return mapOf(
            "WELCOME_MESSAGE" to WELCOME_MESSAGE,
            "PROMPT" to PROMPT,
            "PROMPT_COLOR" to PROMPT_COLOR,
            "TEXT_COLOR" to TEXT_COLOR,
            "ERROR_COLOR" to ERROR_COLOR,
            "WARNING_COLOR" to WARNING_COLOR,
            "INFO_COLOR" to INFO_COLOR,
            "HELP_USAGE_COLOR" to HELP_USAGE_COLOR,
            "HELP_ALIAS_COLOR" to HELP_ALIAS_COLOR,
            "HELP_DESC_COLOR" to HELP_DESC_COLOR,
        )
    }
}