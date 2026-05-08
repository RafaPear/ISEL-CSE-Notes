package pt.isel.reversi.app.utils

import pt.isel.reversi.app.app.AppThemes
import pt.isel.reversi.app.gameAudio.loadGameAudioPool
import pt.isel.reversi.core.CoreConfig
import pt.isel.reversi.core.game.gameServices.GameServiceImpl
import pt.isel.reversi.core.loadCoreConfig
import pt.isel.reversi.core.saveCoreConfig
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.utils.LOGGER
import pt.isel.reversi.utils.audio.AudioPool
import pt.isel.reversi.utils.setLoggerFilePath
import pt.rafap.ktflag.cmd.args.CommandArg
import pt.rafap.ktflag.cmd.args.CommandArgsParser

/**
 * Container for application initialization results.
 *
 * @property audioPool The initialized audio pool for sound effects and music.
 */
data class InitializedArgs(
    val audioPool: AudioPool
)

/** Command-line argument enabling file-based logging. */
val noLogArg = CommandArg(
    name = "log",
    aliases = arrayOf("-nl"),
    description = "If set, disables logging to a file named reversi-app.log",
    returnsValue = false,
    isRequired = false
)

/** Command-line argument disabling audio playback. */
val noAudioArg = CommandArg(
    name = "no-audio",
    aliases = arrayOf("-na"),
    description = "If set, disables audio playback",
    returnsValue = false,
    isRequired = false
)

/** Command-line argument displaying help information. */
val helpArg = CommandArg(
    name = "help",
    aliases = arrayOf("-h", "--help"),
    description = "Displays help information about the command-line arguments",
    returnsValue = false,
    isRequired = false
)

/** Array of all supported command-line arguments. */
val allArgs = arrayOf(
    noLogArg, noAudioArg, helpArg
)

/** Parser for command-line arguments. */
val argsParser = CommandArgsParser(*allArgs)

/**
 * Initializes application arguments including logging and audio settings.
 * Parses command-line arguments and configures the application accordingly.
 *
 * @param args Command-line arguments array.
 * @return InitializedArgs with configured audio pool, or null if help was requested.
 */
fun initializeAppArgs(args: Array<String>): InitializedArgs? {
    val parsedArgs = argsParser.parseArgs(*args)

    val help = parsedArgs[helpArg]
    if (help != null) {
        allArgs.forEach {
            println("${it.name} (${it.aliases.joinToString(", ")}): ${it.description}")
        }
        return null
    }

    val logToFileName = parsedArgs[noLogArg]
    if (logToFileName == null) setLoggerFilePath()

    val audioEnabled = parsedArgs[noAudioArg] == null
    val loadedPool = loadGameAudioPool(AppThemes.DARK.appTheme) {}
    val audioPool = if (audioEnabled) loadedPool else loadedPool.also { it.mute(true) }

    return InitializedArgs(audioPool)
}

/**
 * Runs a health check on the configured game storage backend.
 * If remote (MongoDB) storage is configured, verifies connectivity and functionality.
 * On failure, automatically falls back to local file storage if [save] is true.
 *
 * @param service The game service implementation to test.
 * @param testConf Optional configuration to test (defaults to loading from file).
 * @param save Whether to save the fallback configuration if remote storage fails.
 * @return An exception if the health check failed, null if successful.
 */
suspend fun runStorageHealthCheck(
    service: GameServiceImpl,
    testConf: CoreConfig? = null,
    save: Boolean = true
): Exception? {
    val loadedConf = testConf ?: loadCoreConfig()
    if (loadedConf.gameStorageType == GameStorageType.DATABASE_STORAGE) {
        LOGGER.info("Remote storage detected, checking connectivity...")
        try {
            service.runStorageHealthCheck()
            LOGGER.info("Game remote storage connectivity check passed.")
        } catch (e: Exception) {
            LOGGER.severe("Game remote storage connectivity check failed.")
            LOGGER.severe("Failed to initialize game remote storage: ${e.message}")
            LOGGER.severe("Falling back to local storage.")
            if (save) saveCoreConfig(loadedConf.copy(gameStorageType = GameStorageType.FILE_STORAGE))
            return e
        }
    }
    return null
}