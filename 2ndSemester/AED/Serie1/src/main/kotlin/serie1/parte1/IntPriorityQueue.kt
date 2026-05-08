package serie1.parte1

class IntPriorityQueue(size: Int) {
    private val queue: IntArray = IntArray(size)
    private var size: Int = 0

    fun insert(value: Int) {
        if (size == queue.size) {
            throw IllegalStateException("Queue is full")
        }
        queue[size] = value
        size++
        heapifyUp(size - 1)
    }

    fun removeMin(): Int {
        if (size == 0) {
            throw IllegalStateException("Queue is empty")
        }
        val min = queue[0]
        queue[0] = queue[size - 1]
        size--
        heapifyDown(0)
        return min
    }

    fun isNotEmpty(): Boolean {
        return size > 0
    }

    private fun heapifyUp(index: Int) {
        var i = index
        while (i > 0) {
            val parentIndex = (i - 1) / 2
            if (queue[i] < queue[parentIndex]) {
                swap(i, parentIndex)
                i = parentIndex
            } else {
                break
            }
        }
    }

    private fun heapifyDown(index: Int) {
        var i = index
        while (i < size) {
            val leftChildIndex = 2 * i + 1
            val rightChildIndex = 2 * i + 2
            var smallestIndex = i

            if (leftChildIndex < size && queue[leftChildIndex] < queue[smallestIndex]) {
                smallestIndex = leftChildIndex
            }
            if (rightChildIndex < size && queue[rightChildIndex] < queue[smallestIndex]) {
                smallestIndex = rightChildIndex
            }
            if (smallestIndex != i) {
                swap(i, smallestIndex)
                i = smallestIndex
            } else {
                break
            }
        }
    }

    private fun swap(i: Int, j: Int) {
        val temp = queue[i]
        queue[i] = queue[j]
        queue[j] = temp
    }
}
