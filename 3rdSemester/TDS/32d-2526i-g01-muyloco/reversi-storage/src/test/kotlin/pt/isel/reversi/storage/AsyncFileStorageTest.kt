package pt.isel.reversi.storage

import kotlinx.coroutines.runBlocking
import pt.isel.reversi.utils.BASE_FOLDER
import java.io.File
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFails

class AsyncFileStorageTest {
    data class MockData(val id: Int, val name: String)

    class TestSerializer : Serializer<MockData, String> {
        override fun serialize(obj: MockData): String {
            return "${obj.id},${obj.name}"
        }

        override fun deserialize(obj: String): MockData {
            val parts = obj.split(",")
            return MockData(parts[0].toInt(), parts[1])
        }
    }

    val asyncFileStorage = AsyncFileStorage(
        folder = "test-async-saves",
        serializer = TestSerializer()
    )

    @BeforeTest
    @AfterTest
    fun cleanup() {
        File("test-async-saves").deleteRecursively()
        File(BASE_FOLDER).deleteRecursively()
    }

    @Test
    fun `Run new at an already existing id fails`() {
        assertFails {
            runBlocking {
                asyncFileStorage.new(1.toString()) {
                    MockData(1, "Test1")
                }
                asyncFileStorage.new(1.toString()) {
                    MockData(1, "Test1")
                }
            }
        }
    }

    @Test
    fun `Run save at a non existing id fails`() {
        assertFails {
            runBlocking {
                asyncFileStorage.save(1.toString(), MockData(1, "Test1"))
            }
        }
    }

    @Test
    fun `Run load at a non existing id returns null`() {
        runBlocking {
            val data = asyncFileStorage.load(1.toString())
            assert(data == null)
        }
    }

    @Test
    fun `Run new and load works`() {
        runBlocking {
            val data1 = asyncFileStorage.new(1.toString()) { MockData(1, "Test1") }
            val data2 = asyncFileStorage.load(1.toString())

            assert(data1 == data2)
        }
    }

    @Test
    fun `Run new, save and load works`() {
        runBlocking {
            asyncFileStorage.new(1.toString()) {
                MockData(1, "Test1")
            }
            val updatedData = MockData(1, "UpdatedTest1")
            asyncFileStorage.save(1.toString(), updatedData)
            val data2 = asyncFileStorage.load(1.toString())

            assert(data2 == updatedData)
        }
    }

    @Test
    fun `Run new and delete works`() {
        runBlocking {
            asyncFileStorage.new(1.toString()) { MockData(1, "Test1") }
            asyncFileStorage.delete(1.toString())
            val data = asyncFileStorage.load(1.toString())
            assert(data == null)
        }
    }

    @Test
    fun `Run delete at a non existing id does fail`() {
        runBlocking {
            assertFails {
                asyncFileStorage.delete(1.toString())
            }
        }
    }

    @Test
    fun `Run lastModified at a non existing id returns null`() {
        runBlocking {
            val lastMod = asyncFileStorage.lastModified(1.toString())
            assert(lastMod == null)
        }
    }

    @Test
    fun `Run lastModified at an existing id returns correct timestamp`() {
        runBlocking {
            asyncFileStorage.new(1.toString()) { MockData(1, "Test1") }
            val lastMod = asyncFileStorage.lastModified(1.toString())
            assert(lastMod != null && lastMod > 0)
        }
    }

    @Test
    fun `Run lastModified after save returns updated timestamp`() {
        runBlocking {
            asyncFileStorage.new(1.toString()) { MockData(1, "Test1") }
            val lastMod1 = asyncFileStorage.lastModified(1.toString())

            Thread.sleep(10) // Ensure timestamp difference

            asyncFileStorage.save(1.toString(), MockData(1, "UpdatedTest1"))
            val lastMod2 = asyncFileStorage.lastModified(1.toString())

            assert(lastMod2 != null && lastMod1 != null && lastMod2 > lastMod1)
        }
    }

    @Test
    fun `Run lastModified after delete returns null`() {
        runBlocking {
            asyncFileStorage.new(1.toString()) { MockData(1, "Test1") }
            asyncFileStorage.delete(1.toString())
            val lastMod = asyncFileStorage.lastModified(1.toString())
            assert(lastMod == null)
        }
    }

    @Test
    fun `Run loadAllIds returns all stored ids`() {
        runBlocking {
            asyncFileStorage.new(1.toString()) { MockData(1, "Test1") }
            asyncFileStorage.new(2.toString()) { MockData(2, "Test2") }
            asyncFileStorage.new(3.toString()) { MockData(3, "Test3") }

            val ids = asyncFileStorage.loadAllIds()
            assert(ids.size == 3)
            assert(ids.containsAll(listOf(1.toString(), 2.toString(), 3.toString())))
        }
    }

    @Test
    fun `Run loadAllIds when no ids stored returns empty list`() {
        runBlocking {
            val ids = asyncFileStorage.loadAllIds()
            assert(ids.isEmpty())
        }
    }
}
