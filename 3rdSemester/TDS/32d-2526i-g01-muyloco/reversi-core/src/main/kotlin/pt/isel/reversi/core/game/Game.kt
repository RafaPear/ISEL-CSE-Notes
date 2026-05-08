package pt.isel.reversi.core.game

import pt.isel.reversi.core.CoreConfig
import pt.isel.reversi.core.board.Coordinate
import pt.isel.reversi.core.board.Piece
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.exceptions.EndGame
import pt.isel.reversi.core.exceptions.ErrorType
import pt.isel.reversi.core.exceptions.InvalidGame
import pt.isel.reversi.core.exceptions.InvalidPlay
import pt.isel.reversi.core.game.gameServices.GameServiceImpl
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.loadCoreConfig
import pt.isel.reversi.utils.TRACKER

/**
 * Represents a Reversi game, managing the game state, player turns, and interactions with storage.
 * The game has two modes: local and not local.
 *
 * #### Local Game
 * In a local game, if currGameName is null.
 *
 * #### Not Local Game
 * In a not local game, currGameName is not null. The game state is saved and loaded from storage.
 *
 *
 * @property target Indicates if the game is in target mode.
 * @property currGameName The name of the current game for storage purposes.
 * @property lastModified The timestamp of the last modification to the game state in storage.
 * @property gameState The current state of the game, including the board and players.
 * @property countPass The number of consecutive passes made by players.
 * @property myPiece The piece type representing the current player.
 * @property config The core configuration for the game.
 */
data class Game(
    val target: Boolean = false,
    val currGameName: String? = null,
    val lastModified: Long? = null,
    val gameState: GameState? = null,
    val countPass: Int = 0,
    val myPiece: PieceType? = null,
    val config: CoreConfig = loadCoreConfig(),
    val service: GameServiceImpl
) {
    init {
        TRACKER.trackClassCreated(this, category = "Core.Game")
    }

    /**
     * Reloads the core configuration.
     * @return A new Game instance with the updated configuration.
     */
    fun reloadConfig(): Game = this.copy(
        config = loadCoreConfig()
    )

    fun hasStarted(): Boolean = gameState != null && gameState.players.isNotEmpty() && myPiece != null

    /**
     * Changes the player's piece type.
     * @param newType The new piece type for the player.
     * @return The updated game state with the new piece type.
     */
    fun changeMyPiece(newType: PieceType): Game = this.copy(myPiece = newType)

    /**
     * Ensures that the game has started by checking if the game state and players are initialized.
     * @return The current game state if the game has started.
     * @throws pt.isel.reversi.core.exceptions.InvalidGame if the game is not started yet (game state is null or players list is empty).
     */
    fun requireStartedGame(): GameState {
        if (gameState == null || !hasStarted()) throw InvalidGame(
            message = "Game is not started yet.", type = ErrorType.INFO
        )
        if (currGameName == null && !gameState.players.isFull()) throw InvalidGame(
            message = "Local games must have two players, but have only one.",
            type = ErrorType.ERROR
        )
        return gameState
    }

    /**
     * Checks if it's the player's turn in a not local game.
     * @param gs The game state to check.
     * @throws pt.isel.reversi.core.exceptions.InvalidPlay if it's not the player's turn.
     */
    internal fun checkTurnOnNotLocalGame(gs: GameState) {
        if (currGameName != null && myPiece != gs.lastPlayer.swap()) {
            throw InvalidPlay(
                message = "It's not your turn", type = ErrorType.INFO
            )
        }
    }

    /**
     * Validates whether the game already ended and throws if a winner is set.
     * @throws pt.isel.reversi.core.exceptions.EndGame if the game already has a winner.
     */
    internal fun gameEnded() {
        val gs = requireStartedGame()
        if (gs.winner != null) {
            throw EndGame(
                message = "The game has already ended. The winner is ${gs.winner.type.symbol} with ${gs.winner.points} points.",
                type = ErrorType.INFO
            )
        }
    }

    /**
     * Ensures both players are available either locally or in persisted storage.
     * @return True if both player slots are filled.
     * @throws pt.isel.reversi.core.exceptions.InvalidFile if loading the persisted game fails.
     */
    suspend fun hasAllPlayers(): Boolean = service.hasAllPlayers(this)

    /**
     * Plays a move at the specified coordinate.
     * Saves the piece to data access if the game is not local.
     * Only check player turn if it is a not local game.
     * And resets the pass count to 0.
     * @param coordinate The (row, column) coordinate for the move.
     * @return The new game state after the move.
     * @throws InvalidPlay if it's not the player's turn or if the play is invalid.
     * @throws IllegalArgumentException if the position is out of bounds.
     * @throws InvalidGame if the game is not started yet (board or players are null,empty).
     * @throws pt.isel.reversi.core.exceptions.InvalidFile if there is an error saving the game state.
     * @throws EndGame if the game has already ended.
     */
    suspend fun play(coordinate: Coordinate): Game {
        TRACKER.trackFunctionCall(customName = "Game.play", details = "coordinate=$coordinate", category = "Core.Game")
        val gs = requireStartedGame()
        gameEnded()
        if (!hasAllPlayers()) {
            throw InvalidPlay(
                message = "Cannot play until all players have joined the game.", type = ErrorType.INFO
            )
        }

        checkTurnOnNotLocalGame(gs)

        val piece = Piece(coordinate, gs.lastPlayer.swap())

        val newBoard = GameLogic.play(gs.board, myPiece = piece)

        val refreshPlayers = gs.players.refreshPlayers(newBoard)

        val newGameState = GameState(
            lastPlayer = piece.value,
            board = newBoard,
            players = refreshPlayers,
            winner = gs.winner
        )

        if (currGameName != null) {
            saveOnlyBoard(newGameState)
        }

        return this.copy(
            gameState = newGameState, countPass = 0
        )
    }

    /**
     * Sets the target mode for the game.
     * @param target True to enable target mode.
     * @return The updated game state.
     */
    fun setTargetMode(target: Boolean): Game = this.copy(target = target)

    /**
     * Gets the available plays for the current player.
     * If it is not a local game, and it is not the player's turn, returns an empty list.
     * @return List of available plays.
     * @throws InvalidGame if the game is not started yet (board is null).
     */
    fun getAvailablePlays(): List<Coordinate> {
        val gs = requireStartedGame()

        try {
            checkTurnOnNotLocalGame(gs)
        } catch (_: Exception) {
            return emptyList()
        }

        return GameLogic.getAvailablePlays(
            board = gs.board, myPieceType = gs.lastPlayer.swap()
        )
    }

    /**
     * Passes the turn to the next player.
     * Saves the game state if the game is not local.
     * Only check player turn if it is a not local game.
     * @return The new game state after passing the turn.
     * @throws EndGame if both players have passed consecutively, ending the game.
     * @throws InvalidGame if the game is not started yet (board or players are null,empty).
     * @throws pt.isel.reversi.core.exceptions.InvalidFile if there is an error saving the game state.
     * @throws InvalidPlay if there are available plays and passing is not allowed.
     * @throws EndGame if the game has already ended.
     */
    suspend fun pass(): Game {
        TRACKER.trackFunctionCall(customName = "Game.pass", category = "Core.Game")
        var gs = requireStartedGame()
        gameEnded()

        checkTurnOnNotLocalGame(gs)

        if (GameLogic.getAvailablePlays(board = gs.board, myPieceType = gs.lastPlayer.swap())
                .isNotEmpty()
        ) throw InvalidPlay(
            message = "There are available plays, cannot pass the turn", type = ErrorType.INFO
        )

        if (countPass >= 1) {
            gs = gs.copy(
                winner = when {
                    gs.board.totalBlackPieces > gs.board.totalWhitePieces -> Player(
                        PieceType.BLACK,
                        points = gs.board.totalBlackPieces
                    )

                    gs.board.totalWhitePieces > gs.board.totalBlackPieces -> Player(
                        PieceType.WHITE,
                        points = gs.board.totalWhitePieces
                    )

                    else -> throw EndGame(
                        message = "The game has ended in a draw.", type = ErrorType.INFO
                    )
                }
            )
        }

        gs = gs.copy(
            lastPlayer = gs.lastPlayer.swap(),
        )

        if (currGameName != null) {
            saveOnlyBoard(gs)
        }

        return this.copy(
            gameState = gs, countPass = countPass + 1
        )
    }

    /**
     * Refreshes the game state from storage (board and last player).
     * Updates players to reflect the current board state.
     * Increments the pass count if the board is unchanged but the last player has changed.
     * @return The refreshed game state.
     * @throws InvalidGame if the game is not started yet (board or players are null,empty).
     * @throws pt.isel.reversi.core.exceptions.InvalidFile if there is an error loading the game state from storage.
     */
    suspend fun refresh(): Game = service.refresh(this)

    suspend fun delete() {
        if (currGameName == null) return
        service.delete(currGameName)
    }

    /**
     * Saves the current game state to storage.
     * Saves the player in storage if not already present (makes available this player for future loads).
     * It is recommended to use this method only to save the game at the end.
     * Only applicable for not local games (players size must be 1).
     * @throws InvalidGame if the game is local or not started yet.
     * @throws pt.isel.reversi.core.exceptions.InvalidFile if the current game name is null.
     */
    suspend fun saveEndGame() = service.saveEndGame(this)

    /**
     * Saves only the play-related state (board and last player) to storage.
     * Keeps the existing players in storage unchanged.
     * It is recommended to use this method during gameplay to save progress.
     * Only applicable for not local games (players size must be 1).
     * @param gameState The current game state to save.
     * @throws InvalidGame if the game is local or not started yet.
     * @throws pt.isel.reversi.core.exceptions.InvalidFile if the current game name is null or loading fails.
     */
    suspend fun saveOnlyBoard(gameState: GameState?) =
        service.saveOnlyBoard(currGameName, gameState)

    /**
     * Closes the storage connection.
     */
    suspend fun closeStorage() = service.closeService()
}