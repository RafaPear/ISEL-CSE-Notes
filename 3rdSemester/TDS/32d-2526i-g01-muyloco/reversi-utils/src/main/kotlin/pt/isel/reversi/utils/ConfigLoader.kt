package pt.isel.reversi.utils

import java.io.File
import java.util.*

/**
 * Loads and manages configuration from Java properties files.
 *
 * This loader automatically:
 * - Creates configuration files if they don't exist
 * - Adds missing keys with default values
 * - Reads and parses properties files
 * - Provides type-safe access through Config implementations
 *
 * @param U The Config subclass type to instantiate with loaded values.
 * @property path File path to the configuration properties file.
 * @property factory Function to construct the Config instance from a key-value map.
 */
class ConfigLoader<U : Config>(
    val path: String,
    val factory: (Map<String, String>) -> U
) {

    /**
     * Loads configuration from the properties file at the specified path.
     *
     * If the file doesn't exist:
     * - The parent directory is created if necessary
     * - A new properties file is created
     * - Default values from the factory are written to the file
     *
     * If the file exists but is missing keys:
     * - Missing keys are added with their default values
     * - The file is updated
     *
     * @return A new Config instance with the loaded properties.
     */
    fun loadConfig(): U {
        val file = File(path)
        val props = Properties()

        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        } else {
            file.inputStream().use(props::load)
        }

        val defaults = factory(emptyMap()).getDefaultConfigFileEntries()

        var changed = false
        for ((key, value) in defaults) {
            if (!props.containsKey(key)) {
                props.setProperty(key, value)
                changed = true
            }
        }

        if (changed) {
            file.outputStream().use {
                props.store(it, "Configuration file at ${file.absolutePath}")
            }
        }

        val configMap = props.entries.associate {
            it.key.toString() to it.value.toString().trim()
        }

        return factory(configMap)
    }

    /**
     * Saves a configuration instance to the properties file.
     *
     * This method writes all configuration values from the provided Config instance
     * to the properties file, creating the file and parent directories if necessary.
     *
     * @param config The configuration instance to save.
     */
    fun saveConfig(config: U) {
        val file = File(path)
        val props = Properties()

        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        }

        val configMap = config.getDefaultConfigFileEntries()
        configMap.forEach { (key, value) ->
            props.setProperty(key, value)
        }

        file.outputStream().use {
            props.store(it, "Configuration file updated at ${file.absolutePath}")
        }
    }
}
