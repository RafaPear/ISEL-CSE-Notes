package serie1

import kotlin.test.*
import serie1.parte1.counter

class CounterTest {
    @Test
    fun counter_EmptyArray() {
        val array = IntArray(0)
        assertEquals(Pair(0, 0), counter(array, 0, 0, 0))
    }

    @Test
    fun counter_SingletonArray() {
        val array = IntArray(1)
        assertEquals(Pair(1, 0), counter(array, 1, 1, 2))
        assertEquals(Pair(0, 0), counter(array, 2, 1, 2))
    }

    @Test
    fun counter_ExampleArray() {
        val array = intArrayOf(1, 4, 3, 2, 5)
        val p1 = counter(array, 1, 3, 3)
        assertEquals(Pair(2, 2), p1)
        val p2 = counter(array, 2, 2, 3)
        assertEquals(Pair(0, 4), p2)
    }

}