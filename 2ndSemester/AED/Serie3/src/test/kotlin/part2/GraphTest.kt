package part2

import visualizer.GraphViewer
import visualizer.convertToSimpleGraph
import visualizer.model.Graph
import kotlin.test.*

class GraphTest {

     //Uncomment after implementing GraphStructure class

    @Test
    fun graph_empty() {
        val graph = GraphStructure<Int, String>()
        assertEquals(0, graph.size)
    }

    @Test
    fun graph_singleton() {
        val graph = GraphStructure<Int, String>()
        graph.addVertex(1, "V1")
        graph.addVertex(1, "V1")
        assertEquals(1, graph.size)
        assertEquals("V1", graph.getVertex(1)!!.data)
        graph.addEdge(1, 2)
        graph.addEdge(1, 3)
        assertTrue(graph.getEdge(1, 2) != null)
        assertTrue(graph.getEdge(1, 3) != null)
        graph.addEdge(1, 4)
        assertTrue(graph.getEdge(1, 4) != null)
    }

    @Test
    fun graph_equalElements() {
        val graph = GraphStructure<Int, String>()
        val setV: MutableSet<String> = mutableSetOf()
        for (id in 1..4)
            graph.addVertex(id, "V$id")
        assertEquals(4, graph.size)
        for (id in 1..4) for (id2 in 1..4)
            if (id2 != id) graph.addEdge(id, id2)
        for (v in graph)
            setV.add(v.data)
        assertEquals(setV, mutableSetOf("V1", "V2", "V3", "V4"))
        for (id in 1..4) {
            val setE: MutableSet<Int> = mutableSetOf()
            val vertex=graph.getVertex(id)
            if(vertex!=null)
               for (e in vertex.getAdjacencies())
                 setE.add(e.adjacent)
            val set2 = mutableSetOf(1, 2, 3, 4)
            set2.remove(id)
            assertEquals(setE, set2)
        }
    }

    @Test
    fun graph_someElements() {
        val graph = GraphStructure<Int, String>()
        for (id in 0..99)
            graph.addVertex(id, "V$id")
        assertEquals(100, graph.size)
        for (id in 0..99) for (id2 in 0..99)
            if (id2 != id) graph.addEdge(id, id2)
        for (id in 0..99) for (id2 in 0..99)
            if (id2 != id) graph.addEdge(id, id2)
        val setV: MutableSet<String> = mutableSetOf()
        for (id in 0..99) {
            val vertex=graph.getVertex(id)
            if(vertex!=null) setV.add(vertex.data)
        }
        val set: MutableSet<String> = mutableSetOf()
        for (id in 0..99)
            set.add("V$id")
        assertEquals(setV, set)
    }
}
