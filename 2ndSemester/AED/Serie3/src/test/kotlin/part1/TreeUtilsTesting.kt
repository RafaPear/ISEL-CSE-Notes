package part1

import kotlin.test.*

val cmp = { i1: Int, i2: Int -> i1 - i2 }

fun emptyBST(): Node<Int>? = null

fun singleNodeBST(i: Int): Node<Int>? = add(null, i, cmp)

fun populatedBST(array: IntArray): Node<Int>? {
    var tree: Node<Int>? = null
    for (i in array) tree = add(tree, i, cmp)
    return tree
}

fun populatedMirrorBST(array: IntArray): Node<Int>? {
    var tree: Node<Int>? = null
    for (i in array) tree = mirrorAdd(tree, i, cmp)
    return tree
}

fun populatedTree(array: IntArray, l: Int, r: Int): Node<Int>? {
    if (r < l) return null
    val mid = (l + r) / 2
    val root = Node(array[mid], populatedTree(array, l, mid - 1), populatedTree(array, mid + 1, r))
    return root
}

fun <E> add(root: Node<E>?, e: E, cmp: Comparator<E>): Node<E> {
    var root = root
    if (root == null) root = Node(e, null, null)
    else if (cmp.compare(e, root.value) < 0) root.left = add(root.left, e, cmp)
    else root.right = add(root.right, e, cmp)
    return root
}

fun <E> mirrorAdd(root: Node<E>?, e: E, cmp: Comparator<E>): Node<E> {
    var root = root
    if (root == null) root = Node(e, null, null)
    else if (cmp.compare(e, root.value) > 0) root.left = mirrorAdd(root.left, e, cmp)
    else root.right = mirrorAdd(root.right, e, cmp)
    return root
}

fun <E> remove(root: Node<E>?, e: E?, cmp: Comparator<E>): Node<E>? {
    var root = root
    if (root == null) return root
    else {
        if (cmp.compare(e, root.value) < 0) root.left = remove<E>(root.left, e, cmp)
        else if (cmp.compare(e, root.value) > 0) root.right = remove<E>(root.right, e, cmp)
        else { // found, delete it
            if (root.left == null) root = root.right
            else if (root.right == null) root = root.left
            else {
                val y: Node<E> = minimum(root.right!!)
                root.value = y.value
                root.right = remove(root.right, y.value, cmp)
            }
        }
        return root
    }
}

fun <E> minimum(root: Node<E>): Node<E> {
    if (root.left == null) return root
    else return minimum(root.left!!)
}

fun <E> maximum(root: Node<E>): Node<E> {
    if (root.right == null) return root
    else return minimum(root.right!!)
}

fun <E> assertEqual(root1: Node<E>?, root2: Node<E>?) {
    if (root1 != null && root2 != null) {
        assertEquals(root1.value, root2.value)
        assertEqual(root1.left, root2.left)
        assertEqual(root1.right, root2.right)
    }
    assertEquals(root1, root2)
}


















