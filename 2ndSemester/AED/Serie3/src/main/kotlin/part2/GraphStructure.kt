package part2
import visualizer.GraphViewer
import visualizer.convertToSimpleGraph
import kotlin.collections.mutableSetOf

//
// Uncomment interface declaration and implement its methods
//

class GraphStructure<I, D>: Graph<I, D> {
    override val size: Int
        get() = vertices.size

    val vertices = mutableMapOf<I, Vertex<I,D>>()

    class Vertex<I,D>(override val id: I,override var data: D): Graph.Vertex<I,D> {
        private val adjacents = mutableSetOf<Graph.Edge<I>>()

        override fun setData(newData: D): D {
            val temp = data
            data = newData
            return temp
        }

        override fun getAdjacencies(): MutableSet<Graph.Edge<I>> = adjacents
    }

    class Edge<I>(override val id: I, override val adjacent: I): Graph.Edge<I>

    override fun addVertex(id: I, d: D): D? {
        val existe = vertices.putIfAbsent(id, Vertex(id,d))
        return if (existe == null) d else null
    }

    override fun addEdge(id: I, idAdj: I): I? {
        val vertex = vertices[id] ?: return null
        vertex.getAdjacencies().add(Edge(id,idAdj))
        return id
    }
    override fun getVertex(id: I): Graph.Vertex<I, D>? = vertices[id]

    override fun getEdge(id: I, idAdj: I): Graph.Edge<I>? {
        val vertice = vertices[id] ?: return null
        vertice.getAdjacencies().forEach {if (it.adjacent == idAdj) return it}
        return null
    }

    override fun iterator(): Iterator<Graph.Vertex<I, D>> = vertices.values.iterator()

    fun getCumonities(): MutableMap<D,MutableSet<I>> {
        val cumonities = mutableMapOf<D,MutableSet<I>>()
        vertices.forEach { (_,vertice) ->
            val label = vertice.data
            val cumonitie = cumonities.getOrPut(label) {mutableSetOf()}
            cumonitie.add(vertice.id)
        }
        return cumonities
    }
}
