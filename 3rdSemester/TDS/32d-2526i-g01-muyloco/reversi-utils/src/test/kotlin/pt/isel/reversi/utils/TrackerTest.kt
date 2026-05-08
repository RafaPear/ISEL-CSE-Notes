package pt.isel.reversi.utils

import java.io.File
import kotlin.test.*

class TrackerTest {

    private lateinit var tracker: DevTracker

    @BeforeTest
    fun setUp() {
        tracker = DevTracker()
    }

    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun testTrackPageEnter() {
        tracker.trackPageEnter("TestPage")

        val stats = tracker.getStats("TestPage")
        assertNotNull(stats)
        assertEquals(1, stats.pageEnters.get())
        assertEquals(1, stats.eventCount.get())
    }

    @Test
    fun testTrackMultiplePageEnters() {
        tracker.trackPageEnter("Page1")
        tracker.trackPageEnter("Page1")
        tracker.trackPageEnter("Page2")

        val stats1 = tracker.getStats("Page1")
        val stats2 = tracker.getStats("Page2")

        assertNotNull(stats1)
        assertNotNull(stats2)
        assertEquals(2, stats1.pageEnters.get())
        assertEquals(1, stats2.pageEnters.get())
    }

    @Test
    fun testTrackViewModelCreated() {
        tracker.trackViewModelCreated<String>(className = "GameViewModel")

        val stats = tracker.getStats("GameViewModel")
        assertNotNull(stats)
        assertEquals(1, stats.viewModelCreations.get())
        assertEquals(1, stats.eventCount.get())
    }

    @Test
    fun testTrackRecomposition() {
        tracker.trackRecomposition("TestComposable")

        val stats = tracker.getStats("TestComposable")
        assertNotNull(stats)
        assertEquals(1, stats.recompositions.get())
        assertEquals(1, stats.eventCount.get())
    }

    @Test
    fun testTrackMultipleRecompositions() {
        repeat(5) {
            tracker.trackRecomposition("Button")
        }

        val stats = tracker.getStats("Button")
        assertNotNull(stats)
        assertEquals(5, stats.recompositions.get())
    }

    @Test
    fun testTrackFunctionCall() {
        tracker.trackFunctionCall("processData", "items=10")

        val stats = tracker.getStats("processData")
        assertNotNull(stats)
        assertEquals(1, stats.functionCalls.get())
    }

    @Test
    fun testTrackCustom() {
        tracker.trackCustom("UserAction", "ButtonPressed")

        val stats = tracker.getStats("UserAction")
        assertNotNull(stats)
        assertEquals(1, stats.eventCount.get())
    }

    @Test
    fun testTrackEffects() {
        tracker.trackEffectStart<String>(effectName = "LaunchEffect")
        tracker.trackEffectStart<String>(effectName = "LaunchEffect")
        tracker.trackEffectStop<String>(effectName = "LaunchEffect")

        val stats = tracker.getStats("LaunchEffect")
        assertNotNull(stats)
        assertEquals(2, stats.effectStarts.get())
        assertEquals(1, stats.effectStops.get())
        assertEquals(3, stats.eventCount.get())
    }

    @Test
    fun testGetAllStats() {
        tracker.trackPageEnter("Page1")
        tracker.trackPageEnter("Page2")
        tracker.trackRecomposition("Comp1")

        val allStats = tracker.getAllStats()
        assertEquals(3, allStats.size)
        assertTrue(allStats.containsKey("Page1"))
        assertTrue(allStats.containsKey("Page2"))
        assertTrue(allStats.containsKey("Comp1"))
    }

    @Test
    fun testGetEvents() {
        tracker.trackPageEnter("TestPage")
        tracker.trackPageEnter("TestPage")

        val events = tracker.getEvents("TestPage")
        assertEquals(2, events.size)
        assertTrue(events.all { it.type == TrackingType.PAGE_ENTER })
    }

    @Test
    fun testGetAllEvents() {
        tracker.trackPageEnter("Page1")
        tracker.trackRecomposition("Comp1")
        tracker.trackCustom("Event1", "details")

        val allEvents = tracker.getAllEvents()
        assertEquals(3, allEvents.size)
    }

    @Test
    fun testGetTotalEventsTracked() {
        tracker.trackPageEnter("Page1")
        tracker.trackPageEnter("Page2")
        tracker.trackRecomposition("Comp1")

        assertEquals(3, tracker.getTotalEventsTracked())
    }

    @Test
    fun testSetEnabled() {
        tracker.setEnabled(true)
        assertTrue(tracker.isEnabled())

        tracker.trackPageEnter("Page1")
        assertEquals(1, tracker.getTotalEventsTracked())

        tracker.setEnabled(false)
        assertFalse(tracker.isEnabled())

        tracker.trackPageEnter("Page2")
        // Total should still be 1 because tracking is disabled
        assertEquals(1, tracker.getTotalEventsTracked())
    }

    @Test
    fun testClear() {
        tracker.trackPageEnter("Page1")
        tracker.trackRecomposition("Comp1")
        tracker.trackCustom("Event1", "details")

        assertEquals(3, tracker.getTotalEventsTracked())

        tracker.clear()

        assertEquals(0, tracker.getTotalEventsTracked())
        assertEquals(0, tracker.getAllStats().size)
        assertEquals(0, tracker.getAllEvents().size)
    }

    @Test
    fun testExportStatisticsText() {
        tracker.trackPageEnter("TestPage")
        tracker.trackRecomposition("TestComp")

        val exported = tracker.exportStatistics(ExportFormat.TEXT)

        assertTrue(exported.contains("TRACKING STATISTICS"))
        assertTrue(exported.contains("Total contexts tracked: 2"))
        assertTrue(exported.contains("Total events tracked: 2"))
        assertTrue(exported.contains("TestPage"))
        assertTrue(exported.contains("TestComp"))
    }

    @Test
    fun testExportStatisticsCSV() {
        tracker.trackPageEnter("Page1")
        tracker.trackRecomposition("Comp1")

        val exported = tracker.exportStatistics(ExportFormat.CSV)

        assertTrue(exported.contains("Context,Total Events"))
        assertTrue(exported.contains("Page1"))
        assertTrue(exported.contains("Comp1"))
    }

    @Test
    fun testExportStatisticsJSON() {
        tracker.trackPageEnter("Page1")
        tracker.trackRecomposition("Comp1")

        val exported = tracker.exportStatistics(ExportFormat.JSON)

        assertTrue(exported.contains("\"totalContexts\""))
        assertTrue(exported.contains("\"totalEvents\""))
        assertTrue(exported.contains("\"statistics\""))
        assertTrue(exported.contains("Page1"))
        assertTrue(exported.contains("Comp1"))
    }

    @Test
    fun testTrackClassCreated() {
        tracker.trackClassCreated<String>(className = "Game")
        tracker.trackClassCreated<String>(className = "Game")

        val stats = tracker.getStats("Game")
        assertNotNull(stats)
        assertEquals(2, stats.classCreations.get())
    }

    @Test
    fun testUpdateConfig() {
        val newConfig = TrackerConfig(
            enabled = true,
            maxEventsPerContext = 500,
            autoSave = false,
            stackTraceDepth = 3
        )
        tracker.updateConfig(newConfig)

        // Test that config was updated by checking enabled state
        assertTrue(tracker.isEnabled())
    }

    @Test
    fun testMaxEventsPerContext() {
        val config = TrackerConfig(maxEventsPerContext = 3)
        tracker.updateConfig(config)

        tracker.trackPageEnter("Page1")
        tracker.trackPageEnter("Page1")
        tracker.trackPageEnter("Page1")
        tracker.trackPageEnter("Page1") // This should remove the first one

        val events = tracker.getEvents("Page1")
        assertEquals(3, events.size) // Only last 3 should remain
    }

    @Test
    fun testTrackWithCustomName() {
        tracker.trackPageEnter("CustomPageName")
        tracker.trackRecomposition("CustomCompName")

        val pageStats = tracker.getStats("CustomPageName")
        val compStats = tracker.getStats("CustomCompName")

        assertNotNull(pageStats)
        assertNotNull(compStats)
        assertEquals(1, pageStats.pageEnters.get())
        assertEquals(1, compStats.recompositions.get())
    }

    @Test
    fun testTrackMixedEvents() {
        tracker.trackPageEnter("HomePage")
        tracker.trackViewModelCreated<String>(className = "HomeViewModel")
        tracker.trackRecomposition("HomeScreen")
        tracker.trackFunctionCall("loadData", "items=5")
        tracker.trackCustom("UserInteraction", "scrolled")

        assertEquals(5, tracker.getTotalEventsTracked())
        assertEquals(5, tracker.getAllStats().size)
    }

    @Test
    fun testFirstAndLastOccurrence() {
        tracker.trackPageEnter("TestPage")
        Thread.sleep(10) // Small delay to ensure different timestamps
        tracker.trackPageEnter("TestPage")

        val stats = tracker.getStats("TestPage")
        assertNotNull(stats)
        assertNotNull(stats.firstOccurrence)
        assertNotNull(stats.lastOccurrence)
        assertTrue(stats.lastOccurrence!! >= stats.firstOccurrence!!)
    }

    @Test
    fun testGetNullStats() {
        val stats = tracker.getStats("NonExistentContext")
        assertNull(stats)
    }

    @Test
    fun testGetEmptyEvents() {
        val events = tracker.getEvents("NonExistentContext")
        assertEquals(0, events.size)
    }

    @Test
    fun testTrackViewModelWithObject() {
        class TestViewModel

        val viewModel = TestViewModel()
        tracker.trackViewModelCreated<TestViewModel>(viewModel)

        val stats = tracker.getStats("TestViewModel")
        assertNotNull(stats)
        assertEquals(1, stats.viewModelCreations.get())
    }

    @Test
    fun testAutoSaveConfig() {
        val config = TrackerConfig(autoSave = true)
        tracker.updateConfig(config)

        tracker.trackPageEnter("Page1")

        val filePathResult = tracker.setTrackerFilePath(autoSave = true)
        assertTrue(filePathResult.isNotEmpty())
        assertTrue(tracker.isAutoSaveEnabled())
    }

    @Test
    fun testSetAndGetTrackerFilePath() {
        val filePath = tracker.setTrackerFilePath(autoSave = false)

        assertNotNull(filePath)
        assertTrue(filePath.contains("reversi-tracking"))
        assertEquals(filePath, tracker.getTrackerFilePath())
    }

    @Test
    fun testSetAutoSaveEnabled() {
        tracker.setTrackerFilePath(autoSave = false)
        assertFalse(tracker.isAutoSaveEnabled())

        tracker.setAutoSaveEnabled(true)
        assertTrue(tracker.isAutoSaveEnabled())

        tracker.setAutoSaveEnabled(false)
        assertFalse(tracker.isAutoSaveEnabled())
    }

    @Test
    fun testSaveToFile() {
        tracker.trackPageEnter("TestPage")
        tracker.trackRecomposition("TestComp")

        val filePath = tracker.setTrackerFilePath(autoSave = false)
        val success = tracker.saveToFile(ExportFormat.TEXT)

        assertTrue(success)
        assertTrue(File(filePath).exists())

        // Cleanup
        File(filePath).delete()
    }

    @Test
    fun testAppendEventToFile() {
        tracker.setTrackerFilePath()

        val event = TrackingEvent(
            timestamp = java.time.LocalDateTime.now(),
            type = TrackingType.PAGE_ENTER,
            context = "TestPage",
            details = "test details"
        )

        val success = tracker.appendEventToFile(event)
        assertTrue(success)
    }

    @Test
    fun testMultipleContextsStatistics() {
        // Track different event types for different contexts
        for (i in 1..3) {
            tracker.trackPageEnter("Page$i")
        }
        for (i in 1..2) {
            tracker.trackRecomposition("Comp$i")
        }
        tracker.trackViewModelCreated<String>(className = "ViewModel")

        val allStats = tracker.getAllStats()
        assertEquals(6, allStats.size) // 3 pages + 2 comps + 1 viewmodel

        val allEvents = tracker.getAllEvents()
        assertEquals(6, allEvents.size)
        assertEquals(6, tracker.getTotalEventsTracked())
    }

    @Test
    fun testEventTypeCounting() {
        tracker.trackPageEnter("Context1")
        tracker.trackViewModelCreated<String>(className = "Context1")
        tracker.trackRecomposition("Context1")
        tracker.trackFunctionCall("Context1")
        tracker.trackCustom("Context1", "custom")

        val stats = tracker.getStats("Context1")
        assertNotNull(stats)
        assertEquals(1, stats.pageEnters.get())
        assertEquals(1, stats.viewModelCreations.get())
        assertEquals(1, stats.recompositions.get())
        assertEquals(1, stats.functionCalls.get())
        assertEquals(5, stats.eventCount.get())
    }
}


