package part1

data class Node<E>(var value: E, var left:Node<E>?, var right:Node<E>?){
    constructor(value: E) : this(value,null,null)
}

// Return the number of nodes that have exactly one child.
fun <E> countSingleChildNodes(root: Node<E>?): Int {
    if (root == null) return 0

    val leftChild = root.left
    val rightChild = root.right

    // Check if the current node has exactly one child
    val condition = (leftChild != null && rightChild == null) || (leftChild == null && rightChild != null)
    val count = if (condition) 1 else 0

    // Recursively count in the left and right subtrees
    return count + countSingleChildNodes(leftChild) + countSingleChildNodes(rightChild)
}

// Swap the left and right subtrees recursively for every node.
fun <E> mirrorTree(root: Node<E>?): Node<E>? {
    if (root == null) return null

    val mirrorLeft = mirrorTree(root.right)
    val mirrorRight = mirrorTree(root.left)

    return Node(root.value, mirrorLeft,mirrorRight)
}

// Return values in alternating left-to-right and right-to-left levels.
fun <E> zigzagTraversal(root: Node<E>?): List<List<E>> {
    if (root == null) return emptyList()

    val levels: MutableList<List<E>> = mutableListOf(listOf(root.value))
    var currentLevelValues: List<E> = emptyList()
    var currentLevelNodes = listOf(root)
    var i = 0
    var isLeftToRight = false

    while (i >= 0 && currentLevelNodes.isNotEmpty()) {
        val currentNode = currentLevelNodes[i]

        val (first, second) = if (isLeftToRight)
            currentNode.left to currentNode.right
        else
            currentNode.right to currentNode.left

        if (first != null) {
            currentLevelValues += first.value
            currentLevelNodes += first
        }
        if (second != null) {
            currentLevelValues += second.value
            currentLevelNodes += second
        }

        currentLevelNodes -= currentNode
        i--

        if (i < 0) { // chegou no final do nível
            i = currentLevelValues.size - 1

            if (currentLevelValues.isNotEmpty()) levels += currentLevelValues

            currentLevelValues = emptyList()
            isLeftToRight = !isLeftToRight
        }
    }

    return levels
}











