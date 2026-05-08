package serie1.problema

import kotlin.time.measureTime
import serie1.parte1.createSortedPartitions
import kotlin.time.Duration
import serie1.problema.main

fun mergeSortedPartitions(outFileName: String, numWay: Int) {
    val writer = createWriter(outFileName)
    val readers = Array(numWay) { i -> createReader("$i.txt") }

    val outQueue = EntryPriorityQueue(numWay)

    // Inicializa a fila com o primeiro valor de cada subficheiro
    for (i in 0 until numWay) {
        val line = readers[i].readLine()
        if (line != null) {
            outQueue.insert(Entry(line.toInt(), i))
        }
    }

    //irá percorrer a fila até que esta acabe
    while (outQueue.isNotEmpty()) {
        val min = outQueue.removeMin()
        writer.println(min.value) //printa no ficheiro de saída o elemento mais prioeitário da fila

        // Lê o próximo valor da mesma partição de onde veio o valor mínimo
        val nextLine = readers[min.file].readLine()
        if (nextLine != null) { // apenas inser na fila esse novo elemento se não for nulo
            outQueue.insert(Entry(nextLine.toInt(), min.file))
        }
    }

    writer.close()
    for (i in readers){
        i.close()
    }
}

fun sortFile(fileName: String, outFileName: String, partitions: Int) {
    var numWays = 0
    println("Partitioning File")
    numWays = createSortedPartitions(fileName, partitions)

    println("Sorting File (Merging)")
    mergeSortedPartitions(outFileName, numWays)
}

fun main(args: Array<String>) {
    if (args.size != 3) error("Arguments: input output partitionSize")

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

    sortFile(input, output, partition)
}


fun check(file: String) {
    val reader = createReader(file)
    var line = reader.readLine()
    var lastLine = line
    while (line != null) {
        if (lastLine != null && lastLine.toInt() > line.toInt()) {
            error("Out of order: $lastLine > $line")
        }
        lastLine = line
        line = reader.readLine()
    }
}