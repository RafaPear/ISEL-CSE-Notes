package serie2.part1_2

import kotlin.test.*

class MinimumTest {
    @Test
    fun testHeapSize1() {
        assertEquals(5, minimum(arrayOf(5), 1))
        assertEquals(5, minimum(arrayOf(5, 4, 3, 2, 1 ), 1))
    }

    @Test
    fun testMinimum() {
        assertEquals(1, minimum(arrayOf(10,5,1,4,2), 5))
        assertEquals(1, minimum(arrayOf(15,13,14,10,11,9,12,1,7,5,6,4,3,2,8), 15))
        assertEquals(9, minimum(arrayOf(15,13,14,10,11,9,12,1,7,5,6,4,3,2,8), 7))
        assertEquals(3, minimum(Array(12) { 12-it }, 10))
    }

}