package viewmodel.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import kotlin.math.pow
import kotlin.math.sqrt
import androidx.compose.ui.platform.LocalDensity
import org.jetbrains.kotlinx.multik.ndarray.complex.minus
import org.jetbrains.kotlinx.multik.ndarray.operations.minus

class TFDPLayout(
    private val longRangeAttractionConstant: Float = 0.001f, // strength of attractive force (long-range) - B
    private val nearAttractionConstant: Float = 16.0f, // strength of attractive t-force (near) - A
    private val repulsiveConstant: Float = 2.0f, // extent and magnitude of the repulsive t-force that
    // controls the longest distance of neighbors in the layout - Y
) {
    fun <V> place(width: Double, height: Double, vertices: Collection<VertexViewModel<V>>) {
//        val forces = Array(vertices.size) { mk.ndarray(mk[0.0, 0.0]) } // TODO
        val forces = Array(vertices.size) { Pair(0f, 0f) }
        var k = 0

        vertices.onEach {
            val vi = it
            var forceX = 0f
            var forceY = 0f

            for (vj in vertices) { // repulsive forces
                if (vi == vj) continue

                val dx = vi.x.value - vj.x.value
                val dy = vi.y.value - vj.y.value

                val distance = sqrt(dx.value * dx.value + dy.value * dy.value)

                val repulsion = (distance) / (1 + distance * distance).pow(repulsiveConstant)

                forceX -= dx.value / distance * repulsion
                forceY -= dy.value / distance * repulsion

                val attraction =
                    longRangeAttractionConstant * (distance + ((nearAttractionConstant * distance) / (1 + distance * distance)))

                forceX += dx.value / distance * attraction
                forceY += dy.value / distance * attraction
            }
            forces[k] = Pair(forceX, forceY)
            println("${forceX} ${forceY}")
            println("${forces[k].first} ${forces[k].second}")
            k++
        }

        k = 0
        vertices.onEach { // update positions
            val vi = it
//            for (x in forces) println("${x.first} ${x.second}")
            vi.x.value += forces[k].first.dp
            vi.y.value += forces[k].second.dp
            k++
        }

        for (vi in vertices) { // check borders
            vi.x.value = vi.x.value.coerceIn(0.dp, (width.toFloat() / 2 - 360 - vi.radius.value * 2).dp)
            println(vi.x.value)
            vi.y.value = vi.y.value.coerceIn(0.dp, (height.toFloat() / 2 - vi.radius.value * 2).dp)
            println(vi.y.value)
            println()
        }

        println("------------")
    }
}