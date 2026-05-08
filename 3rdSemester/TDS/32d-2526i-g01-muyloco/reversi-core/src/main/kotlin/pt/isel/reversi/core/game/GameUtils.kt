package pt.isel.reversi.core.game

import pt.isel.reversi.core.CoreConfig
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.*
import pt.isel.reversi.core.game.gameServices.GameServiceImpl
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.loadCoreConfig
import pt.isel.reversi.core.storage.GameStorageType.Companion.setUpStorage
import pt.isel.reversi.utils.TRACKER

fun loadStorageFromConfig() = setUpStorage(loadCoreConfig())

/**
 * Starts a new game.
 * It is recommended to use this method only to create a not local game.
 * If is not a local game makes available the opponent player in storage for future loads.
 * @param side The side length of the board (required).
 * @param players The list of players.
 * @param firstTurn The piece type of the player who goes first can omit to use the default.
 * @param currGameName The current game name can omit to create a local game.
 * @return The new game state.
 * @throws InvalidGame if no players are provided.
 * @throws InvalidNameAlreadyExists if already exists a game with the same name in storage.
 */
suspend fun startNewGame(//TODO: ele esá a possibilitar criar jogos não locais onde os 2 players têm o mesmo tipo
    //acontece quando meto 1 player com tipo preto, e o fristTurn é o oposto e outra pessoa conecta as duas ficam com tipo branco
    //pq o myPice é criado com referencia no fristTurn
    side: Int,
    players: MatchPlayers,
    firstTurn: PieceType,
    currGameName: String? = null,
    service: GameServiceImpl
): Game {
    TRACKER.trackFunctionCall(customName = "startNewGame", details = "gameName=$currGameName", category = "Core.Game")
    if (players.isEmpty()) throw InvalidGame(
        "Need minimum one player to start the game", ErrorType.WARNING
    )

    val board = Board(side).startPieces()

    val gs = GameState(
        players = players.refreshPlayers(board),
        lastPlayer = firstTurn.swap(),
        board = board,
        winner = null
    )

    return if (currGameName != null) {
        try {
            Game(
                target = false,
                gameState = gs,
                currGameName = currGameName,
                myPiece = firstTurn,
                service = service,
            ).also { it.service.new(currGameName) { gs } }
        } catch (_: Exception) {
            throw InvalidNameAlreadyExists(
                message = "A game with the name '$currGameName' already exists.", type = ErrorType.WARNING
            )
        }
    } else {
        Game(
            target = false,
            gameState = gs,
            currGameName = currGameName,
            myPiece = firstTurn,
            service = service,
        )
    }
}

/**
 * Loads an existing game from storage.
 * It is recommended to use this method only connecting to a not local game.
 * Ensures that the player with the specified piece type is included in the loaded game.
 * Removes the player from storage to avoid conflicts in future loads.
 * @param gameName The name of the game to load.
 * @return The loaded game state.
 * @throws InvalidFile if there is an error loading the game state.
 * @throws InvalidPieceInFile if the specified piece type is not found in the loaded game.
 */
suspend fun loadAndEntryGame(
    gameName: String,
    playerName: String? = null,
    desiredType: PieceType?,
    service: GameServiceImpl
): Game {
    TRACKER.trackFunctionCall(customName = "loadGame", details = "gameName=$gameName", category = "Core.Game")
    val loadedState = service.hardLoad(gameName)
        ?: throw InvalidFile(
            message = "$gameName does not exist",
            type = ErrorType.ERROR
        )

    val myPieceType = desiredType ?: loadedState.players.getFreeType()
    ?: throw InvalidPieceInFile(
        message = "No available piece types in the loaded game: $gameName.",
        type = ErrorType.WARNING
    )

    val player = Player(type = myPieceType, name = playerName ?: myPieceType.name)

    val newMatch = loadedState.players.addPlayerOrNull(player) ?: throw InvalidPieceInFile(
        message = "Player with piece type ${myPieceType.symbol} is not available in the loaded game: $gameName.",
        type = ErrorType.WARNING
    )

    val newState = loadedState.copy(players = newMatch)

    service.hardSave(
        id = gameName,
        gameState = newState
    )

    return Game(
        target = false,
        gameState = newState.copy(
            players = newState.players.refreshPlayers(newState.board),
        ),
        currGameName = gameName,
        myPiece = myPieceType,
        service = service
    )
}

/**
 * Converts the game board to a string representation with row/column labels and target markers.
 * If target mode is enabled, available plays are marked with the target character.
 * @return A formatted string representation of the board, or an error message if board is uninitialized.
 */
fun Game.stringifyBoard(): String {
    val sb = StringBuilder()
    val board = gameState?.board ?: return "Board not initialized"
    val availablePlays = if (target) getAvailablePlays() else null
    val useTarget = availablePlays != null

    for (row in 0..board.side) {
        for (col in 0..board.side) {
            val cords = Coordinate(row, col)
            when {
                row == 0 && col == 0 -> sb.append("  ")
                row == 0 -> sb.append("$col ")
                col == 0 -> sb.append("$row ")
                else -> sb.append(
                    when (useTarget && cords in availablePlays) {
                        true -> "${this.config.targetChar} "
                        false -> (board[cords]?.symbol ?: this.config.emptyChar) + " "
                    }
                )
            }
        }
        sb.appendLine()
    }
    return sb.toString()
}

/**
 * Creates a new game instance for testing purposes with specified board and player configuration.
 * @param board The board to use for the game.
 * @param players The players in the game.
 * @param myPiece The piece type of the player controlling this game instance.
 * @param currGameName Optional game name for storage (defaults to local game if null).
 * @return A new Game instance with the provided configuration.
 */
fun newGameForTest(
    board: Board,
    players: MatchPlayers,
    myPiece: PieceType,
    currGameName: String? = null,
    service: GameServiceImpl
): Game = Game(
    target = false,
    currGameName = currGameName,
    myPiece = myPiece,
    gameState = GameState(
        players = players,
        lastPlayer = myPiece,
        board = board,
        winner = null
    ),
    service = service,
    config = CoreConfig(emptyMap())
)
