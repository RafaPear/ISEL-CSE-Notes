package part1
import org.junit.jupiter.api.Test
import kotlin.test.*
import kotlin.math.pow

class zigzagTraversalTest {
    @Test
    fun zigzagTraversalTest_Empty() {
        val tree = emptyBST()
        assertEquals(emptyList(), zigzagTraversal(tree))
    }

    @Test
    fun zigzagTraversalTest_SingleNode() {
        val tree = populatedBST(intArrayOf(5))
        val expected = listOf(listOf(5))
        assertEquals(expected,zigzagTraversal(tree))
    }

    @Test
    fun zigzagTraversalTest_SingleBranch() {
        var arr = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        var tree = populatedBST(arr)
        var expected = arr.map { listOf(it) }
        assertEquals(expected, zigzagTraversal(tree))

        arr = intArrayOf(5,10,6,9,7,8)
        tree = populatedBST(arr)
        expected = arr.map { listOf(it) }
        assertEquals(expected, zigzagTraversal(tree))
    }

    @Test
    fun zigzagTraversalTest_BalancedTree() {
        val expected = listOf(
            listOf(       10   ),
            listOf(   5    ,     15 ).reversed(),
            listOf( 2 , 7  ,  12 ,  17),
            listOf(1,3,6,8 ,11,13,16,18).reversed()
        )
        val totalElements =
            (2.0).pow((expected.size).toDouble()).toInt() - 1
        val arr = IntArray(totalElements)
        var index = 0
        for (level in expected) {
            for (value in level) {
                arr[index++] = value
            }
        }
        val tree = populatedBST(arr)
        assertEquals(expected, zigzagTraversal(tree))
    }
}