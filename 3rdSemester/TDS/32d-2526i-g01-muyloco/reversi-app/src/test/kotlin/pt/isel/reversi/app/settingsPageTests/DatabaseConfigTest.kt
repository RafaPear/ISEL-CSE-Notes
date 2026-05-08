package pt.isel.reversi.app.settingsPageTests

import androidx.compose.ui.test.*
import pt.isel.reversi.app.app.state.AppState
import pt.isel.reversi.app.app.state.ReversiScope
import pt.isel.reversi.app.app.state.testTagButtonConfirm
import pt.isel.reversi.app.pages.settingsPage.sections.*
import pt.isel.reversi.core.game.gameServices.EmptyGameService
import pt.isel.reversi.core.loadCoreConfig
import pt.isel.reversi.core.storage.GameStorageType
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class DatabaseConfigTest {
    val appState = AppState.empty(EmptyGameService())
    val reversiScope = ReversiScope(appState)

    @BeforeTest
    @AfterTest
    fun cleanUp() {
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `check if database URI text field appears for DATABASE_STORAGE`() = runComposeUiTest {
        setContent {
            reversiScope.CoreConfigSection(
                coreConfig = loadCoreConfig().copy(gameStorageType = GameStorageType.DATABASE_STORAGE),
                onConfigChange = { /* No-op for testing */ }
            )
        }

        onNodeWithTag(testTagDbURITextField()).assertExists()
    }

    @Test
    fun `check if database port text field appears for DATABASE_STORAGE`() = runComposeUiTest {
        setContent {
            reversiScope.CoreConfigSection(
                coreConfig = loadCoreConfig().copy(gameStorageType = GameStorageType.DATABASE_STORAGE),
                onConfigChange = { /* No-op for testing */ }
            )
        }

        onNodeWithTag(testTagDbPortTextField()).assertExists()
    }

    @Test
    fun `check if database name text field appears for DATABASE_STORAGE`() = runComposeUiTest {
        setContent {
            reversiScope.CoreConfigSection(
                coreConfig = loadCoreConfig().copy(gameStorageType = GameStorageType.DATABASE_STORAGE),
                onConfigChange = { /* No-op for testing */ }
            )
        }

        onNodeWithTag(testTagDbNameTextField()).assertExists()
    }

    @Test
    fun `check if database user text field appears for DATABASE_STORAGE`() = runComposeUiTest {
        setContent {
            reversiScope.CoreConfigSection(
                coreConfig = loadCoreConfig().copy(gameStorageType = GameStorageType.DATABASE_STORAGE),
                onConfigChange = { /* No-op for testing */ }
            )
        }

        onNodeWithTag(testTagDbUserTextField()).assertExists()
    }

    @Test
    fun `check if database password text field appears for DATABASE_STORAGE`() = runComposeUiTest {
        setContent {
            reversiScope.CoreConfigSection(
                coreConfig = loadCoreConfig().copy(gameStorageType = GameStorageType.DATABASE_STORAGE),
                onConfigChange = { /* No-op for testing */ }
            )
        }

        onNodeWithTag(testTagDbPasswordTextField()).assertExists()
    }

    @Test
    fun `check that database fields do not appear for FILE_STORAGE`() = runComposeUiTest {
        setContent {
            reversiScope.CoreConfigSection(
                coreConfig = loadCoreConfig().copy(gameStorageType = GameStorageType.FILE_STORAGE),
                onConfigChange = { /* No-op for testing */ }
            )
        }

        onNodeWithTag(testTagDbURITextField()).assertDoesNotExist()
        onNodeWithTag(testTagDbPortTextField()).assertDoesNotExist()
        onNodeWithTag(testTagDbNameTextField()).assertDoesNotExist()
        onNodeWithTag(testTagDbUserTextField()).assertDoesNotExist()
        onNodeWithTag(testTagDbPasswordTextField()).assertDoesNotExist()
    }

    @Test
    fun `Verify that the popup appears the first time when changing the URI, port, name, user, or password`() = runComposeUiTest {
        val testTags = listOf(
            testTagDbURITextField(),
            testTagDbPortTextField(),
            testTagDbNameTextField(),
            testTagDbUserTextField(),
            testTagDbPasswordTextField()
        )

        val newInput = "top_secret_input"

        testTags.forEach { testTag ->
            //need to reset the content for each test to avoid state carry-over
            setContent {
                reversiScope.CoreConfigSection(
                    coreConfig = loadCoreConfig().copy(gameStorageType = GameStorageType.DATABASE_STORAGE),
                    onConfigChange = { /* No-op for testing */ }
                )
            }

            // First change - popup should appear
            onNodeWithTag(testTag).performTextInput(newInput)
            onNodeWithTag(testTagPopUpConfirmButton()).assertExists()

            // Confirm the change
            onNodeWithTag(testTagButtonConfirm()).performClick()

            // Second change - popup should not appear
            onNodeWithTag(testTag).performTextInput(newInput + "_again")
            onNodeWithTag(testTagPopUpConfirmButton()).assertDoesNotExist()
        }
    }

    @Test
    fun `verify that changing database port to invalid value does not call onConfigChange`() = runComposeUiTest {
        var onConfigChangeCalled = false

        setContent {
            reversiScope.CoreConfigSection(
                coreConfig = loadCoreConfig().copy(gameStorageType = GameStorageType.DATABASE_STORAGE),
                onConfigChange = {
                    onConfigChangeCalled = true
                }
            )
        }

        // confirm any previous pop-ups
        onNodeWithTag(testTagDbPortTextField()).performTextInput(" ")
        onNodeWithTag(testTagButtonConfirm()).performClick()

        // Try to input an invalid port (non-numeric)
        onNodeWithTag(testTagDbPortTextField()).performTextInput("invalid_port")

        // Verify that onConfigChange was not called
        assert(!onConfigChangeCalled) { "onConfigChange should not be called for invalid port input." }

        onNodeWithTag(testTagDbPortTextField()).performTextInput("-1")

        // Verify that onConfigChange was not called
        assert(!onConfigChangeCalled) { "onConfigChange should not be called for negative port input." }
    }

    @Test
    fun `verify that changing, URI, port,name, user, or password calls onConfigChange`(): Unit = runComposeUiTest {
        val testTags = listOf(
            testTagDbURITextField(),
            testTagDbPortTextField(),
            testTagDbNameTextField(),
            testTagDbUserTextField(),
            testTagDbPasswordTextField()
        )

        val inputs = listOf(
            "new_database_uri",
            "5433", // assuming valid port
            "new_database_name",
            "new_user",
            "new_password"
        )

        var onConfigChangeCalled: Boolean

        //need to reset the content for each test to avoid state carry-over
        setContent {
            reversiScope.CoreConfigSection(
                coreConfig = loadCoreConfig().copy(gameStorageType = GameStorageType.DATABASE_STORAGE),
                onConfigChange = {
                    onConfigChangeCalled = true
                }
            )
        }
        // confirm any previous pop-ups
        onNodeWithTag(testTagDbPortTextField()).performTextInput("5433")
        onNodeWithTag(testTagButtonConfirm()).performClick()

        testTags.forEachIndexed { index, testTag ->
            onConfigChangeCalled = false

            onNodeWithTag(testTag).performTextInput(inputs[index])

            // Verify that onConfigChange was called
            assert(onConfigChangeCalled) { "onConfigChange should be called when changing $testTag." }
        }
    }
}
