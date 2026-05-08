package pt.isel.reversi.core

import kotlinx.coroutines.test.runTest
import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.gameState.GameState
import pt.isel.reversi.core.gameState.MatchPlayers
import pt.isel.reversi.core.gameState.Player
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.core.storage.StorageParams
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFails

class GameStateFileStorageTest {
    val storageParams = StorageParams.FileStorageParams("test-saves")
    val storage = GameStorageType.FILE_STORAGE.storage(storageParams)

    val defaultGameState = GameState(
        players = MatchPlayers(
            Player(PieceType.BLACK, name = "Player 1"),
            Player(PieceType.WHITE, name = "Player 2")
        ),
        lastPlayer = PieceType.BLACK,
        board = Board(8).startPieces()
    )

    @BeforeTest
    @AfterTest
    fun cleanup() {
        File("test-saves").deleteRecursively()
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `Run new at an already existing id fails`() = runTest {

        assertFails {
            storage.new(1.toString()) {
                defaultGameState
            }
            storage.new(1.toString()) {
                defaultGameState
            }
        }

    }

    @Test
    fun `Run save at a non existing id fails`() = runTest {

        assertFails {
            storage.save(1.toString(), defaultGameState)
        }

    }

    @Test
    fun `Run load at a non existing id returns null`() = runTest {

        val gs = storage.load(1.toString())
        assert(gs == null)
    }

    @Test
    fun `Run new and load works`() = runTest {

        val gs1 = storage.new(1.toString()) { defaultGameState }
        val gs2 = storage.load(1.toString())

        assert(gs1 == gs2)

    }

    @Test
    fun `Run new, save and load works`() = runTest {

        val gs1 = storage.new(1.toString()) {
            defaultGameState
        }.copy(lastPlayer = PieceType.BLACK)

        storage.save(1.toString(), gs1)

        val gs2 = storage.load(1.toString())

        assert(gs1 == gs2)

    }

    @Test
    fun `Run new, delete and load returns null`() = runTest {
        storage.new(1.toString()) { defaultGameState }
        storage.delete(1.toString())
        val gs = storage.load(1.toString())

        assert(gs == null)
    }


    @Test
    fun `Run delete at a non existing id fails`() = runTest {
        assertFails {
            storage.delete(1.toString())
        }
    }
}


