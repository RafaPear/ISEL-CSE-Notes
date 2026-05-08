package pt.isel.reversi.core

import pt.isel.reversi.core.storage.GameStorageType
import kotlin.test.Test


///**
// * Configuration holder for core game parameters loaded from properties files.
// * Manages character representations and storage configuration.
// *
// * @property map The underlying configuration map with string keys and values.
// */
//class CoreConfig(override val map: Map<String, String>) : Config {
//    /** Character representing the target pieces on the board. */
//    val targetChar = map["targetChar"]?.firstOrNull() ?: '*'
//
//    /** Character representing empty spaces on the board. */
//    val emptyChar = map["emptyChar"]?.firstOrNull() ?: '.'
//
//    /** Configured storage type for game state persistence. */
//    val gameStorageType = GameStorageType.fromConfigValue(map["gameStorageType"].toString())
//
//    /** Directory path where game saves are stored. */
//    val savesPath = map["savesPath"] ?: makePathString("saves")
//
//    /** Name of the MongoDB database for storing game states. */
//    val dbName = map["dbName"] ?: "gameSaves"
//
//    /** URI for connecting to the MongoDB server. */
//    val dbURI = map["dbURI"] ?: "comunity.ddns.net"
//
//    /** Name of the MongoDB collection for storing game states. */
//    val dbPort = map["dbPort"]?.toIntOrNull() ?: 27017
//
//    /** Username for MongoDB authentication. */
//    val dbUser = map["dbUser"] ?: "<reversiUser>"
//
//    /** Password for MongoDB authentication. */
//    val dbPassword = map["dbPassword"] ?: "<reversiPass>"
//
//    override fun getDefaultConfigFileEntries(): Map<String, String> {
//        return mapOf(
//            "targetChar" to targetChar.toString(),
//            "emptyChar" to emptyChar.toString(),
//            "savesPath" to savesPath,
//            "gameStorageType" to gameStorageType.name,
//            "dbURI" to dbURI,
//            "dbUser" to dbUser,
//            "dbPassword" to dbPassword,
//            "dbPort" to dbPort.toString(),
//            "dbName" to dbName,
//        )
//    }
//
//    /**
//     * Creates a copy of this CoreConfig with optionally modified values.
//     */
//    fun copy(
//        targetChar: Char = this.targetChar,
//        emptyChar: Char = this.emptyChar,
//        gameStorageType: GameStorageType = this.gameStorageType,
//        savesPath: String = this.savesPath,
//        dbName: String = this.dbName,
//        dbURI: String = this.dbURI,
//        dbPort: Int = this.dbPort,
//        dbUser: String = this.dbUser,
//        dbPassword: String = this.dbPassword
//    ): CoreConfig {
//        val newMap = mapOf(
//            "targetChar" to targetChar.toString(),
//            "emptyChar" to emptyChar.toString(),
//            "gameStorageType" to gameStorageType.name,
//            "savesPath" to savesPath,
//            "dbName" to dbName,
//            "dbURI" to dbURI,
//            "dbPort" to dbPort.toString(),
//            "dbUser" to dbUser,
//            "dbPassword" to dbPassword
//        )
//        return CoreConfig(newMap)
//    }
//}


class CoreConfigTests {

    @Test
    fun `CoreConfig defaults when map empty`() {
        val cfg = CoreConfig(emptyMap())

        assert(cfg.targetChar == '*')
        assert(cfg.emptyChar == '.')
        assert(cfg.savesPath.endsWith("saves"))
        assert(cfg.gameStorageType == GameStorageType.FILE_STORAGE)
        assert(cfg.dbName == "gameSaves")
        assert(cfg.dbURI == "comunity.ddns.net")
        assert(cfg.dbPort == 27017)
        assert(cfg.dbUser == "<reversiUser>")
        assert(cfg.dbPassword == "<reversiPass>")
    }

    @Test
    fun `CoreConfig respects provided map values`() {
        val m = mapOf(
            "targetChar" to "#",
            "emptyChar" to "-",
            "savesPath" to "/custom/saves",
            "gameStorageType" to "DATABASE_STORAGE",
            "dbName" to "customDB",
            "dbURI" to "custom.uri.com",
            "dbPort" to "12345",
            "dbUser" to "customUser",
            "dbPassword" to "customPass"
        )
        val cfg = CoreConfig(m)
        assert(cfg.targetChar == '#')
        assert(cfg.emptyChar == '-')
        assert(cfg.savesPath == "/custom/saves")
        assert(cfg.gameStorageType == GameStorageType.DATABASE_STORAGE)
        assert(cfg.dbName == "customDB")
        assert(cfg.dbURI == "custom.uri.com")
        assert(cfg.dbPort == 12345)
        assert(cfg.dbUser == "customUser")
        assert(cfg.dbPassword == "customPass")
    }
}

