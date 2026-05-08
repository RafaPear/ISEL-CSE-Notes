package pt.isel.reversi.core.game.gameServices

import pt.isel.reversi.core.exceptions.InvalidFile
import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.utils.LOGGER
import pt.isel.reversi.utils.TRACKER

/**
 * In-memory fake implementation of GameServiceImpl for testing purposes.
 * Stores game states in memory rather than persisting to an actual storage backend.
 * Useful for unit tests and scenarios where real storage is not available or desired.
 */
class FakeGameService : GameServiceImpl {
    private val _game = mutableListOf<Pair<String, GameState>>()

    /**
     * Provides read-only access to the in-memory game storage.
     */
    val game get() = _game.toList()

    override fun getStorageTypeName(): String = "fake"

    fun load(id: String) =
        _game.find { it.first == id }?.second

    override suspend fun delete(gameName: String) {
        _game.find { it.first == gameName }?.let {
            _game.remove(it)
        }
    }

    override suspend fun new(gameName: String, gameStateProvider: () -> GameState) {
        _game.add(
            Pair(gameName, gameStateProvider())
        )
    }

    fun save(id: String, gameState: GameState) {
        val index = _game.indexOfFirst { it.first == id }
        if (index != -1) {
            _game[index] = Pair(id, gameState)
        } else {
            _game.add(Pair(id, gameState))
        }
    }

    override suspend fun hasAllPlayers(game: Game): Boolean {
        val gs = game.requireStartedGame()
        val name = game.currGameName ?: return (gs.players.isFull())

        val loaded = load(name) ?: return false
        return (loaded.players.isFull())
    }

    override suspend fun refresh(game: Game): Game {
        TRACKER.trackFunctionCall(customName = "Game.refresh", category = "Core.Game")
        val gs = game.requireStartedGame()
        if (game.currGameName == null) return game


        val loadedState = refreshBase(game) ?: return game
        val countPassCondition = loadedState.board == gs.board && loadedState.lastPlayer != gs.lastPlayer
        val mod = game.lastModified ?: 0L

        return game.copy(
            gameState = loadedState.refreshPlayers(),
            countPass = if (countPassCondition) game.countPass + 1 else 0,
            lastModified = mod + if (game.gameState == loadedState) 0L else 1L
        )
    }

    override suspend fun refreshBase(game: Game): GameState? {
        if (game.currGameName == null) return null

        return load(game.currGameName)
    }

    override suspend fun hardLoad(id: String) = load(id)

    override suspend fun hardSave(id: String, gameState: GameState) =
        save(id, gameState)

    override suspend fun saveEndGame(game: Game) {
        TRACKER.trackFunctionCall(customName = "Game.saveEndGame", category = "Core.Game")
        val gs = game.requireStartedGame()

        val name = game.currGameName ?: return

        _game.find { it.first == game.currGameName } ?: run {
            new(
                gameName = name,
            ) { gs.copy(players = MatchPlayers()) }
            return
        }

        val loadedGs = try {
            load(game.currGameName)
        } catch (e: InvalidFile) {
            delete(game.currGameName)
            LOGGER.warning("Deleted corrupted game from storage: ${game.currGameName} due to ${e.message}")
            return
        }


        var playersInStorage = loadedGs?.players ?: MatchPlayers()

        if (loadedGs != null && loadedGs.winner != null && loadedGs.winner == gs.winner) {
            LOGGER.info("Game already ended in storage: ${game.currGameName}")
            delete(game.currGameName)
            LOGGER.info("Deleted ended game from storage: ${game.currGameName}")
            return
        }

        val myPieceTemp = game.myPiece ?: return

        playersInStorage = MatchPlayers(null, playersInStorage.getPlayerByType(myPieceTemp.swap()))

        LOGGER.info("Saving game state to storage: ${game.currGameName}")
        save(
            id = game.currGameName,
            gameState = gs.copy(
                players = playersInStorage,
            )
        )
    }

    override suspend fun saveOnlyBoard(gameName: String?, gameState: GameState?) {
        val gs = gameState ?: return

        val name = gameName ?: return

        _game.find { it.first == name } ?: run {
            try {
                new(name) { gameState }
                return@saveOnlyBoard
            } catch (_: Exception) {
                return
            }
        }

        val ls = load(id = name) ?: return


        var lsGameState = ls

        ls.players.forEachIndexed { index, player ->
            val gsPlayer = gs.players[index]
            if (gsPlayer != null && gsPlayer.name != player.name) {
                lsGameState = lsGameState.changeName(newName = gsPlayer.name, pieceType = gsPlayer.type)
            }
        }

        save(
            name,
            gs.copy(
                players = lsGameState.players,
            )
        )
    }

    override suspend fun runStorageHealthCheck() {
        // No-op for fake service
    }

    override suspend fun closeService() {
        // No-op for fake service
    }

    override suspend fun getAllGameNames(): List<String> {
        return _game.map { it.first }
    }
}