package pt.isel.reversi.core

import pt.isel.reversi.utils.CORE_CONFIG_FILE
import pt.isel.reversi.utils.ConfigLoader

/** Minimum allowed board side length. */
const val SIDE_MIN = 4

/** Maximum allowed board side length. */
const val SIDE_MAX = 26

/**
 * Loads the core module configuration from the configuration file.
 * Creates the file with default values if it doesn't exist.
 * @return A CoreConfig instance with the loaded or default settings.
 */
fun loadCoreConfig(): CoreConfig = ConfigLoader(CORE_CONFIG_FILE) {
    CoreConfig(it)
}.loadConfig()

/**
 * Saves the core module configuration to the configuration file.
 * @param config The CoreConfig instance to save.
 */
fun saveCoreConfig(config: CoreConfig) = ConfigLoader(CORE_CONFIG_FILE) {
    CoreConfig(it)
}.saveConfig(config)

