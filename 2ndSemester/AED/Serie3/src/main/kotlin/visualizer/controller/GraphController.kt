package visualizer.controller

import pt.isel.canvas.Canvas
import visualizer.RenderConfig
import visualizer.model.Graph
import visualizer.model.Node
import visualizer.view.GraphCanvas

class GraphController(
    private val canvas: Canvas,
    private val g: Graph,
    private val viewer: GraphCanvas,
    private val cfg: RenderConfig = RenderConfig(24, 0x444444, 0x2196F3, 0xFFFFFF, 140)
) {
    private var selected: Node? = null
    private val radiusSq get() = cfg.nodeRadius * cfg.nodeRadius

    init {
        // Quando se carrega no rato, verifica proximidade do nó
        canvas.onMouseDown { e ->
            val mx = e.x; val my = e.y
            selected = g.nodes.find {
                val dx = it.x + viewer.offsetX - mx
                val dy = it.y + viewer.offsetY - my
                dx*dx + dy*dy <= radiusSq
            }
        }
        // Durante o arrasto, actualiza a posição do nó seleccionado
        canvas.onMouseMove { e ->
            if (e.down) {
                selected?.let {
                    it.x = e.x - viewer.offsetX
                    it.y = e.y - viewer.offsetY
                }
            } else {
                selected = null
            }
        }
    }
}