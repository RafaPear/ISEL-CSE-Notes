package serie2.part1_2

import kotlin.test.*

class TestIntArrayList {
    val N = 1000

    @Test
    fun testWithOneElement() {
        val q = IntArrayList(1)
        assertTrue(q.append(10))
        assertEquals(10, q.get(0))
        assertFalse(q.append(10))
        q.addToAll(10)
        assertEquals(20, q.get(0))
        assertTrue(q.remove())
        assertFalse(q.remove(), "can't remove, array is empty")
    }

    @Test
    fun testWithThreeElement() {
        val q = IntArrayList(3)
        assertTrue(q.append(10))
        assertEquals(10, q.get(0))
        assertTrue(q.append(20))
        assertEquals(20, q.get(1))
        q.addToAll(100)
        assertEquals(110, q.get(0))
        assertEquals(120, q.get(1))
        assertTrue(q.append(30))
        assertEquals(30, q.get(2))
        assertFalse(q.append(50))
    }

    @Test
    fun testIntArrayList() {
        val values = Array(N) { (it + 1) * 10 }
        val q = IntArrayList(values.size)
        for (i in values.indices) {
            assertTrue(q.append(values[i]))
            assertEquals(values[i], q.get(i))
        }
        assertFalse(q.append(5))
        q.addToAll(100)
        for (i in values.indices) {
            assertEquals(values[i] + 100, q.get(0))
            assertTrue(q.remove())
            assertTrue(q.append(values[i]))
            assertEquals(values[i], q.get(values.size - 1))
        }
    }
}