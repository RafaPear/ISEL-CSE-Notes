package problem

import part2.GraphStructure
import java.io.File

fun parseGraphFromFile(file: String): GraphStructure<Any,Any> {
    val graph = GraphStructure<Any,Any>()
    var lines = emptyList<String>()
    try {
        lines = File(file).readLines()
    } catch (e: Exception) {
        println("Erro ao ler arquivo: ${e.message}")
    }
    if (!lines.isNotEmpty()) error("Ficheiro vazio")

    lines.forEach { it ->
        val line = it.split("->")
        val vertice = line[0].replace(" ","")
        graph.addVertex(vertice,vertice)

        line[1].split(" ").forEach {adj->
            if (adj != "") {
                graph.addEdge(vertice, adj)
            }
        }
    }
    return graph
}