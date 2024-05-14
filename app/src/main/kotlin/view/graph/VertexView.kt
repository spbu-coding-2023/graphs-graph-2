package view.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import viewmodel.graph.VertexViewModel

@Composable
fun <D> VertexView(viewModel: VertexViewModel<D>) {
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier =
            Modifier.offset {
                    IntOffset(viewModel.x.value.roundToPx(), viewModel.y.value.roundToPx())
                }
                .size(viewModel.radius * 2)
                .background(
                    if (viewModel.isSelected.value) Color.Yellow else Color.LightGray,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .pointerInput(Unit) {
                    coroutineScope.launch {
                        detectDragGestures { change, dragAmount ->
                            viewModel.onDrag(DpOffset(dragAmount.x.toDp(), dragAmount.y.toDp()))
                            change.consume()
                        }
                        detectTapGestures(
                            onTap = { viewModel.isSelected.value = !viewModel.isSelected.value }
                        )
                    }
                },
        contentAlignment = Alignment.Center
    ) {
        if (viewModel.dataVisible.value) {
            Text(modifier = Modifier.align(Alignment.Center), text = viewModel.getVertexData)
        }
    }
}
