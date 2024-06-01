package view.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.zIndex
import maxEdgeStrokeWidth
import minEdgeStrokeWidth
import viewmodel.WindowViewModel
import viewmodel.graph.EdgeViewModel

@Composable
fun <D> EdgeView(viewModel: EdgeViewModel<D>, scale: Float) {
    val windowVM = WindowViewModel()
    windowVM.SetCurrentDimensions()

    val firstVertexCenter = viewModel.calculateFirstVertexCenter(scale)
    val secondVertexCenter = viewModel.calculateSecondVertexCenter(scale)

    val firstVertexCenterX = firstVertexCenter.first
    val firstVertexCenterY = firstVertexCenter.second
    val secondVertexCenterX = secondVertexCenter.first
    val secondVertexCenterY = secondVertexCenter.second

    val arrowPoints = viewModel.calculateArrowPoints(scale)

    val highlightColor by remember { derivedStateOf { viewModel.highlightColor } }
    val edgeColor = highlightColor.value

    Canvas(modifier = Modifier.fillMaxSize().zIndex(-1f)) {
        drawLine(
            color = edgeColor,
            strokeWidth = (5f * scale).coerceIn(minEdgeStrokeWidth, maxEdgeStrokeWidth),
            start = Offset(firstVertexCenterX.toPx(), firstVertexCenterY.toPx()),
            end = Offset(secondVertexCenterX.toPx(), secondVertexCenterY.toPx()),
        )

        if (arrowPoints.isNotEmpty()) {
            val trianglePath = Path()

            // these points represent the vertices of a triangle
            trianglePath.moveTo(
                arrowPoints[0].first.toPx(),
                arrowPoints[0].second.toPx(),
            )
            trianglePath.lineTo(arrowPoints[1].first.toPx(), arrowPoints[1].second.toPx())
            trianglePath.lineTo(arrowPoints[2].first.toPx(), arrowPoints[2].second.toPx())
            trianglePath.close()

            drawPath(path = trianglePath, color = edgeColor, style = Fill)
        }
    }
}
