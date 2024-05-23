package view.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.zIndex
import viewmodel.WindowViewModel
import viewmodel.graph.EdgeViewModel
import kotlin.math.max
import kotlin.math.min

@Composable
fun <D> EdgeView(viewModel: EdgeViewModel<D>, scale: Float) {
    val windowVM = WindowViewModel()
    windowVM.SetCurrentDimensions()

    val maxStrokeWidth = 12f// TODO: move to shared const file
    val minStrokeWidth = 4f

    val firstVertexCenter = viewModel.calculateFirstVertexCenter(scale)
    val secondVertexCenter = viewModel.calculateSecondVertexCenter(scale)

    val firstVertexCenterX = firstVertexCenter.first
    val firstVertexCenterY = firstVertexCenter.second
    val secondVertexCenterX = secondVertexCenter.first
    val secondVertexCenterY = secondVertexCenter.second

    val arrowPoints = viewModel.calculateArrowPoints(scale)

    Canvas(modifier = Modifier.fillMaxSize().zIndex(-1f)) {
        drawLine(
            color = Color.LightGray,
            strokeWidth = (5f * scale).coerceIn(minStrokeWidth, maxStrokeWidth),
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

        if (arrowPoints.isNotEmpty()) {
            val trianglePath = Path()

            // these points represent the vertices of a triangle
            trianglePath.moveTo(
                arrowPoints[0].first.toPx(),
                arrowPoints[0].second.toPx(),
            )
            trianglePath.lineTo(
                arrowPoints[1].first.toPx(),
                arrowPoints[1].second.toPx()
            )
            trianglePath.lineTo(
                arrowPoints[2].first.toPx(),
                arrowPoints[2].second.toPx()
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
