package visualizer
import part2.GraphStructure
import pt.isel.canvas.Canvas
import pt.isel.canvas.onFinish
import pt.isel.canvas.onStart
import visualizer.controller.GraphController
import visualizer.layout.ForceLayout
import visualizer.model.Graph
import visualizer.view.GraphCanvas
import kotlin.system.exitProcess

class GraphViewer(
    graph: Graph,
    nodeColor: Int = 0x2196F3,
    edgeColor: Int = 0x444444,
    timeMultiplier: Int = 0,
    width: Int = 1900,
    height: Int = 800,
    labelColor: Int = 0xFFFFFF,
) {

    constructor(
        graph1: GraphStructure<Any,Any>,
        nodeColor: Int = 0x2196F3,
        edgeColor: Int = 0x444444,
        timeMultiplier: Int = 0,
        width: Int = 1900,
        height: Int = 800,
        labelColor: Int = 0xFFFFFF,
    ): this(
        graph = convertToSimpleGraph(graph1),
        nodeColor = nodeColor,
        edgeColor = edgeColor,
        timeMultiplier = timeMultiplier,
        width = width,
        height = height,
        labelColor = labelColor
    )


    val amount = graph.nodes.size
    val calc = (amount) / (width)
    val linkDistance = if (amount < 40) 40 else amount
    val nodeRadius = if (amount < 20) 20 else amount
    private val cfg = RenderConfig(nodeRadius, edgeColor, nodeColor, labelColor, linkDistance)
    init {
        runViewer(graph, cfg, width, height, timeMultiplier)
    }
    private fun runViewer(graph: Graph, cfg: RenderConfig, w: Int, h: Int, timeMultiplier: Int) {
        onStart {
            val canvas = Canvas(w, h, 0xFFFFFF)
            val viewer = GraphCanvas(canvas, graph, cfg)
            val layout = ForceLayout(graph, cfg.linkDistance)
            GraphController(canvas, graph, viewer, cfg)
            viewer.center()

            var period = 0
            val frameTime = graph.nodes.size * timeMultiplier
            canvas.onTimeProgress(0) {
                layout.step()
                if (period<=frameTime)
                    period++
                else {
                    viewer.center()
                    viewer.render()
                    period = 0
                }
            }
        }
        onFinish {
            exitProcess(0)
        }
    }
}