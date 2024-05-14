package view.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import viewmodel.graph.EdgeViewModel

@Composable
fun <D> EdgeView(viewModel: EdgeViewModel<D>) {
    Canvas(modifier = Modifier.size(300.dp, 300.dp)) {
        drawLine(
            color = Color.Red,
            strokeWidth = 10.0f,
            start =
                Offset(
                    viewModel.firstVertex.x.value.toPx() + viewModel.firstVertex.radius.toPx(),
                    viewModel.firstVertex.y.value.toPx() + viewModel.firstVertex.radius.toPx()
                ),
            end =
                Offset(
                    viewModel.secondVertex.x.value.toPx() + viewModel.secondVertex.radius.toPx(),
                    viewModel.secondVertex.y.value.toPx() + viewModel.secondVertex.radius.toPx()
                ),
            alpha = 1.0f
        )
    }
}
