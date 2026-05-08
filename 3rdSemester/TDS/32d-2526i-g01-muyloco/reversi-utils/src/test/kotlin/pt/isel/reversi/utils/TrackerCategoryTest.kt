package pt.isel.reversi.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for the new category-based grouping feature in DevTracker.
 */
class TrackerCategoryTest {

    @Test
    fun `test category grouping with single category`() {
        val tracker = DevTracker(TrackerConfig(enabled = true))

        // Track events with the same category
        tracker.trackPageEnter(customName = "Page1", category = "Page.Game")
        tracker.trackViewModelCreated<Any>(viewModel = null, className = "ViewModel1", category = "Page.Game")
        tracker.trackEffectStart<Any>(effectInstance = null, effectName = "Effect1", category = "Page.Game")
        tracker.trackEffectStop<Any>(effectInstance = null, effectName = "Effect1", category = "Page.Game")

        // Verify categorized stats
        val categorizedStats = tracker.getCategorizedStats()
        assertEquals(1, categorizedStats.size, "Should have 1 category")
        assertTrue(categorizedStats.containsKey("Page.Game"), "Should contain Page.Game category")

        val gamePageStats = categorizedStats["Page.Game"]!!
        // Note: Page1, ViewModel1, and Effect1 (start/stop share same context) = 3 unique contexts
        assertEquals(3, gamePageStats.size, "Page.Game category should have 3 unique contexts")
    }

    @Test
    fun `test category grouping with multiple categories`() {
        val tracker = DevTracker(TrackerConfig(enabled = true))

        // Track events with different categories
        tracker.trackPageEnter(customName = "Page1", category = "Page.Game")
        tracker.trackViewModelCreated<Any>(viewModel = null, className = "ViewModel1", category = "Page.Game")
        tracker.trackClassCreated<Any>(classInstance = null, className = "Game", category = "Core.Game")
        tracker.trackFunctionCall(customName = "play", category = "Core.Logic")
        tracker.trackClassCreated<Any>(classInstance = null, className = "FileStorage", category = "Storage.File")
        tracker.trackFunctionCall(customName = "save", category = "Storage.File")

        // Verify categorized stats
        val categorizedStats = tracker.getCategorizedStats()
        assertEquals(4, categorizedStats.size, "Should have 4 categories")
        assertTrue(categorizedStats.containsKey("Page.Game"))
        assertTrue(categorizedStats.containsKey("Core.Game"))
        assertTrue(categorizedStats.containsKey("Core.Logic"))
        assertTrue(categorizedStats.containsKey("Storage.File"))

        // Verify category sizes
        assertEquals(2, categorizedStats["Page.Game"]!!.size)
        assertEquals(1, categorizedStats["Core.Game"]!!.size)
        assertEquals(1, categorizedStats["Core.Logic"]!!.size)
        assertEquals(2, categorizedStats["Storage.File"]!!.size)
    }

    @Test
    fun `test uncategorized events are still tracked`() {
        val tracker = DevTracker(TrackerConfig(enabled = true))

        // Track events without categories
        tracker.trackPageEnter(customName = "Page1")
        tracker.trackViewModelCreated<Any>(viewModel = null, className = "ViewModel1")

        // Track events with categories
        tracker.trackClassCreated<Any>(classInstance = null, className = "Game", category = "Core.Game")

        // Verify all events are in getAllStats
        val allStats = tracker.getAllStats()
        assertEquals(3, allStats.size, "Should track all events regardless of category")

        // Verify only categorized events are in getCategorizedStats
        val categorizedStats = tracker.getCategorizedStats()
        assertEquals(1, categorizedStats.size, "Should only have categorized events")
        assertEquals(1, categorizedStats["Core.Game"]!!.size)
    }

    @Test
    fun `test export formats include category information`() {
        val tracker = DevTracker(TrackerConfig(enabled = true))

        tracker.trackPageEnter(customName = "GamePage", category = "Page.Game")
        tracker.trackViewModelCreated<Any>(viewModel = null, className = "GameViewModel", category = "Page.Game")
        tracker.trackClassCreated<Any>(classInstance = null, className = "Game", category = "Core.Game")

        // Test TEXT export includes category section
        val textExport = tracker.exportStatistics(ExportFormat.TEXT)
        assertTrue(textExport.contains("GROUPED BY CATEGORY"), "TEXT export should have category section")
        assertTrue(textExport.contains("Category: Page.Game"))
        assertTrue(textExport.contains("Category: Core.Game"))

        // Test CSV export includes Category column
        val csvExport = tracker.exportStatistics(ExportFormat.CSV)
        assertTrue(csvExport.contains("Category,Context"), "CSV should have Category column")
        assertTrue(csvExport.contains("\"Page.Game\""))
        assertTrue(csvExport.contains("\"Core.Game\""))

        // Test JSON export includes categorizedStatistics
        val jsonExport = tracker.exportStatistics(ExportFormat.JSON)
        assertTrue(jsonExport.contains("\"categorizedStatistics\""), "JSON should have categorizedStatistics")
        assertTrue(jsonExport.contains("\"Page.Game\""))
        assertTrue(jsonExport.contains("\"Core.Game\""))
        assertTrue(jsonExport.contains("\"totalCategories\""))
    }

    @Test
    fun `test same context in same category only appears once`() {
        val tracker = DevTracker(TrackerConfig(enabled = true))

        // Track the same context multiple times with the same category
        tracker.trackFunctionCall(customName = "Game.play", category = "Core.Game")
        tracker.trackFunctionCall(customName = "Game.play", category = "Core.Game")
        tracker.trackFunctionCall(customName = "Game.play", category = "Core.Game")

        val categorizedStats = tracker.getCategorizedStats()
        val coreGameStats: List<TrackingStats> = categorizedStats["Core.Game"]!!

        // Should only have one TrackingStats entry for "Game.play"
        assertEquals(1, coreGameStats.size)
        assertEquals("Game.play", coreGameStats[0].context)
        // But should have counted all 3 function calls
        assertEquals(3, coreGameStats[0].functionCalls.get())
    }

    @Test
    fun `test tracking with details and category`() {
        val tracker = DevTracker(TrackerConfig(enabled = true))

        tracker.trackFunctionCall(
            customName = "Game.play",
            details = "coordinate=3,4",
            category = "Core.Game"
        )
        tracker.trackFunctionCall(
            customName = "FileStorage.save",
            details = "id=game1",
            category = "Storage.File"
        )

        val events: Map<String, List<TrackingEvent>> = tracker.getAllEvents()
        val gamePlayEvents: List<TrackingEvent> = events["Game.play"]!!
        val storageSaveEvents: List<TrackingEvent> = events["FileStorage.save"]!!

        assertEquals(1, gamePlayEvents.size)
        assertEquals("coordinate=3,4", gamePlayEvents[0].details)
        assertEquals("Core.Game", gamePlayEvents[0].category)

        assertEquals(1, storageSaveEvents.size)
        assertEquals("id=game1", storageSaveEvents[0].details)
        assertEquals("Storage.File", storageSaveEvents[0].category)
    }
}

