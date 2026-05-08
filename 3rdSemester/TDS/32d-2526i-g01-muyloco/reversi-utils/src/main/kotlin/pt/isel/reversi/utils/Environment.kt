package pt.isel.reversi.utils

import java.io.File
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Environment constants and configuration for the Reversi application.
 *
 * This module defines paths to configuration files and provides a global logger instance
 * and a development tracker instance configured for consistent logging and tracking
 * across all modules.
 */

/** Base data directory for configuration and logs. */
const val BASE_FOLDER = "data"

/** Per-run logs folder derived from current date (e.g., data/logs/log-YYYY-MM-DD[-N]). */
val RUN_LOG_FOLDER: String = run {
    val baseRunFolder = makePathString("logs", "log")
    val runFolder = generateUniqueTimestampedFileName(baseRunFolder, "")
    File(runFolder).mkdirs()
    runFolder
}

/** Configuration directory path containing all application configuration files. */
const val CONFIG_FOLDER = "$BASE_FOLDER/config"

/** Core module configuration file path (reversi-core.properties). */
const val CORE_CONFIG_FILE = "$CONFIG_FOLDER/reversi-core.properties"

/** CLI module configuration file path (reversi-cli.properties). */
const val CLI_CONFIG_FILE = "$CONFIG_FOLDER/reversi-cli.properties"

/**
 * Global logger instance configured with console handler and plain formatter.
 *
 * This logger is configured to output to standard output with a plain text format.
 * It is used throughout the application for logging game events, errors, and diagnostics.
 *
 * Configuration:
 * - Level: ALL (captures all log levels)
 * - Handler: StdOutConsoleHandler (writes to standard output)
 * - Formatter: PlainFormatter (outputs messages in plain text format)
 */
val LOGGER: Logger = Logger.getGlobal().apply {
    useParentHandlers = false
    level = Level.ALL
    val handler = StdOutConsoleHandler().apply {
        level = Level.ALL
        formatter = PlainFormatter()
    }
    addHandler(handler)
}

/**
 * Global development tracker instance for monitoring application behavior.
 *
 * This tracker records page entries, view model creations, recompositions, and custom events.
 * It provides automatic function detection and event tracking capabilities without affecting
 * production code.
 *
 * Features:
 * - Automatic function name detection from call stack
 * - Thread-safe event recording with atomic counters
 * - Optional file-based persistence of tracking data
 * - Auto-save capability for continuous data collection
 * - Integration with the main LOGGER for event logging
 *
 * Usage:
 * ```
 * // Track a page entry (automatically detects function name)
 * trackPageEnter()
 *
 * // Track view model creation
 * trackViewModelCreated(viewModel)
 *
 * // Track recomposition
 * trackRecomposition()
 *
 * // Print summary of all tracked data
 * printTrackingSummary()
 *
 * // Save to file with auto-save enabled
 * TRACKER.setTrackerFilePath(autoSave = true)
 * TRACKER.saveToFile()
 * ```
 *
 * @see DevTracker
 */
val TRACKER = DevTracker()
