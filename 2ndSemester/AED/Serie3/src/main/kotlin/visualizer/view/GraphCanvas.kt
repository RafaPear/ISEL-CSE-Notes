package visualizer.view

import pt.isel.canvas.Canvas
import visualizer.RenderConfig
import visualizer.model.Graph

class GraphCanvas(
    private val canvas: Canvas,
    private val graph: Graph,
    private val cfg: RenderConfig = RenderConfig()
) {
    // offset públicos para que o controlador conheça a tradução
    var offsetX = 0; private set
    var offsetY = 0; private set

    /** Calcula deslocamento para manter o grafo centrado no canvas */
    fun center() {
        if (graph.nodes.isEmpty()) return
        val minX = graph.nodes.minOf { it.x }
        val maxX = graph.nodes.maxOf { it.x }
        val minY = graph.nodes.minOf { it.y }
        val maxY = graph.nodes.maxOf { it.y }
        offsetX = (canvas.width - (maxX - minX)) / 2 - minX
        offsetY = (canvas.height - (maxY - minY)) / 2 - minY
    }

    fun render() {
        canvas.erase()
        // desenha arestas primeiro
        graph.edges.forEach {
            canvas.drawLine(
                it.src.x + offsetX, it.src.y + offsetY,
                it.dst.x + offsetX, it.dst.y + offsetY,
                cfg.edgeColor, 2
            )
        }
        // desenha nós por cima
        graph.nodes.forEach {
            canvas.drawCircle(
                it.x + offsetX, it.y + offsetY,
                cfg.nodeRadius, cfg.nodeColor, 0
            )
            canvas.drawText(
                it.x + offsetX - 8, it.y + offsetY + 6,
                it.label, cfg.labelColor, 14
            )
        }
    }
}
