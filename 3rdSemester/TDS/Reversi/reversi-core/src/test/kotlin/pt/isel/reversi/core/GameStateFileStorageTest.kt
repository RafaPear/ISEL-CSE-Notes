package pt.isel.reversi.core

import pt.isel.reversi.core.board.Board
import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.storage.GameState
import java.io.File
import kotlin.test.Test
import kotlin.test.assertFails

class GameStateFileStorageTest {
    val storage = STORAGE.copy(folder = "test-saves")

    val defaultGameState = GameState(
        players = listOf(
            Player(PieceType.BLACK),
            Player(PieceType.WHITE)
        ),
        lastPlayer = PieceType.BLACK,
        board = Board(8).startPieces()
    )

    fun cleanup(func: () -> Unit) {
        File("test-saves").deleteRecursively()
        func()
        File("test-saves").deleteRecursively()
    }

    @Test
    fun `Run new at an already existing id fails`() {
        cleanup {
            assertFails {
                storage.new(1.toString()) {
                    defaultGameState
                }
                storage.new(1.toString()) {
                    defaultGameState
                }
            }
        }
    }

    @Test
    fun `Run save at a non existing id fails`() {
        cleanup {
            assertFails {
                storage.save(1.toString(), defaultGameState)
            }
        }
    }

    @Test
    fun `Run load at a non existing id returns null`() {
        cleanup {
            val gs = storage.load(1.toString())
            assert(gs == null)
        }
    }

    @Test
    fun `Run new and load works`() {
        cleanup {
            val gs1 = storage.new(1.toString()) { defaultGameState }
            val gs2 = storage.load(1.toString())

            assert(gs1 == gs2)
        }
    }

    @Test
    fun `Run new, save and load works`() {
        cleanup {
            val gs1 = storage.new(1.toString()) {
                defaultGameState
            }.copy(lastPlayer = PieceType.BLACK)

            storage.save(1.toString(), gs1)

            val gs2 = storage.load(1.toString())

            assert(gs1 == gs2)
        }
    }

    @Test
    fun `Run new, delete and load returns null`() {
        cleanup {
            storage.new(1.toString()) { defaultGameState }
            storage.delete(1.toString())
            val gs = storage.load(1.toString())

            assert(gs == null)
        }
    }

    @Test
    fun `Run delete at a non existing id fails`() {
        cleanup {
            assertFails {
                storage.delete(1.toString())
            }
        }
    }
}