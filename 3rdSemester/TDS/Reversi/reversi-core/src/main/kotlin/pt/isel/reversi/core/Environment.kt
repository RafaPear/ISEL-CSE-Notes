package pt.isel.reversi.core

import pt.isel.reversi.core.board.PieceType
import pt.isel.reversi.core.storage.serializers.GameStateSerializer
import pt.isel.reversi.storage.FileStorage

/** Default board side length used by the CLI and tests. */
const val BOARD_SIDE = 8

/** Default piece type for the first player. */
val First_Player_TURN = PieceType.BLACK

/** Minimum allowed board side length. */
const val SIDE_MIN = 4

/** Maximum allowed board side length. */
const val SIDE_MAX = 26
/**
 * Default file-based storage used by the simple demo runner and tests. The folder is relative to the working
 * directory and defaults to `saves`.
 */
val STORAGE = FileStorage(
    folder = "saves",
    serializer = GameStateSerializer()
)
