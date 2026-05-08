Module reversi-cli

This module provides a small, scriptable command-line interface to play the Reversi game using the core model and the
local storage implementation. It depends on `pt.rafap.ktflag` to implement a compact, testable command framework.

![Cli Architecture](../images/UML_Structure_CLI.png)

#Package pt.isel.reversi.cli

## Overview

Hosts the CLI loop, command parsing and rendering utilities. The CLI is designed to be minimal and driven by plain
standard input/output which makes it easy to script and test.

#Package pt.isel.reversi.cli.commands

## Overview

Contains concrete command handlers used by the CLI. Each command is implemented as an object extending the
`CommandImpl<Game>` contract provided by `KtFlag`.

### Commands implemented
- NewCmd — create a new game (specify first player and optional name)
- JoinCmd — join an existing named game
- PlayCmd — play a move at (row, col)
- PassCmd — pass the current turn
- ShowCmd — render the current board and scores
- ExitCmd — terminate the application

## Example command structure

The command objects define `CommandInfo` and implement an `execute` method that receives the parsed arguments and a
`Game?` context. New commands can be added by defining an object and registering it with the `CLI` parser in
`Main.kt`.

> Note: The CLI uses `KtFlag` (https://github.com/rafapear/KtFlag) to reduce boilerplate for argument parsing.
