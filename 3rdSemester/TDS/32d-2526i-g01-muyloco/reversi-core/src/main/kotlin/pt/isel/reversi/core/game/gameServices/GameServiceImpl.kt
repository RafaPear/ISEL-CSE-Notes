package pt.isel.reversi.core.game.gameServices

import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.gameState.GameState

/**
 * Service interface for managing game state persistence and retrieval operations.
 * Provides methods for saving, loading, and managing game states across different storage backends.
 */
interface GameServiceImpl {
    /**
     * Gets the name of the storage type being used by this service.
     * @return A string identifying the storage implementation (e.g., "FileStorage", "MongoDBStorage").
     */
    fun getStorageTypeName(): String

    /**
     * Checks if all required players have joined the game.
     * @param game The game instance to check.
     * @return True if both players are present, false otherwise.
     */
    suspend fun hasAllPlayers(game: Game): Boolean

    /**
     * Refreshes the game state by loading the latest version from storage.
     * Updates the game instance with any changes made by other players.
     * @param game The current game instance.
     * @return An updated Game instance with refreshed state.
     */
    suspend fun refresh(game: Game): Game

    /**
     * Loads the base game state from storage without game instance wrapping.
     * @param game The game instance containing the game name to load.
     * @return The refreshed GameState, or null if no changes were made.
     */
    suspend fun refreshBase(game: Game): GameState?

    /**
     * Directly loads a game state from storage by its identifier.
     * @param id The unique identifier of the game to load.
     * @return The loaded GameState, or null if not found.
     */
    suspend fun hardLoad(id: String): GameState?

    /**
     * Directly saves a game state to storage under the given identifier.
     * @param id The unique identifier for the game.
     * @param gameState The game state to save.
     */
    suspend fun hardSave(id: String, gameState: GameState)

    /**
     * Saves the final game state at the end of a game session.
     * Handles cleanup and proper storage of winner information.
     * @param game The completed game to save.
     */
    suspend fun saveEndGame(game: Game)

    /**
     * Saves only the board state and last player to storage.
     * Used during gameplay to persist moves without modifying player information.
     * @param gameName The name of the game to update.
     * @param gameState The current game state with updated board.
     */
    suspend fun saveOnlyBoard(gameName: String?, gameState: GameState?)

    /**
     * Runs a health check on the storage system to verify it's functioning correctly.
     * Creates, reads, updates, and deletes a test game state.
     * @throws pt.isel.reversi.core.exceptions.BadStorage if the health check fails.
     */
    suspend fun runStorageHealthCheck()

    /**
     * Creates a new game entry in storage.
     * @param gameName The unique name for the new game.
     * @param gameStateProvider A factory function that produces the initial game state.
     * @throws Exception if a game with this name already exists.
     */
    suspend fun new(gameName: String, gameStateProvider: () -> GameState)

    /**
     * Deletes a game from storage.
     * @param gameName The name of the game to delete.
     */
    suspend fun delete(gameName: String)

    /**
     * Closes the service and releases any held resources.
     * Should be called when the service is no longer needed.
     */
    suspend fun closeService()
    suspend fun getAllGameNames(): List<String>
}