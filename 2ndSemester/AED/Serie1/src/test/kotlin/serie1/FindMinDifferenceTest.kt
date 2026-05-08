package serie1

import kotlin.test.*
import serie1.parte1.findMinDifference

class FindMinDifferenceTest {
    @Test
    fun findMinDifference_OnBothEmptyArrays() {
        val v1 = IntArray(0)
        val v2 = IntArray(0)
        val diff: Int = findMinDifference(v1, v2)
        assertEquals(-1, diff)

    }

    @Test
    fun findMinDifference_OnAnArraysWithOneElementAndEmpty() {
        val v1 = intArrayOf(1)
        val v2 = IntArray(0)
        var diff: Int = findMinDifference(v1, v2)
        assertEquals(-1, diff)
        diff = findMinDifference(v2, v1)
        assertEquals(-1, diff)
    }

    @Test
    fun findMinDifference_OnAnArraysWithOneElement() {
        val v1 = intArrayOf(-1)
        val v2 = intArrayOf(-1)
        var diff: Int = findMinDifference(v1, v2)
        assertEquals(0, diff)
        diff = findMinDifference(v2, v1)
        assertEquals(0, diff)
    }

    @Test
    fun findMinDifference_OnAnArraysWithRandomElements() {
        val v1 = intArrayOf(-23, -10, 34, 68)
        val v2 = intArrayOf(-15, -12, 32, 33)
        var diff: Int = findMinDifference(v1, v2)
        assertEquals(1, diff)
        diff = findMinDifference(v2, v1)
        assertEquals(1, diff)
    }

    @Test
    fun findMinDifference_OnAnArraysWithRandomNegativeElements() {
        val v1 = intArrayOf(-99, -81, -70, -68, -45, -27, -3)
        val v2 = intArrayOf(-84, -35, -25, -16, -9)
        var diff: Int = findMinDifference(v1, v2)
        assertEquals(2, diff)
        diff = findMinDifference(v2, v1)
        assertEquals(2, diff)
    }

    @Test
    fun findMinDifference_OnAnArraysWithRandomPositiveElements() {
        val v1 = intArrayOf(3, 27, 45, 68, 70, 81, 99)
        val v2 = intArrayOf(9, 16, 25, 35, 75, 84)
        var diff: Int = findMinDifference(v1, v2)
        assertEquals(2, diff)
        diff = findMinDifference(v2, v1)
        assertEquals(2, diff)
    }
}