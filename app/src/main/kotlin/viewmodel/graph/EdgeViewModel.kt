package viewmodel.graph

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import kotlin.math.sqrt

const val ARROW_SIZE = 20f
const val ARROW_DEPTH = 2.5f
const val SQRT_3 = 1.732f

class EdgeViewModel<D>(
    private val firstVertex: VertexViewModel<D>,
    private val secondVertex: VertexViewModel<D>,
    private val isDirected: Boolean
) {

    fun isDirected() = isDirected

    private val vertexRadius: Dp
        get() = firstVertex.radius

    var highlightColor = mutableStateOf(Color.LightGray)

    internal fun calculateFirstVertexCenter(scale: Float): Pair<Dp, Dp> {
        val x = firstVertex.x.value + vertexRadius * scale
        val y = firstVertex.y.value + vertexRadius * scale

        return Pair(x, y)
    }

    internal fun calculateSecondVertexCenter(scale: Float): Pair<Dp, Dp> {
        val x = secondVertex.x.value + vertexRadius * scale
        val y = secondVertex.y.value + vertexRadius * scale

        return Pair(x, y)
    }

    internal fun calculateArrowPoints(scale: Float): List<Pair<Dp, Dp>> {
        if (!isDirected) return listOf()

        val firstVertexCenterX = calculateFirstVertexCenter(scale).first
        val firstVertexCenterY = calculateFirstVertexCenter(scale).second

        val secondVertexCenterX = calculateSecondVertexCenter(scale).first
        val secondVertexCenterY = calculateSecondVertexCenter(scale).second

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

        val arrowEndPointX = secondVertexCenterX - normedVectorX * (vertexRadius.value - ARROW_DEPTH) * scale
        val arrowEndPointY = secondVertexCenterY - normedVectorY * (vertexRadius.value - ARROW_DEPTH) * scale

        val arrowLeftPointX = arrowEndPointX - aX * ARROW_SIZE * scale
        val arrowLeftPointY = arrowEndPointY - aY * ARROW_SIZE * scale

        val arrowRightPointX = arrowEndPointX - bX * ARROW_SIZE * scale
        val arrowRightPointY = arrowEndPointY - bY * ARROW_SIZE * scale

        return listOf(
            Pair(arrowEndPointX, arrowEndPointY),
            Pair(arrowLeftPointX, arrowLeftPointY),
            Pair(arrowRightPointX, arrowRightPointY)
        )
    }
}
