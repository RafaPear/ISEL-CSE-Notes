package visualizer.layout

import visualizer.model.Graph
import kotlin.math.min
import kotlin.math.sqrt

class ForceLayout(private val g: Graph, private val desiredLen: Int = 140) {
    private val k = desiredLen
    private val cooling = 0.98
    private var temp = k.toDouble()

    fun step() {
        if (g.nodes.isEmpty()) return
        val dispX = DoubleArray(g.nodes.size)
        val dispY = DoubleArray(g.nodes.size)

        // força repulsiva
        for (i in g.nodes.indices) {
            for (j in i + 1 until g.nodes.size) {
                val v = g.nodes[i]
                val u = g.nodes[j]
                var dx = (v.x - u.x).toDouble()
                var dy = (v.y - u.y).toDouble()
                var dist = sqrt(dx * dx + dy * dy)
                if (dist < 0.01) dist = 0.01
                val force = k * k / dist
                dx /= dist; dy /= dist
                dispX[i] += dx * force
                dispY[i] += dy * force
                dispX[j] -= dx * force
                dispY[j] -= dy * force
            }
        }

        // força atractiva
        for (e in g.edges) {
            val v = e.src; val u = e.dst
            val idxV = g.nodes.indexOf(v)
            val idxU = g.nodes.indexOf(u)
            var dx = (v.x - u.x).toDouble()
            var dy = (v.y - u.y).toDouble()
            var dist = sqrt(dx * dx + dy * dy)
            if (dist < 0.01) dist = 0.01
            val force = (dist * dist) / k
            dx /= dist; dy /= dist
            dispX[idxV] -= dx * force
            dispY[idxV] -= dy * force
            dispX[idxU] += dx * force
            dispY[idxU] += dy * force
        }

        // aplica deslocamento
        for ((i, n) in g.nodes.withIndex()) {
            var dx = dispX[i]
            var dy = dispY[i]
            var disp = sqrt(dx * dx + dy * dy)
            if (disp < 0.01) disp = 0.01
            dx = dx / disp * min(disp, temp)
            dy = dy / disp * min(disp, temp)
            n.x += dx.toInt()
            n.y += dy.toInt()
        }
        temp *= cooling
    }
}