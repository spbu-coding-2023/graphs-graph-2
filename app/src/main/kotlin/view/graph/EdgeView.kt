package view.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import viewmodel.WindowViewModel
import viewmodel.graph.EdgeViewModel
import kotlin.math.sqrt

const val ARROW_SIZE = 20f
const val ARROW_DEPTH = 2.5f
const val SQRT_3 = 1.732f

@Composable
fun <D> EdgeView(viewModel: EdgeViewModel<D>) {

    val windowVM = WindowViewModel()
    windowVM.SetCurrentDimensions()

    val radius = viewModel.firstVertex.radius

    val firstVertexCenterX = viewModel.firstVertex.x.value + radius
    val firstVertexCenterY = viewModel.firstVertex.y.value + radius

    val secondVertexCenterX = viewModel.secondVertex.x.value + radius
    val secondVertexCenterY = viewModel.secondVertex.y.value + radius

    // find the coordinates of vector representing edge
    val vectorX = secondVertexCenterX - firstVertexCenterX
    val vectorY = secondVertexCenterY - firstVertexCenterY

    val len = sqrt(vectorX * vectorX + vectorY * vectorY)

    val normedVectorX = vectorX / len
    val normedVectorY = vectorY / len

    // rotate normed vector by Pi/6
    val aX = normedVectorX * SQRT_3/2 - normedVectorY * 1/2
    val aY = normedVectorX * 1/2 + normedVectorY * SQRT_3/2

    // rotate normed vector by negative Pi/6
    val bX = normedVectorX * SQRT_3/2 + normedVectorY * 1/2
    val bY = -normedVectorX * 1/2 + normedVectorY * SQRT_3/2

    val arrowEndPointX = secondVertexCenterX - normedVectorX * (radius.value - ARROW_DEPTH)
    val arrowEndPointY = secondVertexCenterY - normedVectorY * (radius.value - ARROW_DEPTH)

    val leftArrowPointX = arrowEndPointX - aX * ARROW_SIZE
    val leftArrowPointY = arrowEndPointY - aY * ARROW_SIZE
    val rightArrowPointX = arrowEndPointX - bX * ARROW_SIZE
    val rightArrowPointY = arrowEndPointY - bY * ARROW_SIZE

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = Color.LightGray,
            strokeWidth = 5f,
            start =
            Offset(
                firstVertexCenterX.toPx(),
                firstVertexCenterY.toPx()
            ),
            end =
            Offset(
                secondVertexCenterX.toPx(),
                secondVertexCenterY.toPx()
            ),
        )

        if (viewModel.isDirected.value) {
            val trianglePath = Path()

            // these points represent the vertices of a triangle
            trianglePath.moveTo(
                arrowEndPointX.toPx(),
                arrowEndPointY.toPx(),
            )
            trianglePath.lineTo(
                leftArrowPointX.toPx(),
                leftArrowPointY.toPx()
            )
            trianglePath.lineTo(
                rightArrowPointX.toPx(),
                rightArrowPointY.toPx()
            )
            trianglePath.close()

            drawPath(
                path = trianglePath,
                color = Color.LightGray,
                style = Fill
            )
        }
    }
}

private operator fun Dp.times(other: Dp): Float {
    return this.value * other.value
}
