package view.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import viewmodel.WindowViewModel
import viewmodel.graph.EdgeViewModel

@Composable
fun <D> EdgeView(viewModel: EdgeViewModel<D>) {

    val windowVM = WindowViewModel()
    windowVM.SetCurrentDimensions()

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = Color.LightGray,
            strokeWidth = 5.0f,
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
        )
    }
}

