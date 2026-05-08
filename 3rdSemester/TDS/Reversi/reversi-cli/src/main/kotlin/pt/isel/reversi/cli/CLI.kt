package pt.isel.reversi.cli

import pt.isel.reversi.core.Game
import pt.rafap.ktflag.CommandParser
import pt.rafap.ktflag.cmd.CommandImpl
import pt.rafap.ktflag.cmd.CommandResultType
import pt.rafap.ktflag.style.Colors
import pt.rafap.ktflag.style.Colors.colorText

/**
 * Simple command-line interface coordinator.
 *
 * Holds the set of available `CommandImpl<Game>` objects, manages an optional debug mode
 * and runs the interactive read-eval-print loop that dispatches commands to the parser.
 */
class CLI(
    val commands: Array<CommandImpl<Game>>,
    val debug: Boolean = false,
    val welcomeMessage: String = "Welcome to Reversi CLI!",
    debugCommands: Array<CommandImpl<Game>> = arrayOf(),
) {
    private fun logDebug(message: String) {
        if (debug) println(colorText("[DEBUG] $message", Colors.YELLOW))
    }

    val parser = CommandParser(*(commands + debugCommands))

    /**
     * Entry point for the CLI version of the Reversi game.
     * Initializes the board and command parser, and handles user input.
     */
    fun startLoop() {
        /**
         * The current game.
         */
        var game: Game? = null

        println(colorText(welcomeMessage, Colors.INFO_COLOR))
        while (true) {
            val input = parser.readInput()
            game = parseInput(input, game)
        }
    }

    fun parseInput(input: String, context: Game? = null): Game? {
        var game: Game? = context

        val result = parser.parseInputToResult(input, game)

        if (result == null) {
            println(colorText("[ERROR] Unknown command", Colors.RED))
            return game
        }

        when {
            result.type == CommandResultType.UNKNOWN_COMMAND  -> parser.printUnknownCommandError(input, result)

            result.type != CommandResultType.SUCCESS          -> result.printError()

            result.type == CommandResultType.SUCCESS && debug -> logDebug(result.message)

            result.result != null                             -> game = result.result!!
        }
        return game
    }
}
