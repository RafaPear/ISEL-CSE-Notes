package serie1.problema

import serie1.parte1.createSortedPartitions
import java.util.PriorityQueue
import kotlin.time.Duration
import kotlin.time.measureTime

fun mergeSortedPartitions2(outFileName: String, numWay: Int) {
    val writer = createWriter(outFileName)
    val readers = Array(numWay) { i -> createReader("$i.txt") }

    val outQueue = PriorityQueue<Entry>(numWay)

    // Inicializa a fila com o primeiro valor de cada partição
    for (i in 0 until numWay) {
        val line = readers[i].readLine()
        if (line != null) {
            outQueue.add(Entry(line.toInt(), i))
        }
    }

    while (outQueue.isNotEmpty()) {

        val min = outQueue.poll()
        writer.println(min.value)

        // Lê o próximo valor da mesma partição de onde veio o valor mínimo
        val nextLine = readers[min.file].readLine()
        if (nextLine != null) {
            outQueue.add(Entry(nextLine.toInt(), min.file))
        }
    }

    writer.close()
    readers.forEach { it.close() }
}

fun sortFile2(fileName: String, outFileName: String, partitions: Int) {
    var numWays = 0
    println("Partitioning File")
    numWays = createSortedPartitions(fileName, partitions)

    println("Sorting File (Merging)")
    mergeSortedPartitions2(outFileName, numWays)
}

fun main(args: Array<String>) {
    if (args.size != 3) error("Arguments: input.txt output.txt partitionSize")

    var input: String? = null
    var output: String? = null
    var partition: Int? = null

    try {
        input = args[0]
        output = args[1]
        partition = args[2].toInt()
    } catch (e: NumberFormatException) {
        error("Argument 'partitionSize' must be a valid integer.")
    } catch (e: Exception) {
        error("Error while processing arguments: ${e.message}")
    }

    sortFile2(input, output, partition)
}