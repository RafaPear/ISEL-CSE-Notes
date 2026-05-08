package serie1.problema

// fila de prioridade da class Entry, onde o nível de prioridade basea-se únicamente no Entry.value
class EntryPriorityQueue(size: Int) {
    private val queue = Array<Entry>(size){ Entry() }
    private var size: Int = 0

    fun insert(entry: Entry) {
        if (size == queue.size) {
            throw IllegalStateException("Queue is full")
        }
        queue[size] = entry
        size++
        heapifyUp(size - 1)
    }

    fun removeMin(): Entry {
        if (size == 0 ) {
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
            if (queue[i].value < queue[parentIndex].value) {
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

            if (leftChildIndex < size && queue[leftChildIndex].value < queue[smallestIndex].value) {
                smallestIndex = leftChildIndex
            }
            if (rightChildIndex < size && queue[rightChildIndex].value < queue[smallestIndex].value) {
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