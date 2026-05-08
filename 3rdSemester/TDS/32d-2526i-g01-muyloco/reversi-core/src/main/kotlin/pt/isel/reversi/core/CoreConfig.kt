package pt.isel.reversi.core

import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.utils.Config
import pt.isel.reversi.utils.makePathString

/**
 * Configuration holder for core game parameters loaded from properties files.
 * Manages character representations and storage configuration.
 *
 * @property map The underlying configuration map with string keys and values.
 */
class CoreConfig(override val map: Map<String, String>) : Config {
    /** Character representing the target pieces on the board. */
    val targetChar = map["targetChar"]?.firstOrNull() ?: '*'

    /** Character representing empty spaces on the board. */
    val emptyChar = map["emptyChar"]?.firstOrNull() ?: '.'

    /** Configured storage type for game state persistence. */
    val gameStorageType = GameStorageType.fromConfigValue(map["gameStorageType"].toString())

    /** Directory path where game saves are stored. */
    val savesPath = map["savesPath"] ?: makePathString("saves")

    /** Name of the MongoDB database for storing game states. */
    val dbName = map["dbName"] ?: "gameSaves"

    /** URI for connecting to the MongoDB server. */
    val dbURI = map["dbURI"] ?: "comunity.ddns.net"

    /** Name of the MongoDB collection for storing game states. */
    val dbPort = map["dbPort"]?.toIntOrNull() ?: 27017

    /** Username for MongoDB authentication. */
    val dbUser = map["dbUser"] ?: "<reversiUser>"

    /** Password for MongoDB authentication. */
    val dbPassword = map["dbPassword"] ?: "<reversiPass>"

    /**
     * Returns the default configuration entries for core game settings.
     * @return A map of default configuration key-value pairs.
     */
    override fun getDefaultConfigFileEntries(): Map<String, String> {
        return mapOf(
            "targetChar" to targetChar.toString(),
            "emptyChar" to emptyChar.toString(),
            "savesPath" to savesPath,
            "gameStorageType" to gameStorageType.name,
            "dbURI" to dbURI,
            "dbUser" to dbUser,
            "dbPassword" to dbPassword,
            "dbPort" to dbPort.toString(),
            "dbName" to dbName,
        )
    }

    /**
     * Creates a copy of this CoreConfig with optionally modified values.
     * @param targetChar The character representing targets on the board.
     * @param emptyChar The character representing empty spaces on the board.
     * @param gameStorageType The storage type for game persistence.
     * @param savesPath The directory path for game saves.
     * @param dbName The MongoDB database name.
     * @param dbURI The MongoDB connection URI.
     * @param dbPort The MongoDB connection port.
     * @param dbUser The MongoDB username for authentication.
     * @param dbPassword The MongoDB password for authentication.
     * @return A new CoreConfig with the specified values.
     */
    fun copy(
        targetChar: Char = this.targetChar,
        emptyChar: Char = this.emptyChar,
        gameStorageType: GameStorageType = this.gameStorageType,
        savesPath: String = this.savesPath,
        dbName: String = this.dbName,
        dbURI: String = this.dbURI,
        dbPort: Int = this.dbPort,
        dbUser: String = this.dbUser,
        dbPassword: String = this.dbPassword
    ): CoreConfig {
        val newMap = mapOf(
            "targetChar" to targetChar.toString(),
            "emptyChar" to emptyChar.toString(),
            "gameStorageType" to gameStorageType.name,
            "savesPath" to savesPath,
            "dbName" to dbName,
            "dbURI" to dbURI,
            "dbPort" to dbPort.toString(),
            "dbUser" to dbUser,
            "dbPassword" to dbPassword
        )
        return CoreConfig(newMap)
    }
}





