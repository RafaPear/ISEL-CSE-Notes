package pt.isel.reversi.utils

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.thread

/**
 * Data class representing a single tracking event.
 *
 * @property timestamp The time when the event occurred.
 * @property type The type of tracking event.
 * @property context The context/name of the tracked element.
 * @property category Optional grouping category (can be any type).
 * @property details Additional details about the event.
 * @property threadId The ID of the thread that generated the event.
 */
data class TrackingEvent(
    val timestamp: LocalDateTime,
    val type: TrackingType,
    val context: String,
    val category: Any? = null,
    val details: String = "",
    val threadId: Long = Thread.currentThread().threadId()
)

/**
 * Enum representing the types of tracking events that can be recorded.
 */
enum class TrackingType {
    PAGE_ENTER,
    VIEW_MODEL_CREATED,
    CLASS_CREATED,
    RECOMPOSITION,
    FUNCTION_CALL,
    EFFECT_START,
    EFFECT_STOP,
    CUSTOM
}

/**
 * Data class representing statistics for a specific tracked context.
 *
 * References:
 * - [Practical uses for AtomicInteger](https://stackoverflow.com/questions/4818699/practical-uses-for-atomicinteger)
 * - [Java Multithreading - Threadsafe Counter](https://stackoverflow.com/questions/29883719/java-multithreading-threadsafe-counter)
 *
 *
 * @property context The name of the tracked element.
 * @property eventCount Total number of events recorded for this context.
 * @property pageEnters Number of page entries.
 * @property viewModelCreations Number of view models created.
 * @property classCreations Number of class instances created.
 * @property recompositions Number of recompositions.
 * @property functionCalls Number of function calls.
 * @property firstOccurrence The timestamp of the first occurrence.
 * @property lastOccurrence The timestamp of the last occurrence.
 * @property effectStarts Number of effect starts.
 * @property effectStops Number of effect stops.
 *
 */
data class TrackingStats(
    val context: String,
    val eventCount: AtomicInteger = AtomicInteger(0),
    val pageEnters: AtomicInteger = AtomicInteger(0),
    val viewModelCreations: AtomicInteger = AtomicInteger(0),
    val classCreations: AtomicInteger = AtomicInteger(0),
    val recompositions: AtomicInteger = AtomicInteger(0),
    val functionCalls: AtomicInteger = AtomicInteger(0),
    val effectStarts: AtomicInteger = AtomicInteger(0),
    val effectStops: AtomicInteger = AtomicInteger(0),
    @Volatile var firstOccurrence: LocalDateTime? = null,
    @Volatile var lastOccurrence: LocalDateTime? = null
) {
    fun incrementType(type: TrackingType) {
        eventCount.incrementAndGet()
        when (type) {
            TrackingType.PAGE_ENTER -> pageEnters.incrementAndGet()
            TrackingType.VIEW_MODEL_CREATED -> viewModelCreations.incrementAndGet()
            TrackingType.CLASS_CREATED -> classCreations.incrementAndGet()
            TrackingType.RECOMPOSITION -> recompositions.incrementAndGet()
            TrackingType.FUNCTION_CALL -> functionCalls.incrementAndGet()
            TrackingType.EFFECT_START -> effectStarts.incrementAndGet()
            TrackingType.EFFECT_STOP -> effectStops.incrementAndGet()
            TrackingType.CUSTOM -> {} // No specific counter
        }
    }
}

/**
 * Configuration for the DevTracker.
 *
 * @property enabled Whether tracking is enabled.
 * @property maxEventsPerContext Maximum events to store per context (0 = unlimited).
 * @property autoSave Whether to auto-save after each event.
 * @property stackTraceDepth Depth to search in stack trace for caller detection.
 */
data class TrackerConfig(
    val enabled: Boolean = true,
    val maxEventsPerContext: Int = 1000,
    val autoSave: Boolean = false,
    val stackTraceDepth: Int = 4
)

/**
 * Development tracker for monitoring application behavior.
 *
 * Provides comprehensive tracking of page entries, view model creations,
 * recompositions, function calls, and custom events with thread-safe operations.
 *
 * References:
 * - [Java’s ConcurrentHashMap.compute() Method Explained](https://medium.com/@AlexanderObregon/javas-concurrenthashmap-compute-method-explained-ddec7cfd44f4)
 * - [ConcurrentHashMap in Java](https://www.geeksforgeeks.org/java/concurrenthashmap-in-java/)
 * - [How can I get the current stack trace in Java?](https://stackoverflow.com/questions/1069066/how-can-i-get-the-current-stack-trace-in-java)
 *
 * @property config Configuration for the tracker.
 */
@Suppress("Unused")
class DevTracker(
    private var config: TrackerConfig = TrackerConfig()
) {
    private val events = ConcurrentHashMap<String, MutableList<TrackingEvent>>()
    private val stats = ConcurrentHashMap<String, TrackingStats>()
    private val categorizedStats = ConcurrentHashMap<Any, MutableList<TrackingStats>>()
    private val dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
    private val fullDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    @Volatile
    private var trackingFilePath: String? = null

    @Volatile
    private var autoSaveEnabled: Boolean = config.autoSave

    private val totalEventsTracked = AtomicLong(0)

    /**
     * Updates the tracker configuration.
     */
    fun updateConfig(newConfig: TrackerConfig) {
        config = newConfig
        autoSaveEnabled = newConfig.autoSave
    }

    /**
     * Enables or disables tracking globally.
     */
    fun setEnabled(value: Boolean) {
        config = config.copy(enabled = value)
        LOGGER.info("Tracking ${if (value) "enabled" else "disabled"}")
    }

    /**
     * Gets the current enabled state.
     */
    fun isEnabled(): Boolean = config.enabled

    /**
     * Automatically detects the calling function name from the call stack.
     *
     * References:
     * - [How can I get the current stack trace in Java?](https://stackoverflow.com/questions/1069066/how-can-i-get-the-current-stack-trace-in-java)
     *
     * @return The name of the calling function or "Unknown" if detection fails.
     */
    private fun detectCallingFunction(): String {
        return try {
            val stackTrace = Thread.currentThread().stackTrace
            if (stackTrace.size > config.stackTraceDepth) {
                val element = stackTrace[config.stackTraceDepth]
                buildOrigin(element.className, element.methodName)
            } else {
                "Unknown"
            }
        } catch (e: Exception) {
            LOGGER.info("Error detecting calling function: ${e.message}")
            "Unknown"
        }
    }

    /**
     * Records a tracking event with improved performance and memory management.
     */
    private fun recordEvent(event: TrackingEvent) {
        if (!config.enabled) return

        try {
            totalEventsTracked.incrementAndGet()

            val contextKey = event.context
            val isFirstEvent = !events.containsKey(contextKey)

            // Update or create event list with size limit
            events.compute(contextKey) { _, existing ->
                val list = existing ?: mutableListOf()
                list.add(event)

                // Enforce max events limit if configured
                if (config.maxEventsPerContext > 0 && list.size > config.maxEventsPerContext) {
                    list.removeAt(0) // Remove oldest event
                }
                list
            }

            // Update statistics
            val contextStats = stats.computeIfAbsent(contextKey) { TrackingStats(contextKey) }
            contextStats.incrementType(event.type)

            // Update category grouping if category is provided
            event.category?.let { category ->
                categorizedStats.compute(category) { _, existing ->
                    val list = existing ?: mutableListOf()
                    if (!list.contains(contextStats)) {
                        list.add(contextStats)
                    }
                    list
                }
            }

            // Update timestamps with thread-safe compare-and-set pattern
            if (contextStats.firstOccurrence == null) {
                synchronized(contextStats) {
                    if (contextStats.firstOccurrence == null) {
                        contextStats.firstOccurrence = event.timestamp
                    }
                }
            }
            contextStats.lastOccurrence = event.timestamp

            // Log only on first occurrence of this context
            if (isFirstEvent) {
                logTrackingEvent(event)
            }

            if (autoSaveEnabled) {
                asyncSave()
            }
        } catch (e: Exception) {
            LOGGER.info("Error recording event: ${e.message}")
        }
    }

    /**
     * Logs a tracking event in a structured format.
     */
    private fun logTrackingEvent(event: TrackingEvent) {
        val message = buildString {
            append("[${event.timestamp.format(dateFormatter)}] ")
            append("[Thread-${event.threadId}] ")
            event.category?.let { append("[$it] ") }
            append("Started tracking ${event.type.name}:${event.context}")
            if (event.details.isNotEmpty()) {
                append(" - ${event.details}")
            }
        }
        LOGGER.info(message)
    }

    /**
     * Tracks a page entry with automatic caller detection.
     * @param customName Optional custom name for the page.
     * @param category Optional category for grouping (can be any type).
     */
    fun trackPageEnter(customName: String? = null, category: Any? = null) {
        recordEvent(
            TrackingEvent(
                timestamp = LocalDateTime.now(),
                type = TrackingType.PAGE_ENTER,
                context = customName ?: detectCallingFunction(),
                category = category
            )
        )
    }

    /**
     * Tracks a view model creation.
     * @param viewModel The view model instance being tracked.
     * @param className Optional custom class name.
     * @param category Optional category for grouping (can be any type).
     */
    fun <T : Any> trackViewModelCreated(viewModel: T? = null, className: String? = null, category: Any? = null) {
        val context = className ?: viewModel?.javaClass?.simpleName ?: "Unknown"
        recordEvent(
            TrackingEvent(
                timestamp = LocalDateTime.now(),
                type = TrackingType.VIEW_MODEL_CREATED,
                context = context,
                category = category
            )
        )
    }

    /**
     * Tracks a class creation.
     * @param classInstance The class instance being tracked.
     * @param className Optional custom class name.
     * @param category Optional category for grouping (can be any type).
     */
    fun <T : Any> trackClassCreated(classInstance: T? = null, className: String? = null, category: Any? = null) {
        val context = className ?: classInstance?.javaClass?.simpleName ?: "Unknown"
        recordEvent(
            TrackingEvent(
                timestamp = LocalDateTime.now(),
                type = TrackingType.CLASS_CREATED,
                context = context,
                category = category
            )
        )
    }

    fun <T : Any> trackEffectStart(effectInstance: T? = null, effectName: String? = null, category: Any? = null) {
        val context = effectName ?: effectInstance?.javaClass?.simpleName ?: "Unknown"
        recordEvent(
            TrackingEvent(
                timestamp = LocalDateTime.now(),
                type = TrackingType.EFFECT_START,
                context = context,
                category = category
            )
        )
    }

    fun <T : Any> trackEffectStop(effectInstance: T? = null, effectName: String? = null, category: Any? = null) {
        val context = effectName ?: effectInstance?.javaClass?.simpleName ?: "Unknown"
        recordEvent(
            TrackingEvent(
                timestamp = LocalDateTime.now(),
                type = TrackingType.EFFECT_STOP,
                context = context,
                category = category
            )
        )
    }

    /**
     * Tracks a recomposition with automatic caller detection.
     * @param customName Optional custom name for the recomposition.
     * @param category Optional category for grouping (can be any type).
     */
    fun trackRecomposition(customName: String? = null, category: Any? = null) {
        recordEvent(
            TrackingEvent(
                timestamp = LocalDateTime.now(),
                type = TrackingType.RECOMPOSITION,
                context = customName ?: detectCallingFunction(),
                category = category
            )
        )
    }

    /**
     * Tracks a function call with optional details.
     * @param customName Optional custom name for the function.
     * @param details Additional details about the function call.
     * @param category Optional category for grouping (can be any type).
     */
    fun trackFunctionCall(customName: String? = null, details: String = "", category: Any? = null) {
        recordEvent(
            TrackingEvent(
                timestamp = LocalDateTime.now(),
                type = TrackingType.FUNCTION_CALL,
                context = customName ?: detectCallingFunction(),
                details = details,
                category = category
            )
        )
    }

    /**
     * Tracks a custom event.
     * @param context The context/name of the event.
     * @param details Additional details about the event.
     * @param category Optional category for grouping (can be any type).
     */
    fun trackCustom(context: String, details: String = "", category: Any? = null) {
        recordEvent(
            TrackingEvent(
                timestamp = LocalDateTime.now(),
                type = TrackingType.CUSTOM,
                context = context,
                details = details,
                category = category
            )
        )
    }

    /**
     * Gets statistics for a specific context.
     */
    fun getStats(context: String): TrackingStats? = stats[context]

    /**
     * Gets all recorded statistics as an immutable map.
     */
    fun getAllStats(): Map<String, TrackingStats> = stats.toMap()

    /**
     * Gets statistics grouped by category.
     * @return A map of category to list of tracking statistics.
     */
    fun getCategorizedStats(): Map<Any, List<TrackingStats>> = categorizedStats.mapValues { it.value.toList() }

    /**
     * Gets all recorded events for a specific context.
     */
    fun getEvents(context: String): List<TrackingEvent> =
        events[context]?.toList() ?: emptyList()

    /**
     * Gets all recorded events as an immutable map.
     */
    fun getAllEvents(): Map<String, List<TrackingEvent>> =
        events.mapValues { it.value.toList() }

    /**
     * Gets the total number of events tracked across all contexts.
     */
    fun getTotalEventsTracked(): Long = totalEventsTracked.get()

    /**
     * Prints a comprehensive summary of all tracked data.
     */
    fun printSummary() {
        if (stats.isEmpty()) {
            LOGGER.info("[TRACK] No tracking data recorded yet.")
            return
        }

        LOGGER.info("========== TRACKING SUMMARY ==========")
        LOGGER.info("Total contexts tracked: ${stats.size}")
        LOGGER.info("Total events tracked: ${totalEventsTracked.get()}")
        LOGGER.info("")

        stats.values.sortedByDescending { it.eventCount.get() }.forEach { stat ->
            LOGGER.info("Context: ${stat.context}")
            LOGGER.info("  Total Events: ${stat.eventCount.get()}")
            LOGGER.info("  Page Enters: ${stat.pageEnters.get()}")
            LOGGER.info("  ViewModel Creations: ${stat.viewModelCreations.get()}")
            LOGGER.info("  Class Creations: ${stat.classCreations.get()}")
            LOGGER.info("  Recompositions: ${stat.recompositions.get()}")
            LOGGER.info("  Function Calls: ${stat.functionCalls.get()}")
            LOGGER.info("  Effect Starts: ${stat.effectStarts.get()}")
            LOGGER.info("  Effect Stops: ${stat.effectStops.get()}")
            stat.firstOccurrence?.let {
                LOGGER.info("  First: ${it.format(dateFormatter)}")
            }
            stat.lastOccurrence?.let {
                LOGGER.info("  Last: ${it.format(dateFormatter)}")
            }
            LOGGER.info("")
        }
        LOGGER.info("======================================")
    }

    /**
     * Clears all tracked data and resets counters.
     */
    fun clear() {
        events.clear()
        stats.clear()
        totalEventsTracked.set(0)
        LOGGER.info("[TRACK] Tracking data cleared.")
    }

    /**
     * Exports statistics in various formats.
     */
    fun exportStatistics(format: ExportFormat = ExportFormat.TEXT): String {
        return when (format) {
            ExportFormat.TEXT -> exportAsText()
            ExportFormat.CSV -> exportAsCSV()
            ExportFormat.JSON -> exportAsJSON()
        }
    }

    private fun exportAsText(): String = buildString {
        append("========== TRACKING STATISTICS ==========\n")
        append("Generated: ${LocalDateTime.now().format(fullDateFormatter)}\n")
        append("Total contexts tracked: ${stats.size}\n")
        append("Total events tracked: ${totalEventsTracked.get()}\n")
        append("Total categories: ${categorizedStats.size}\n")
        append("\n")

        // Export by category if categories exist
        if (categorizedStats.isNotEmpty()) {
            append("========== GROUPED BY CATEGORY ==========\n\n")
            categorizedStats.entries.sortedBy { it.key.toString() }.forEach { (category, statsList) ->
                val categoryStr = category.toString()
                append("Category: $categoryStr\n")
                append("${"=".repeat(40 + categoryStr.length)}\n")

                statsList.sortedByDescending { it.eventCount.get() }.forEach { stat ->
                    append("  Context: ${stat.context}\n")
                    append("    Total Events: ${stat.eventCount.get()}\n")
                    append("    Page Enters: ${stat.pageEnters.get()}\n")
                    append("    ViewModel Creations: ${stat.viewModelCreations.get()}\n")
                    append("    Class Creations: ${stat.classCreations.get()}\n")
                    append("    Recompositions: ${stat.recompositions.get()}\n")
                    append("    Function Calls: ${stat.functionCalls.get()}\n")
                    append("    Effect Starts: ${stat.effectStarts.get()}\n")
                    append("    Effect Stops: ${stat.effectStops.get()}\n")
                    stat.firstOccurrence?.let {
                        append("    First: ${it.format(fullDateFormatter)}\n")
                    }
                    stat.lastOccurrence?.let {
                        append("    Last: ${it.format(fullDateFormatter)}\n")
                    }
                    append("\n")
                }
                append("\n")
            }
        }

        // Export all contexts (including uncategorized)
        append("========== ALL CONTEXTS ==========\n\n")
        stats.values.sortedByDescending { it.eventCount.get() }.forEach { stat ->
            append("Context: ${stat.context}\n")
            append("  Total Events: ${stat.eventCount.get()}\n")
            append("  Page Enters: ${stat.pageEnters.get()}\n")
            append("  ViewModel Creations: ${stat.viewModelCreations.get()}\n")
            append("  Class Creations: ${stat.classCreations.get()}\n")
            append("  Recompositions: ${stat.recompositions.get()}\n")
            append("  Function Calls: ${stat.functionCalls.get()}\n")
            append("  Effect Starts: ${stat.effectStarts.get()}\n")
            append("  Effect Stops: ${stat.effectStops.get()}\n")
            stat.firstOccurrence?.let {
                append("  First: ${it.format(fullDateFormatter)}\n")
            }
            stat.lastOccurrence?.let {
                append("  Last: ${it.format(fullDateFormatter)}\n")
            }
            append("\n")
        }
        append("=========================================\n")
    }

    private fun exportAsCSV(): String = buildString {
        append("Category,Context,Total Events,Page Enters,ViewModel Creations,Class Creations,Recompositions,Function Calls,Effect Start,Effect Stop,First Occurrence,Last Occurrence\n")

        // Create a map from context to category for easy lookup
        val contextToCategory = mutableMapOf<String, String>()
        categorizedStats.forEach { (category, statsList) ->
            statsList.forEach { stat ->
                contextToCategory[stat.context] = category.toString()
            }
        }

        stats.values.sortedByDescending { it.eventCount.get() }.forEach { stat ->
            val category = contextToCategory[stat.context] ?: ""
            append("\"$category\",")
            append("\"${stat.context}\",")
            append("${stat.eventCount.get()},")
            append("${stat.pageEnters.get()},")
            append("${stat.viewModelCreations.get()},")
            append("${stat.classCreations.get()},")
            append("${stat.recompositions.get()},")
            append("${stat.functionCalls.get()},")
            append("${stat.effectStarts.get()},")
            append("${stat.effectStops.get()},")
            append("\"${stat.firstOccurrence?.format(fullDateFormatter) ?: ""}\",")
            append("\"${stat.lastOccurrence?.format(fullDateFormatter) ?: ""}\"\n")
        }
    }

    private fun exportAsJSON(): String = buildString {
        append("{\n")
        append("  \"totalContexts\": ${stats.size},\n")
        append("  \"totalEvents\": ${totalEventsTracked.get()},\n")
        append("  \"totalCategories\": ${categorizedStats.size},\n")

        // Export categorized statistics
        if (categorizedStats.isNotEmpty()) {
            append("  \"categorizedStatistics\": {\n")
            categorizedStats.entries.sortedBy { it.key.toString() }.forEachIndexed { catIndex, (category, statsList) ->
                val categoryStr = category.toString().replace("\"", "\\\"")
                append("    \"$categoryStr\": [\n")
                statsList.sortedByDescending { it.eventCount.get() }.forEachIndexed { index, stat ->
                    append("      {\n")
                    append("        \"context\": \"${stat.context}\",\n")
                    append("        \"totalEvents\": ${stat.eventCount.get()},\n")
                    append("        \"pageEnters\": ${stat.pageEnters.get()},\n")
                    append("        \"viewModelCreations\": ${stat.viewModelCreations.get()},\n")
                    append("        \"classCreations\": ${stat.classCreations.get()},\n")
                    append("        \"recompositions\": ${stat.recompositions.get()},\n")
                    append("        \"functionCalls\": ${stat.functionCalls.get()},\n")
                    append("        \"effectStarts\": ${stat.effectStarts.get()},\n")
                    append("        \"effectStops\": ${stat.effectStops.get()},\n")
                    append("        \"firstOccurrence\": \"${stat.firstOccurrence?.format(fullDateFormatter) ?: ""}\",\n")
                    append("        \"lastOccurrence\": \"${stat.lastOccurrence?.format(fullDateFormatter) ?: ""}\"\n")
                    append("      }${if (index < statsList.size - 1) "," else ""}\n")
                }
                append("    ]${if (catIndex < categorizedStats.size - 1) "," else ""}\n")
            }
            append("  },\n")
        }

        // Export all statistics
        append("  \"statistics\": [\n")

        stats.values.sortedByDescending { it.eventCount.get() }.forEachIndexed { index, stat ->
            append("    {\n")
            append("      \"context\": \"${stat.context}\",\n")
            append("      \"totalEvents\": ${stat.eventCount.get()},\n")
            append("      \"pageEnters\": ${stat.pageEnters.get()},\n")
            append("      \"viewModelCreations\": ${stat.viewModelCreations.get()},\n")
            append("      \"classCreations\": ${stat.classCreations.get()},\n")
            append("      \"recompositions\": ${stat.recompositions.get()},\n")
            append("      \"functionCalls\": ${stat.functionCalls.get()},\n")
            append("      \"effectStarts\": ${stat.effectStarts.get()},\n")
            append("      \"effectStops\": ${stat.effectStops.get()},\n")
            append("      \"firstOccurrence\": \"${stat.firstOccurrence?.format(fullDateFormatter) ?: ""}\",\n")
            append("      \"lastOccurrence\": \"${stat.lastOccurrence?.format(fullDateFormatter) ?: ""}\"\n")
            append("    }${if (index < stats.size - 1) "," else ""}\n")
        }

        append("  ]\n")
        append("}\n")
    }

    /**
     * Returns a path with the given extension, replacing any existing one.
     */
    private fun withExt(path: String, ext: String): String {
        val idx = path.lastIndexOf('.')
        val base = if (idx <= 0) path else path.take(idx)
        return base + ext
    }

    /**
     * Sets up file tracking with optional auto-save.
     * Keeps signature intact. Generates a default .log file initially.
     */
    fun setTrackerFilePath(autoSave: Boolean = false): String {
        val runFolder = RUN_LOG_FOLDER
        val baseName = "$runFolder/reversi-tracking"
        val filePath = generateUniqueTimestampedFileName(baseName, ".log")
        File(filePath).parentFile?.mkdirs()
        File(filePath).createNewFile()
        trackingFilePath = filePath
        autoSaveEnabled = autoSave
        LOGGER.info("Tracking file: '$filePath' | Auto-save: $autoSave | Run folder: '$runFolder'")
        return filePath
    }

    /**
     * Saves tracking data to file synchronously.
     * Keeps signature intact. Ensures single, correct extension.
     */
    fun saveToFile(format: ExportFormat = ExportFormat.TEXT): Boolean {
        val filePath = trackingFilePath ?: run {
            LOGGER.info("No tracking file path configured")
            return false
        }
        return try {
            val content = exportStatistics(format)
            val target = withExt(filePath, format.ext)
            File(target).parentFile?.mkdirs()
            File(target).writeText(content)
            LOGGER.info("Tracking data saved to '$target'")
            true
        } catch (e: Exception) {
            LOGGER.info("Error saving tracking data: ${e.message}")
            false
        }
    }

    /**
     * Saves tracking data asynchronously in a separate thread.
     */
    private fun asyncSave() {
        trackingFilePath?.let { path ->
            thread(start = true, isDaemon = true, name = "TrackerAutoSave") {
                try {
                    val content = exportStatistics()
                    File(path).writeText(content)
                } catch (e: Exception) {
                    LOGGER.info("Async save error: ${e.message}")
                }
            }
        }
    }

    /**
     * Appends a single event to the tracking file (useful for large datasets).
     */
    fun appendEventToFile(event: TrackingEvent): Boolean {
        val filePath = trackingFilePath ?: return false

        return try {
            File(filePath).appendText(
                "${event.timestamp.format(fullDateFormatter)} | " +
                        "${event.type} | ${event.context} | ${event.details}\n"
            )
            true
        } catch (e: Exception) {
            LOGGER.info("Error appending event: ${e.message}")
            false
        }
    }

    fun getTrackerFilePath(): String? = trackingFilePath

    fun isAutoSaveEnabled(): Boolean = autoSaveEnabled

    fun setAutoSaveEnabled(enabled: Boolean) {
        autoSaveEnabled = enabled
        LOGGER.info("Auto-save ${if (enabled) "enabled" else "disabled"}")
    }
}

/**
 * Export format options for tracking data.
 */
enum class ExportFormat(val ext: String) {
    TEXT(".log"),
    CSV(".csv"),
    JSON(".json")
}