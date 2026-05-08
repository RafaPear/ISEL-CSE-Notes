package serie2.part3

/********************************************************************
 * Utility functions to create and manipulate doubly linked lists
 * with sentinel node.
 *  - emptyList: creates an empty list with a sentinel node.
 *  - isEmpty: extension function that returns true if the doubly linked
 *             list with sentinel is empty.
 *  - toList: extension function of Node<E> that
 *            creates a List<E> from a doubly linked list with sentinel.
 *  - toLinkedList: extension function of List<E> that
 *                  creates a doubly linked list with a sentinel node.
 *  - singletonList: creates a doubly linked list with a sentinel node
 *                      with a single element
 */

/**
 * Create an empty doubly linked list with a sentinel node.
 * @return a doubly linked list with a sentinel node empty.
 */
fun <E> emptyList(): Node<E> {
    val empty = Node<E>()
    empty.previous = empty
    empty.next = empty.previous
    return empty
}

fun <E> Node<E>.isEmpty(): Boolean = this.next === this && this.previous === this

private fun <E> addAfter(node: Node<E>, v: E ) {
    val n = Node<E>(v, node.next, node)
    node.next?.previous = n
    node.next = n
}

/**
 * Create a doubly linked list with a sentinel node with one element.
 * @param value the value of the single element.
 * @return a doubly linked list with a sentinel node.
 */
fun <E> singletonList(value: E): Node<E> {
    val l: Node<E> = emptyList()
    l.previous?.let { addAfter(it, value) }
    return l
}

/**
 * Extension function to create a doubly linked list with
 * a sentinel node from a List.
 * @return a doubly linked list with a sentinel node.
 */
fun <E> List<E>.toLinkedList(): Node<E> {
    val l: Node<E> = emptyList()
    for (v in this) {
        l.previous?.let { addAfter(it, v) }
    }
    return l
}

/**
 * Extension function to create a List from a doubly linked
 * list with sentinel.
 * The node must be the sentinel node.
 * @return a List with the elements of the doubly linked list.
 */
fun <E> Node<E>.toList(): List<E> {
    val res: MutableList<E> = mutableListOf<E>()
    var curr = this.next
    while ( curr !== this ) {
       curr?.let {
          res.add(it.value)
          curr = it.next
       }
    }
    return res
}

/********************************************************************
 * Utility functions to create and manipulate doubly linked lists
 * without sentinel.
 *  - nodeToList: creates a List<E> from a doubly linked list without sentinel.
 *  - listToNode: create a linked list without sentinel from a List<E>.
 */

/**
 *  Create a List from a doubly linked list without sentinel.
 *  @param l the head of doubly linked list to convert.
 *  @return a List with the elements of the doubly linked list.
 */
fun <E> nodeToList(l: Node<E>?): List<E> {
    val res: MutableList<E> = mutableListOf<E>()
    var curr = l
    while ( curr != null ) {
       res.add(curr.value)
       curr = curr.next
    }
    return res
}

/** Create a doubly linked list without sentinel from a List.
 * @param l the list to convert.
 * @return a doubly linked list.
 */
fun <E> listToNode(l: List<E>): Node<E>? {
    return l.toLinkedList().removeSentinel()
}

/* Extension function to remove the sentinel node from a
 * doubly linked list with sentinel.
 */
private fun <E> Node<E>.removeSentinel( ): Node<E>?{
    previous?.next = null
    next?.previous = null
    return next
}