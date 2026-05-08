package pt.isel.reversi.cli

import pt.isel.reversi.utils.CLI_CONFIG_FILE
import pt.isel.reversi.utils.ConfigLoader

/**
 * Global CLI configuration loaded from the CLI configuration file.
 * Contains all theme colors, prompts, and text settings for the command-line interface.
 */
val CLI_CONFIG: CliConfig = ConfigLoader(CLI_CONFIG_FILE) { CliConfig(it) }.loadConfig()

/** Welcome message displayed when the CLI starts. */
val WELCOME_MESSAGE = CLI_CONFIG.WELCOME_MESSAGE

/** Command prompt string displayed to the user. */
val PROMPT = CLI_CONFIG.PROMPT

/** ANSI color code for the command prompt. */
val PROMPT_COLOR = CLI_CONFIG.PROMPT_COLOR

/** ANSI color code for regular text output. */
val TEXT_COLOR = CLI_CONFIG.TEXT_COLOR

/** ANSI color code for error messages. */
val ERROR_COLOR = CLI_CONFIG.ERROR_COLOR

/** ANSI color code for warning messages. */
val WARNING_COLOR = CLI_CONFIG.WARNING_COLOR

/** ANSI color code for informational messages. */
val INFO_COLOR = CLI_CONFIG.INFO_COLOR

/** ANSI color code for help command usage text. */
val HELP_USAGE_COLOR = CLI_CONFIG.HELP_USAGE_COLOR

/** ANSI color code for help command aliases. */
val HELP_ALIAS_COLOR = CLI_CONFIG.HELP_ALIAS_COLOR

/** ANSI color code for help command descriptions. */
val HELP_DESC_COLOR = CLI_CONFIG.HELP_DESC_COLOR
