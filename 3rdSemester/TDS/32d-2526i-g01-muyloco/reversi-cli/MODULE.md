Module reversi-cli

## Overview

A scriptable, interactive command-line interface for playing Reversi. The CLI is designed to be minimal, event-driven,
and testable. It uses the KtFlag library for compact command parsing and provides a read-eval-print loop (REPL)
that manages game context and command execution.

The CLI is stateful, maintaining the current game in memory and coordinating with the storage module to load and
save games. It separates presentation logic (rendering the board) from command logic (executing moves), making it
easy to test individual commands.

![Reversi App Screenshot](../images/UML_Structure_CLI.png)

## Architecture

The module is organized into two main packages:

- **Main CLI Package** — CLI coordinator, command parsing, and rendering
- **Commands Package** — Individual command implementations

### CLI Coordinator (CLI.kt)

The main loop that:

1. Displays a welcome message
2. Creates a command parser with all registered commands
3. Repeatedly reads input and dispatches to the parser
4. Maintains the current Game context across iterations
5. Displays command results to the user

The CLI uses KtFlag's CommandParser for flexible argument parsing and colored output.

### Command Pattern

All commands extend `CommandImpl<Game>` and provide:

- `CommandInfo` metadata (title, description, aliases, usage)
- An `execute()` method that takes arguments and a game context
- A return value indicating success/failure and the new game state

This design allows commands to be:

- Tested independently
- Added/removed dynamically
- Configured with different parameters
- Composed with other commands

## Commands

### Game Management

- **NewCmd (n)** — Creates a new game with specified first player (# or @)
    - Syntax: `new # [name]`
    - Optional name saves game to persistent storage
    - Saves previous game before creating new one

- **JoinCmd (j)** — Joins an existing saved game
    - Syntax: `join <name> [#|@]`
    - Loads game from storage by name
    - Optional piece type specifies which side you control
    - Useful for multi-player or turn-based scenarios

### Gameplay

- **PlayCmd (p)** — Executes a move at specified coordinates
    - Syntax: `play (row) (col)` or `play (rowcol)`
    - Supports multiple coordinate formats:
        - `play 3 4` (two separate arguments)
        - `play 3A` (row + letter column)
        - `play 34` (row + digit column)
    - Validates move legality before executing
    - Displays updated board after move

- **PassCmd** — Passes turn when no legal moves available
    - Syntax: `pass`
    - Checks that player actually has no legal moves
    - Counts consecutive passes; ends game after two
    - Announces winner if game ends

### Game Viewing

- **ShowCmd (s)** — Displays current board and game state
    - Syntax: `show`
    - Shows ASCII board with piece positions
    - Displays both players' scores
    - Indicates current turn
    - Shows game name if applicable

- **RefreshCmd (r)** — Reloads game state from storage
    - Syntax: `refresh`
    - Useful in multi-process scenarios where another instance might have updated the game
    - Reloads from storage and redisplays board

### Game Control

- **TargetCmd** — Toggles target mode for visual feedback
    - Syntax: `target [true|false|yes|no|1|0|on|off]`
    - Enables/disables highlighting of available moves
    - Supports various boolean representations

- **ExitCmd (exit, quit, q)** — Terminates the application
    - Prompts to save current game before exit if needed
    - Persists game state to storage
    - Closes any open resources

### Debug Commands (--debug flag)

- **DebugCmd (dbg)** — Displays detailed internal state
    - Shows board representation
    - Lists player information
    - Displays configuration values
    - Lists saved games in storage folder

- **ListGamesCmd (lg)** — Lists all saved games
    - Shows files in the saves directory
    - Helpful for testing and debugging

## Configuration

The CLI reads configuration from `reversi-cli.properties`:

- `WELCOME_MESSAGE` — Message shown at startup
- `PROMPT` — Command prompt symbol
- Color configuration (`PROMPT_COLOR`, `TEXT_COLOR`, `ERROR_COLOR`, etc.)

Colors can be customized to match terminal capabilities.

## Command Execution Flow

1. User types command with arguments
2. Parser matches against registered commands
3. Command's execute() is called with arguments and current Game context
4. Command validates input and executes business logic
5. Command returns CommandResult with success/failure and new game state
6. CLI displays the result message
7. CLI updates its game context if game changed

## Integration

The CLI integrates with:

- **reversi-core** — Game logic and types
- **reversi-storage** — Persistence (loading and saving games)
- **reversi-utils** — Configuration loading and utilities
- **KtFlag** — Command parsing and rendering

The CLI is intentionally thin, delegating all business logic to the core and storage modules.

## Error Handling

Commands provide user-friendly error messages:

- "Game is not defined" — When operations require a game but none is active
- "Invalid coordinates provided" — For malformed move syntax
- "It's not your turn" — For networked games
- "There are available plays, cannot pass" — For illegal pass attempts

These messages help users understand what went wrong and how to fix it.

## Testing

Commands are designed to be testable:

- Each command can be tested independently with mock Game objects
- Command parsing can be tested without I/O
- Output rendering can be captured and verified
- Integration tests can simulate user input sequences

#Package pt.isel.reversi.cli

## Overview

Hosts the CLI loop, command parsing and rendering utilities. The CLI is designed to be minimal and driven by plain
standard input/output which makes it easy to script and test.

### Key Components

- `CLI` — Main CLI coordinator class managing the REPL loop
- `CliConfig` — Configuration for CLI appearance (colors, prompts, messages)
- `Environment` — Constants for CLI-specific paths and settings
- `Main` — Entry point for the CLI application

### Responsibilities

- Providing an interactive command-line interface for the Reversi game
- Managing the REPL (Read-Eval-Print Loop)
- Parsing and dispatching commands
- Maintaining game context across command executions
- Rendering game state to the console
- Loading CLI configuration from properties file

#Package pt.isel.reversi.cli.commands

## Overview

Contains concrete command handlers used by the CLI. Each command is implemented as an object extending the
`CommandImpl<Game>` contract provided by `KtFlag`.

### Commands implemented

- `NewCmd` — create a new game (specify first player and optional name)
    - Syntax: `new # [name]` or `new @ [name]`
    - Creates a new Reversi game with the specified first player
    - Optional name parameter saves the game to persistent storage
    - Saves the previous game before creating a new one

- `JoinCmd` — join an existing named game
    - Syntax: `join <name> [#|@]`
    - Loads a saved game by name from storage
    - Optional piece type parameter specifies which player you control
    - Useful for multiplayer or turn-based scenarios

- `PlayCmd` — play a move at (row, col)
    - Syntax: `play <row> <col>` or `play <rowcol>`
    - Supports multiple coordinate formats:
        - `play 3 4` (separate row and column)
        - `play 3A` (row + letter column)
        - `play 34` (row + digit column)
    - Validates move legality before execution
    - Updates board and displays result

- `PassCmd` — pass the current turn
    - Syntax: `pass`
    - Only valid when no legal moves are available
    - Counts consecutive passes; ends game after two consecutive passes
    - Announces winner if game ends

- `ShowCmd` — render the current board and scores
    - Syntax: `show` or `s`
    - Displays ASCII representation of the board
    - Shows both players' scores
    - Indicates current player's turn
    - Shows game name if saved

- `RefreshCmd` — reload game state from storage
    - Syntax: `refresh` or `r`
    - Reloads the current game from persistent storage
    - Useful when another process/player has made moves
    - Redisplays the updated board

- `TargetCmd` — toggle target mode for visual feedback
    - Syntax: `target [true|false|yes|no|1|0|on|off]`
    - Enables/disables highlighting of legal moves
    - Accepts various boolean representations
    - Helps visualize available moves

- `ExitCmd` — terminate the application
    - Syntax: `exit`, `quit`, or `q`
    - Prompts to save current game before exit
    - Persists game state to storage
    - Closes resources and exits cleanly

- `DebugCmd` — display detailed internal state (debug mode only)
    - Syntax: `debug` or `dbg`
    - Shows board representation details
    - Lists player information
    - Displays configuration values
    - Lists saved games in storage folder
    - Only available when `--debug` flag is used

- `ListGamesCmd` — list all saved games
    - Syntax: `listgames` or `lg`
    - Shows all game files in the saves directory
    - Displays game names available for joining
    - Helpful for discovering saved games

### Responsibilities

- Implementing game commands with validation and error handling
- Parsing command arguments in various formats
- Executing game logic through the core module
- Providing user-friendly feedback messages
- Coordinating with storage for persistence operations

## Example command structure

The command objects define `CommandInfo` and implement an `execute` method that receives the parsed arguments and a
`Game?` context. New commands can be added by defining an object and registering it with the `CLI` parser in
`Main.kt`.

### Command Result Flow

1. User enters command with arguments
2. CommandParser matches command and extracts arguments
3. Command's `execute()` method is called with arguments and current Game
4. Command validates inputs and checks game state
5. Command executes business logic via core module
6. Command returns `CommandResult` with success/failure and updated game
7. CLI displays result message to user
8. CLI updates its game context for next command

> Note: The CLI uses `KtFlag` (https://github.com/rafapear/KtFlag) to reduce boilerplate for argument parsing.
