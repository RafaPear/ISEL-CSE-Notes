package pt.isel.reversi.core

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.*
import pt.isel.reversi.core.storage.GameState
import pt.isel.reversi.storage.Storage

/**
 * Lightweight test/dummy implementation of [Game] used for data access and integration tests.
 *
 * Only acts as a structural carrier for required properties; behavioural methods are left as TODOs
 * so they surface if accidentally invoked in logic outside targeted tests. Use the nested helper
 * subclasses to build simple game states for tests (empty, one player or two players).
 *
 * Note: This class is intentionally minimal and not suitable for exercising game logic.
 */
@Suppress("unused")
data class Game(
    val storage: Storage<String, GameState, String>,
    val target: Boolean,
    val currGameName: String?,
    val gameState: GameState?,
    val countPass: Int = 0,
) {

    constructor() : this(
        storage = STORAGE,
        target = false,
        currGameName = null,
        gameState = null,
    )

    /**
     * Ensures that the game has started by checking if the game state and players are initialized.
     * @return The current game state if the game has started.
     * @throws InvalidGameException if the game is not started yet (game state is null or players list is empty).
     */
    private fun requireStartedGame(): GameState {
        if (gameState == null || gameState.players.isEmpty())
            throw InvalidGameException(
                message = "Game is not started yet."
            )
        return gameState
    }

    /**
     * Checks if it's the player's turn in a not local game.
     * @param gs The game state to check.
     * @throws InvalidPlayException if it's not the player's turn.
     */
    private fun checkTurnOnNotLocalGame(gs: GameState) {
        if (gs.players.size == 1 && gs.players[0].type != gs.lastPlayer.swap()) {
            throw InvalidPlayException("It's not your turn")
        }
    }

    /**
     * Plays a move at the specified coordinate.
     * Saves the piece to data access if the game is not local.
     * Only check player turn if it is a not local game.
     * And resets the pass count to 0.
     * @param coordinate The (row, column) coordinate for the move.
     * @return The new game state after the move.
     * @throws InvalidPlayException if it's not the player's turn or if the play is invalid.
     * @throws IllegalArgumentException if the position is out of bounds.
     * @throws InvalidGameException if the game is not started yet (board or players are null,empty).
     * @throws InvalidFileException if there is an error saving the game state.
     */
    fun play(coordinate: Coordinate): Game {
        val gs = requireStartedGame()

        checkTurnOnNotLocalGame(gs)

        val piece = Piece(coordinate, gs.lastPlayer.swap())

        val newBoard = GameLogic.play(gs.board, myPiece = piece)

        val refreshPlayers = gs.players.map { it.refresh(newBoard) }

        val newGameState = GameState(
            lastPlayer = piece.value,
            board = newBoard,
            players = refreshPlayers
        )

        if (currGameName != null) {
            saveOnlyPlay(newGameState)
        }

        return this.copy(
            gameState = newGameState,
            countPass = 0
        )
    }


    /**
     * Sets the target mode for the game.
     * @param target True to enable target mode.
     * @return The updated game state.
     */
    fun setTargetMode(target: Boolean): Game =
        this.copy(target = target)

    /** Gets the available plays for the current player.
     * If it is not a local game, and it is not the player's turn, returns an empty list.
     * @return List of available plays.
     * @throws InvalidGameException if the game is not started yet (board is null).
     */
    fun getAvailablePlays(): List<Coordinate> {
        val gs = requireStartedGame()

        try {
            checkTurnOnNotLocalGame(gs)
        } catch (_: EndGameException) {
            return emptyList()
        }

        return GameLogic.getAvailablePlays(
            board = gs.board,
            myPieceType = gs.lastPlayer.swap()
        )
    }

    /**
     * Passes the turn to the next player.
     * Saves the game state if the game is not local.
     * Only check player turn if it is a not local game.
     * @return The new game state after passing the turn.
     * @throws EndGameException if both players have passed consecutively, ending the game.
     * @throws InvalidGameException if the game is not started yet (board or players are null,empty).
     * @throws InvalidFileException if there is an error saving the game state.
     */
    fun pass(): Game {
        var gs = requireStartedGame()

        checkTurnOnNotLocalGame(gs)

        if (countPass >= 1) {
            throw EndGameException(
                message = "Both players have passed consecutively. The game has ended."
            )
        }

        gs = gs.copy(
            lastPlayer = gs.lastPlayer.swap(),
        )

        if (currGameName != null) {
            saveOnlyPlay(gs)
        }

        return this.copy(
            gameState = gs,
            countPass = countPass + 1
        )
    }

    /**
     * Refreshes the game state from storage (board and last player).
     * Updates players to reflect the current board state.
     * Increments the pass count if the board is unchanged but the last player has changed.
     * @return The refreshed game state.
     * @throws InvalidGameException if the game is not started yet (board or players are null,empty).
     * @throws InvalidFileException if there is an error loading the game state from storage.
     */
    fun refresh(): Game {
        val gs = requireStartedGame()

        if (currGameName == null) return this

        val loadedState = storage.load(currGameName) ?: throw InvalidFileException(
            message = "Failed to load game state from storage: $currGameName"
        )

        return this.copy(
            gameState = loadedState.copy(players = gs.players.map { it.refresh(loadedState.board) }),
            countPass =
                if (loadedState.board == gs.board
                    && loadedState.lastPlayer != gs.lastPlayer
                )
                    countPass + 1
                else 0
        )
    }

    /**
     * Saves the current game state to storage.
     * Saves the player in storage if not already present (makes available this player for future loads).
     * It is recommended to use this method only to save the game at the end.
     * Only applicable for not local games (players size must be 1).
     * @throws InvalidGameException if the game is local or not started yet.
     * @throws InvalidFileException if the current game name is null.
     */
    fun saveGame() {
        val gs = requireStartedGame()
        if (gs.players.size != 1)
            throw InvalidGameException(
                message = "Only a not local game can be saved (players size must be 1)"
            )

        if (currGameName == null)
            throw InvalidFileException("Name of the current game is null")

        var playersInStorage = storage.load(currGameName)?.players ?: emptyList()

        if (gs.players[0] !in playersInStorage) {
            playersInStorage = playersInStorage + gs.players[0]
        }

        try {
            storage.save(
                id = currGameName,
                obj = gs.copy(
                    players = playersInStorage
                )
            )
        } catch (_: EndGameException) {
            storage.new(
                id = currGameName,
            ) {
                gs.copy(
                    players = playersInStorage
                )
            }
        }
    }

    /**
     * Saves only the play-related state (board and last player) to storage.
     * Keeps the existing players in storage unchanged.
     * It is recommended to use this method during gameplay to save progress.
     * Only applicable for not local games (players size must be 1).
     * @param gs The current game state to save.
     * @throws InvalidGameException if the game is local or not started yet.
     * @throws InvalidFileException if the current game name is null or loading fails.
     */
    fun saveOnlyPlay(gs: GameState) {

        if (gs.players.size != 1)
            throw InvalidGameException(
                message = "Only a not local game can be saved (players size must be 1)"
            )

        if (currGameName == null)
            throw InvalidFileException("Name of the current game is null")

        val ls = storage.load(currGameName) ?: throw InvalidFileException(
            message = "Failed to load game state from storage: $currGameName"
        )

        storage.save(
            id = currGameName,
            obj = gs.copy(
                players = ls.players
            )
        )
    }
}


/**
 * Starts a new game.
 * It is recommended to use this method only to create a not local game.
 * If is not a local game makes available the opponent player in storage for future loads.
 * @param side The side length of the board.
 * @param players The list of players.
 * @param firstTurn The piece type of the player who goes first can omit to use the default.
 * @param currGameName The current game name can omit to create a local game.
 * @return The new game state.
 * @throws InvalidGameException if no players are provided.
 * @throws Exception if already exists a game with the same name in storage.
 */
fun startNewGame(
    side: Int = BOARD_SIDE,
    players: List<Player>,
    firstTurn: PieceType = First_Player_TURN,
    currGameName: String? = null,
): Game {
    if (players.isEmpty())
        throw InvalidGameException(
            "Need minimum one player to start the game"
        )

    val board = Board(side).startPieces()

    val gs = GameState(
        board = board,
        players = players.map { it.refresh(board) },
        lastPlayer = firstTurn.swap()
    )

    if (currGameName != null && gs.players.size == 1) {
        val newGS = gs.copy(
            players = listOf(gs.players[0].swap()),
        )

        STORAGE.new(currGameName) { newGS }

        return Game(
            storage = STORAGE,
            target = false,
            gameState = gs,
            currGameName = currGameName,
        )
    }

    return Game(
        storage = STORAGE,
        target = false,
        gameState = gs,
        currGameName = currGameName,
    )
}

/**
 * Loads an existing game from storage.
 * It is recommended to use this method only connecting to a not local game.
 * Ensures that the player with the specified piece type is included in the loaded game.
 * Removes the player from storage to avoid conflicts in future loads.
 * @param gameName The name of the game to load.
 * @return The loaded game state.
 * @throws InvalidFileException if there is an error loading the game state.
 * @throws InvalidPieceInFileException if the specified piece type is not found in the loaded game.
 */
fun loadGame(
    gameName: String
): Game {
    val storage = STORAGE
    val loadedState = storage.load(gameName)
        ?: throw InvalidFileException(
            message = "$gameName does not exist"
        )

    val myPieceType =
        if (loadedState.players.isNotEmpty())
            loadedState.players[0].type
        else
            throw InvalidPieceInFileException(
                message = "No players available in the loaded game: $gameName.",
            )

    val gs = loadedState.copy(
        players = loadedState.players.find { it.type == myPieceType }?.let {
            listOf(it)
        } ?: throw InvalidPieceInFileException(
            message = "Player with piece type $myPieceType is not available in the loaded game: $gameName.",
        ),
    )

    storage.save(
        id = gameName,
        obj = gs.copy(
            players = loadedState.players - gs.players[0]
        )
    )

    return Game(
        storage = storage,
        target = false,
        gameState = gs,
        currGameName = gameName,
    )
}
