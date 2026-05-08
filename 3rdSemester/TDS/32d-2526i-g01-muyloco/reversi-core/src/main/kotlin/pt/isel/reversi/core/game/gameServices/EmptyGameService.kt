package pt.isel.reversi.core.game.gameServices

import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.gameState.GameState

/**
 * Empty implementation of GameServiceImpl that performs no storage operations.
 * Used primarily for testing scenarios where storage interaction is not needed.
 * All methods are no-ops and return default or dummy values.
 */
class EmptyGameService : GameServiceImpl {
    override fun getStorageTypeName(): String {
        return "FakeStorage"
    }

    override suspend fun hasAllPlayers(game: Game): Boolean {
        return true
    }

    override suspend fun refresh(game: Game): Game {
        return game
    }

    override suspend fun refreshBase(game: Game): GameState? {
        return game.gameState
    }

    override suspend fun hardLoad(id: String): GameState? = null

    override suspend fun hardSave(id: String, gameState: GameState) {
        // No-op
    }

    override suspend fun saveEndGame(game: Game) {
        // No-op
    }

    override suspend fun saveOnlyBoard(gameName: String?, gameState: GameState?) {
        // No-op
    }

    override suspend fun runStorageHealthCheck() {
        // No-op
    }

    override suspend fun delete(gameName: String) {
        // No-op
    }

    override suspend fun closeService() {
        // No-op
    }

    override suspend fun new(gameName: String, gameStateProvider: () -> GameState) {
        // No-op
    }
    override suspend fun getAllGameNames(): List<String> {
        return emptyList()
    }
}