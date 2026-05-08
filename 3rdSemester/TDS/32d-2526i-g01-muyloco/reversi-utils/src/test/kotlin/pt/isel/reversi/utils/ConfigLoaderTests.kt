package pt.isel.reversi.utils

import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ConfigLoaderTests {

    val config = "mock_config.properties"

    @BeforeTest
    @AfterTest
    fun cleanup() {
        File(config).deleteRecursively()
    }

    @Test
    fun `loadConfig from non-existent file works correctly`() {
        val config = ConfigLoader(config) { MockConfig(it) }.loadConfig()

        assert(config.TEST_INT == 42)
        assert(config.TEST_STRING == "default")
        assert(config.TEST_CHAR == 'X')
        assert(config.TEST_BOOL)
        assert(config.TEST_DOUBLE == 3.14)
        assert(config.TEST_LONG == 100000L)
        assert(config.TEST_FLOAT == 2.71f)
    }

    @Test
    fun `loadConfig from file with some entries works correctly`() {
        val partialConfigMap = mapOf(
            "TEST_INT" to "29",
            "TEST_STRING" to "test",
            "TEST_BOOL" to "false"
        )
        ConfigLoader(config) { MockConfig(partialConfigMap) }.loadConfig()

        val config = ConfigLoader(config) { MockConfig(it) }.loadConfig()

        assert(config.TEST_INT == 29)
        assert(config.TEST_STRING == "test")
        assert(config.TEST_CHAR == 'X') // default
        assert(!config.TEST_BOOL)
        assert(config.TEST_DOUBLE == 3.14) // default
        assert(config.TEST_LONG == 100000L) // default
        assert(config.TEST_FLOAT == 2.71f) // default
    }

    @Test
    fun `loadConfig from file with all entries works correctly`() {
        val fullConfigMap = mapOf(
            "TEST_INT" to "200",
            "TEST_STRING" to "full",
            "TEST_CHAR" to "Z",
            "TEST_BOOL" to "true",
            "TEST_DOUBLE" to "6.28",
            "TEST_LONG" to "200000",
            "TEST_FLOAT" to "1.61"
        )
        ConfigLoader(config) { MockConfig(fullConfigMap) }.loadConfig()

        val config = ConfigLoader(config) { MockConfig(it) }.loadConfig()

        assert(config.TEST_INT == 200)
        assert(config.TEST_STRING == "full")
        assert(config.TEST_CHAR == 'Z')
        assert(config.TEST_BOOL)
        assert(config.TEST_DOUBLE == 6.28)
        assert(config.TEST_LONG == 200000L)
        assert(config.TEST_FLOAT == 1.61f)
    }
}