# Reversi Board Game - Kotlin (JVM)

[![Release Tests](https://github.com/RafaPear/Reversi-Grupo1/actions/workflows/release-tests.yml/badge.svg)](https://github.com/RafaPear/Reversi-Grupo1/actions/workflows/release-tests.yml)


A modular, test-friendly implementation of the Reversi board game written in Kotlin (JVM). The project emphasizes clean
separation of concerns, explicit domain modeling, and pluggable persistence.

![Reversi Board](images/Reversi_Board_CLI.png)

## Modules
- `reversi-core` — immutable core domain model, game logic and serializers
- `reversi-storage` — simple local filesystem storage implementation (text files)
- `reversi-cli` — small command-line client to play the game

## Quick Start

On Unix / macOS:

```bash
./gradlew build
```

On Windows (cmd.exe):

```cmd
gradlew.bat build
```

Run the produced module jar (paths vary depending on build):

```cmd
java -jar reversi-cli/build/libs/reversi-cli.jar
```

## Project Structure

The project is split in three modules (core, storage and cli). See the `reversi-core`, `reversi-storage` and
`reversi-cli` folders for implementation details and tests.

## Testing

Run the test suite:

```cmd
gradlew.bat test
```

## Documentation

Generate Dokka multi-module HTML:

```cmd
gradlew.bat dokkaHtmlMultiModule
```

Output: `build/dokka/htmlMultiModule/index.html`

For module-level descriptions see the `MODULE.md` files inside each module folder which are also included in the
generated documentation.
