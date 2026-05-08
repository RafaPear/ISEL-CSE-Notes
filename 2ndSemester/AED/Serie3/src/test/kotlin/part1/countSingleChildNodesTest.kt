package part1

import org.junit.jupiter.api.Test
import kotlin.test.*

class countSingleChildNodesTest {

    @Test
    fun countSingleChildNodes_emptyTree() {
        val tree = emptyBST()
        assertEquals(0, countSingleChildNodes(tree))
    }

    @Test
    fun countSingleChildNodes_singleNodeTree() {
        val tree = singleNodeBST(1)
        assertEquals(0, countSingleChildNodes(tree))
    }

    @Test
    fun countSingleChildNodes_fullTree() {
        val ar = intArrayOf(8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36)
        var tree = populatedTree(ar, 0, ar.size - 1)
        assertEquals(0, countSingleChildNodes(tree))
        tree = remove(tree, 12, cmp)
        tree = remove(tree, 16, cmp)
        tree = remove(tree, 24, cmp)
        tree = remove(tree, 36, cmp)
        assertEquals(4, countSingleChildNodes(tree))
    }

    @Test
    fun countSingleChildNodes_balancedTree() {
        val ar = intArrayOf(40, 20, 30, 60, 10, 15, 70, 55, 80)
        val tree = populatedTree(ar, 0, ar.size - 1)
        assertEquals(2, countSingleChildNodes(tree))
    }

    @Test
    fun countSingleChildNodes_someTree() {
        val ar = intArrayOf(10, 4, 1, 8, 5, 6, 7, 0, 2, 3, 9, 12, 11)
        val tree = populatedBST(ar)
        assertEquals(4, countSingleChildNodes(tree))
    }
}





