package viewmodel.graph

import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp
import kotlin.math.sqrt

const val ARROW_SIZE = 20f
const val ARROW_DEPTH = 2.5f
const val SQRT_3 = 1.732f

class EdgeViewModel<D>(
    private val firstVertex: VertexViewModel<D>,
    private val secondVertex: VertexViewModel<D>,
    private val isDirected: State<Boolean>
) {

    private val radius = firstVertex.radius

    internal fun calculateFirstVertexCenter(): Pair<Dp, Dp> {
        val x = firstVertex.x.value + radius
        val y = firstVertex.y.value + radius

        return Pair(x, y)
    }

    internal fun calculateSecondVertexCenter(): Pair<Dp, Dp> {
        val x = secondVertex.x.value + radius
        val y = secondVertex.y.value + radius

        return Pair(x, y)
    }

    internal fun calculateArrowPoints(): List<Pair<Dp, Dp>> {
        if (!isDirected.value) return listOf()

        val firstVertexCenterX = calculateFirstVertexCenter().first
        val firstVertexCenterY = calculateFirstVertexCenter().second

        val secondVertexCenterX = calculateSecondVertexCenter().first
        val secondVertexCenterY = calculateSecondVertexCenter().second

        val vectorX = secondVertexCenterX - firstVertexCenterX
        val vectorY = secondVertexCenterY - firstVertexCenterY

        val len = sqrt(vectorX.value * vectorX.value + vectorY.value * vectorY.value)
        val normedVectorX = vectorX / len
        val normedVectorY = vectorY / len

        // rotate normed vector by Pi/6
        val aX = normedVectorX * SQRT_3 / 2 - normedVectorY * 1 / 2
        val aY = normedVectorX * 1 / 2 + normedVectorY * SQRT_3 / 2

        // rotate normed vector by negative Pi/6
        val bX = normedVectorX * SQRT_3 / 2 + normedVectorY * 1 / 2
        val bY = -normedVectorX * 1 / 2 + normedVectorY * SQRT_3 / 2

        val arrowEndPointX = secondVertexCenterX - normedVectorX * (radius.value - ARROW_DEPTH)
        val arrowEndPointY = secondVertexCenterY - normedVectorY * (radius.value - ARROW_DEPTH)

        val arrowLeftPointX = arrowEndPointX - aX * ARROW_SIZE
        val arrowLeftPointY = arrowEndPointY - aY * ARROW_SIZE

        val arrowRightPointX = arrowEndPointX - bX * ARROW_SIZE
        val arrowRightPointY = arrowEndPointY - bY * ARROW_SIZE

        return listOf(
            Pair(arrowEndPointX, arrowEndPointY),
            Pair(arrowLeftPointX, arrowLeftPointY),
            Pair(arrowRightPointX, arrowRightPointY)
        )
    }
}
