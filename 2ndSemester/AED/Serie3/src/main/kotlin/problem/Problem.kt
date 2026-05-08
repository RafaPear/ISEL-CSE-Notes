package problem
import part2.GraphStructure
import visualizer.GraphViewer


fun main() {
    val graph = parseGraphFromFile("graph1.gr")
    label_propagation(graph)
    graph.getCumonities().forEach { println(it) }
    GraphViewer(graph)
}

fun label_propagation(graph: GraphStructure<Any, Any>) {
    var stop = false

    while (!stop) {
        stop = true

        graph.forEach { vertice ->
            val count = mutableMapOf<String, Int>()

            // Conta as labels (data) dos vizinhos
            vertice.getAdjacencies().forEach { edge ->
                val adjId = edge.adjacent
                val adj = graph.getVertex(adjId)
                if (adj != null) {
                    val adjLabel = adj.data.toString()
                    count[adjLabel] = count.getOrDefault(adjLabel, 0) + 1
                }
            }

            val bestLabel = count.maxByOrNull { it.value }?.key ?: vertice.data
            if (bestLabel != vertice.data){
                vertice.setData(bestLabel)
                stop = false
            }
        }
    }
}