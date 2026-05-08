package visualizer

import part2.GraphStructure
import visualizer.model.Edge
import visualizer.model.Graph
import visualizer.model.Node
import java.io.File
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator
import kotlin.math.*

fun parseGraphSimpleFromFile(fileName: String): Graph {
    val lines = File(fileName).readLines().filter { it.isNotBlank() }
    require(lines.isNotEmpty()) { "Ficheiro vazio" }
    val g = Graph()

    val labels = lines.map{ it.split("->")[0].replace(" ","") }
    val nodes = labels.mapIndexed { i, label -> label to Node(i, 0, 0, label) }.toMap()
    g.nodes += nodes.values

    lines.forEach { ln ->
        val (srcLabel, dstPart) = ln.split("->").map { it.trim() }
        val src = nodes[srcLabel] ?: return@forEach
        dstPart.split(' ').map { it.trim() }.filter { it.isNotEmpty() }.forEach { dl ->
            val dst = nodes[dl] ?: return@forEach
            g.edges += Edge(src, dst)
        }
    }

    // posicionamento inicial em círculo
    val r = 200; val cx = 400; val cy = 300; val n = g.nodes.size
    g.nodes.forEachIndexed { idx, node ->
        val ang = 2 * PI * idx / n
        node.x = (cx + r * cos(ang)).toInt()
        node.y = (cy + r * sin(ang)).toInt()
    }
    return g
}
fun generateClusteredGraph(
    clusters: Int = 4,
    perCluster: Int = 8,
    intraDegree: Int = 3,
    bridges: Int = 6,
    canvasW: Int = 800,
    canvasH: Int = 600
): Graph {
    val g = Graph()

    /* Raios adaptativos ------------------------------------------------- */
    val minSide = min(canvasW, canvasH)
    val clusterRadius = (minSide / (2.5 * clusters)).roundToInt().coerceAtLeast(60)
    val bigRadius = (minSide / 2.2).roundToInt() - clusterRadius

    var id = 0
    repeat(clusters) { c ->
        val centerAng = 2 * PI * c / clusters
        val cx = (canvasW / 2 + bigRadius * cos(centerAng)).toInt()
        val cy = (canvasH / 2 + bigRadius * sin(centerAng)).toInt()
        repeat(perCluster) { i ->
            val ang = 2 * PI * i / perCluster
            val x = (cx + clusterRadius * cos(ang)).toInt()
            val y = (cy + clusterRadius * sin(ang)).toInt()
            g.nodes += Node(id, x, y, "N$id"); id++
        }
    }

    /* Ligações internas -------------------------------------------------- */
    for (c in 0 until clusters) {
        val start = c * perCluster
        val part = g.nodes.subList(start, start + perCluster)
        part.forEachIndexed { i, v ->
            for (k in 1..intraDegree) g.edges += Edge(v, part[(i + k) % perCluster])
        }
    }

    /* Pontes entre clusters --------------------------------------------- */
    fun rndNode(cluster: Int) = g.nodes[cluster * perCluster + (0 until perCluster).random()]
    repeat(bridges) {
        val a = (0 until clusters).random(); var b: Int; do { b = (0 until clusters).random() } while (b == a)
        g.edges += Edge(rndNode(a), rndNode(b))
    }
    return g
}

fun <I, D> convertToSimpleGraph(graphStructure: GraphStructure<I, D>): Graph {
    val nodeMap = mutableMapOf<I, Node>()
    val simpleGraph = Graph()

    var index = 0
    for (vertex in graphStructure) {
        val node = Node(
            id = index,
            x = (0..800).random(),
            y = (0..600).random(),
            label = vertex.id.toString()
        )
        nodeMap[vertex.id] = node
        simpleGraph.nodes.add(node)
        index++
    }

    for ((id, vertex) in graphStructure.vertices) {
        val srcNode = nodeMap[id] ?: continue
        for (edge in vertex.getAdjacencies()) {
            val dstNode = nodeMap[edge.adjacent] ?: continue
            simpleGraph.edges.add(Edge(src = srcNode, dst = dstNode))
        }
    }

    return simpleGraph
}
