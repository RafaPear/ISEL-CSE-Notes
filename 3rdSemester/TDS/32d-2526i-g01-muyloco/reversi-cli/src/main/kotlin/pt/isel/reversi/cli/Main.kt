package pt.isel.reversi.cli

import pt.isel.reversi.cli.commands.*
import pt.isel.reversi.core.board.PieceType
import pt.rafap.ktflag.cmd.args.CommandArg
import pt.rafap.ktflag.cmd.args.CommandArgsParser

/**
 * Command-line arguments: debug flag enables internal debug commands and extra diagnostics.
 */
private val debugArg = CommandArg(
    name = "Debug",
    description = "Enables debug mode with additional commands.",
    aliases = arrayOf("--debug", "-d"),
    isRequired = false,
    returnsValue = false,
)

val allCommands = arrayOf(
    PlayCmd,
    PassCmd,
    NewCmd,
    JoinCmd,
    ExitCmd,
    ShowCmd,
    RefreshCmd,
    TargetCmd
)

val debugCommands = arrayOf(
    DebugCmd,
    ListGamesCmd
)

val pieceTypes = PieceType.entries.joinToString("|") { it.symbol.toString() }

/**
 * Main entry point for the Reversi CLI application. It configures the available commands
 * and starts the interactive loop.
 */
fun main(args: Array<String>) {
    val parser = CommandArgsParser(
        debugArg
    )

    val parsed = parser.parseArgs(*args)

    val debug = parsed[debugArg] != null

    val cli = CLI(
        commands = allCommands,
        debug = debug,
        debugCommands = debugCommands,
    )
    cli.startLoop()
}