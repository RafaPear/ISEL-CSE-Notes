package part1
import org.junit.jupiter.api.Test
import kotlin.test.*

class mirrorTreeTest {
    @Test
    fun mirrorTree_empty() {
        val tree = emptyBST()
        assertEquals(null, mirrorTree(tree))
    }

    @Test
    fun mirrorTree_SingleNodeTree() {
        val tree = add(null,1,cmp)
        assertEquals(Node(1), mirrorTree(tree))
    }

    @Test
    fun mirrorTree_SingleBranch() {
        val arr = intArrayOf(5,4,3,2,1)
        val original = populatedBST(arr)
        val expectedTree = populatedMirrorBST(arr)
        assertEquals(expectedTree,mirrorTree(original))
    }

    @Test
    fun mirrorTree_Balanced() {
        val array = intArrayOf(4,2,3,1,6,5,7)
        val original = populatedBST(array)

        val treeExpected = populatedMirrorBST(array)
        assertEquals(treeExpected,mirrorTree(original))
    }

    @Test
    fun mirrorTree_Unbalanced() {
        val array = intArrayOf(10,5,7,2,6,8,3,1,20)
        val original = populatedBST(array)

        val treeExpected = populatedMirrorBST(array)
        assertEquals(treeExpected,mirrorTree(original))
    }

    @Test
    fun mirrotTre_twoBranch() {
        val array = intArrayOf(20,19,18,17,16,15,14,13,12,11,10,
            21,22,23,24,25,26,27,28,29,30
        )
        val original = populatedBST(array)

        val treeExpected = populatedMirrorBST(array)
        assertEquals(treeExpected,mirrorTree(original))
    }
}