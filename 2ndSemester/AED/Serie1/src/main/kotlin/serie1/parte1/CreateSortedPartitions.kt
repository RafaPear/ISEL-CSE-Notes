package serie1.parte1

import serie1.problema.createReader
import serie1.problema.createWriter
import java.io.PrintWriter


fun main() {
    println(createSortedPartitions("ints.txt",100))
}

fun createSortedPartitions(fileName: String, partitionSize: Int): Int{
    val reader = createReader(fileName)

    var temp_partitionSize = partitionSize
    var numWay = 0
    var writer = createWriter("${numWay}.txt")
    var temp_list = IntPriorityQueue(partitionSize)

    while (true) {

        val line = reader.readLine()

        if (line == null) break // quando chega no final do ficheiro quebra o loop

        //ele vai adicionando as linhas lidas a um array, até que o temp_partitionSize chegar a 0,
        //quando chega a zero chama a função createSortFile, e reinicia o processo adicionando +1 no numWay
        if (temp_partitionSize > 0) {
            temp_partitionSize--
            temp_list.insert(line.toInt())
        } else {
            createSortFile(temp_list,writer)
            temp_list = IntPriorityQueue(partitionSize)
            numWay++
            writer = createWriter("${numWay}.txt")// cria um novo ficheiro de escrita
            temp_partitionSize = partitionSize // reseta o temp_partitionSize
            temp_list.insert(line.toInt())
            temp_partitionSize--
        }
    }
    createSortFile(temp_list,writer)
    numWay++
    return numWay
}

fun createSortFile(a: IntPriorityQueue ,writer: PrintWriter) {
    // constroi um ficheiro ordenado com base na array recibida
    while (a.isNotEmpty()) {
        writer.println(a.removeMin().toString())
    }
    writer.close()
}
