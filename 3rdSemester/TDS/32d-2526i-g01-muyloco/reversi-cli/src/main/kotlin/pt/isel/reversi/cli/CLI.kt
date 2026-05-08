package pt.isel.reversi.cli

import pt.isel.reversi.core.game.Game
import pt.isel.reversi.utils.TRACKER
import pt.rafap.ktflag.CommandParser
import pt.rafap.ktflag.ParserConfig
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandResultType
import pt.rafap.ktflag.style.Colors.colorText

/**
 * Simple command-line interface coordinator.
 *
 * Holds the set of available `CommandImpl<Game>` objects, manages an optional debug mode
 * and runs the interactive read-eval-print loop that dispatches commands to the parser.
 *
 * @property commands Array of command implementations available in the CLI.
 * @property debug Whether debug mode is enabled.
 * @property debugCommands Additional commands available only when debug mode is enabled.
 */
class CLI(
    val commands: Array<CommandImpl<Game>>,
    val debug: Boolean = false,
    debugCommands: Array<CommandImpl<Game>> = arrayOf(),
) {
    init {
        TRACKER.trackClassCreated(this, category = "CLI.Core")
    }

    private fun log(message: String) {
        println(colorText(message))
    }

    val finalCommands = commands + if (debug) debugCommands else arrayOf()

    val parserConfig = ParserConfig<Game>(
        prompt = PROMPT,
        promptColor = PROMPT_COLOR,
        errorColor = ERROR_COLOR,
        warningColor = WARNING_COLOR,
        infoColor = INFO_COLOR,
        helpUsageColor = HELP_USAGE_COLOR,
        helpAliasColor = HELP_ALIAS_COLOR,
        helpDescColor = HELP_DESC_COLOR,
    )

    val parser = CommandParser(*finalCommands, config = parserConfig)

    /**
     * Entry point for the CLI version of the Reversi game.
     * Initializes the board and command parser, and handles user input in an interactive loop.
     */
    fun startLoop() {
        var game: Game? = null

        println(colorText(WELCOME_MESSAGE, TEXT_COLOR))
        while (true) {
            val input = parser.readInput()
            game = parseInput(input, game)
        }
    }

    /**
     * Parses user input and executes the corresponding command.
     * @param input The user input string to parse.
     * @param context The current game context, or null if no game is active.
     * @return The updated game context after command execution, or the original context if parsing failed.
     */
    fun parseInput(input: String, context: Game? = null): Game? {
        TRACKER.trackFunctionCall(customName = "CLI.parseInput", details = "input=$input", category = "CLI.Core")
        var game: Game? = context

        val result = parser.parseInputToResult(input, game)

        if (result == null) {
            println(colorText("[ERROR] Unknown command", ERROR_COLOR))
            return game
        }

        when {
            result.type == CommandResultType.UNKNOWN_COMMAND -> parser.printUnknownCommandError(input, result)

            result.type != CommandResultType.SUCCESS -> result.printError(parserConfig)

            result.type == CommandResultType.SUCCESS && debug -> {
                log(result.message); game = result.result
            }

            result.result != null -> game = result.result
        }

        return game
    }
}
