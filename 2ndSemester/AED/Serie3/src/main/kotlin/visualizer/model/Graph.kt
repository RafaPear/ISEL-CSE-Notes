package visualizer.model

data class Node(val id: Int, var x: Int, var y: Int, val label: String)
data class Edge(val src: Node, val dst: Node)
class Graph {
    val nodes = mutableListOf<Node>()
    val edges = mutableListOf<Edge>()
}