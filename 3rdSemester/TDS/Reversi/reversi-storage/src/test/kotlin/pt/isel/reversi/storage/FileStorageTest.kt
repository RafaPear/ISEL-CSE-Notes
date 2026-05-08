package pt.isel.reversi.storage

import java.io.File
import kotlin.test.Test
import kotlin.test.assertFails

class FileStorageTest {
    data class MockData(val id: Int, val name: String)

    class TestSerializer: Serializer<MockData, String> {
        override fun serialize(obj: MockData): String {
            return "${obj.id},${obj.name}"
        }

        override fun deserialize(obj: String): MockData {
            val parts = obj.split(",")
            return MockData(parts[0].toInt(), parts[1])
        }
    }

    val fileStorage = FileStorage(
        folder = "test-saves",
        serializer = TestSerializer()
    )

    fun cleanup(func: () -> Unit) {
        File("test-saves").deleteRecursively()
        func()
        File("test-saves").deleteRecursively()
    }

    @Test
    fun `Run new at an already existing id fails`() {
        cleanup {
            assertFails {
                fileStorage.new(1.toString()) {
                    MockData(1, "Test1")
                }
                fileStorage.new(1.toString()) {
                    MockData(1, "Test1")
                }
            }
        }
    }

    @Test
    fun `Run save at a non existing id fails`() {
        cleanup {
            assertFails {
                fileStorage.save(1.toString(), MockData(1, "Test1"))
            }
        }
    }

    @Test
    fun `Run load at a non existing id returns null`() {
        cleanup {
            val data = fileStorage.load(1.toString())
            assert(data == null)
        }
    }

    @Test
    fun `Run new and load works`() {
        cleanup {
            val data1 = fileStorage.new(1.toString()) { MockData(1, "Test1") }
            val data2 = fileStorage.load(1.toString())

            assert(data1 == data2)
        }
    }

    @Test
    fun `Run new, save and load works`() {
        cleanup {
            fileStorage.new(1.toString()) {
                MockData(1, "Test1")
            }
            val updatedData = MockData(1, "UpdatedTest1")
            fileStorage.save(1.toString(), updatedData)
            val data2 = fileStorage.load(1.toString())

            assert(data2 == updatedData)
        }
    }

    @Test
    fun `Run new and delete works`() {
        cleanup {
            fileStorage.new(1.toString()) { MockData(1, "Test1") }
            fileStorage.delete(1.toString())
            val data = fileStorage.load(1.toString())

            assert(data == null)
        }
    }

    @Test
    fun `Run delete at a non existing id fails`() {
        cleanup {
            assertFails {
                fileStorage.delete(1.toString())
            }
        }
    }

    @Test
    fun `Run multiple operations`() {
        cleanup {
            val data1 = fileStorage.new(1.toString()) { MockData(1, "Test1") }
            val data2 = fileStorage.load(1.toString())
            assert(data1 == data2)

            val updatedData = MockData(1, "UpdatedTest1")
            fileStorage.save(1.toString(), updatedData)
            val data3 = fileStorage.load(1.toString())
            assert(data3 == updatedData)

            fileStorage.delete(1.toString())
            val data4 = fileStorage.load(1.toString())
            assert(data4 == null)
        }
    }

}