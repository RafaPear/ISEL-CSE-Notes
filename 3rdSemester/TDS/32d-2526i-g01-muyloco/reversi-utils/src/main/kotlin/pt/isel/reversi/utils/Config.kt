package pt.isel.reversi.utils

/**
 * Contract for configuration objects that provide key-value configuration properties.
 *
 * Implementations should define their own default configuration values and provide
 * a way to load/persist configuration from properties files.
 *
 * @property map A map containing configuration key-value pairs loaded from the properties file.
 */
interface Config {
    /**
     * The configuration properties as key-value pairs.
     * Keys are property names, values are configuration values as strings.
     */
    val map: Map<String, String>

    /**
     * Returns the default configuration entries for this Config implementation.
     *
     * These defaults are used when a configuration file doesn't exist or when
     * loading a file that is missing some keys.
     *
     * @return A map of default configuration key-value pairs.
     */
    fun getDefaultConfigFileEntries(): Map<String, String>
}