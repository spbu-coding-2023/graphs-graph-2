package viewmodel.graph

import androidx.compose.ui.unit.dp
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import java.util.ArrayList
import kotlin.math.sqrt

class TFDPLayout() {
    private val attractionConstant = 0.001 // strength of attractive force
    private val repulsionConstant = 80.0 // strength of repulsive force
    private val dampingFactor = 0.85 // reduces oscillation of vertices

    fun <V> place(width: Double, height: Double, vertices: Collection<VertexViewModel<V>>) {
//        val forces = Array(vertices.size) { mk.ndarray(mk[0.0, 0.0]) } // TODO
        val forces = Array(vertices.size) { Pair(0.0, 0.0) }
        var k = 0

        vertices.onEach {
            val vi = it
            var forceX = 0.0
            var forceY = 0.0

            for (vj in vertices) { // repulsive forces
                if (vi == vj) continue

                val dx = vi.x.value.value - vj.x.value.value
                val dy = vi.y.value.value - vj.y.value.value
                val distance = sqrt(dx * dx + dy * dy)

                val repulsion = repulsionConstant / (distance * distance)

                forceX += dx / distance * repulsion
                forceY += dy / distance * repulsion

                val attraction = attractionConstant * (distance * distance)

                forceX -= dx / distance * attraction
                forceY -= dy / distance * attraction
            }
            forces[k] = Pair(forceX * dampingFactor, forceY * dampingFactor)
            k++
        }

        k = 0
        vertices.onEach { // update positions
            val vi = it

            vi.x.value += forces[k].first.dp
            vi.y.value += forces[k].second.dp
            k++
        }

        for (vi in vertices) { // check borders
            vi.x.value = vi.x.value.coerceIn(0.dp, width.dp - vi.radius * 2)
            vi.y.value = vi.y.value.coerceIn(0.dp, height.dp - vi.radius * 2)
        }
    }
}