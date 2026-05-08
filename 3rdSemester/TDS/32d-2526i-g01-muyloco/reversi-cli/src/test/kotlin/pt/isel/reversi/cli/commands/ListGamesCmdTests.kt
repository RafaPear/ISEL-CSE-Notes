package pt.isel.reversi.cli.commands

import pt.isel.reversi.core.game.Game
import pt.isel.reversi.core.game.gameServices.GameService
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.core.storage.StorageParams
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class ListGamesCmdTests {
    val gameService = GameService(
        storage = GameStorageType.FILE_STORAGE,
        params = StorageParams.FileStorageParams(folder = "data/saves")
    )

    @BeforeTest
    @AfterTest
    fun cleanup() {
        File(BASE_FOLDER).deleteRecursively()
    }


    @Test
    fun `ListGamesCmd reports no saved games when folder missing`() {
        val res = ListGamesCmd.execute(context = Game(service = gameService))
        assertTrue(res.message.contains("No saved games"))
    }

    @Test
    fun `ListGamesCmd lists files in saves folder`() {
        val savesFolder = "data/saves"
        File(savesFolder).mkdirs()
        File(savesFolder, "game1.json").writeText("x")
        File(savesFolder, "game2.json").writeText("y")

        val res = ListGamesCmd.execute(context = Game(service = gameService))
        assertTrue(
            res.message.contains("Available games") && res.message.contains("- game1") && res.message.contains(
                "- game2"
            )
        )
    }
}
