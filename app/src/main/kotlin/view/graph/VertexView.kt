package view.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import viewmodel.WindowViewModel
import viewmodel.graph.VertexViewModel

val MAX_VERTEX_RADIUS = 35.dp
val MIN_VERTEX_RADIUS = 7.dp

@Composable
fun <D> VertexView(viewModel: VertexViewModel<D>, scale: Float) {
    val coroutineScope = rememberCoroutineScope { Dispatchers.Default }
    val windowVM = WindowViewModel()
    windowVM.SetCurrentDimensions()

    val adjustedX = viewModel.x.value
    val adjustedY = viewModel.y.value
    val adjustedRadius = (viewModel.radius * scale).coerceIn(MIN_VERTEX_RADIUS, MAX_VERTEX_RADIUS)

    val highlightColor by remember { derivedStateOf { viewModel.highlightColor } }
    val borderColor = highlightColor.value

    Box(
        modifier = Modifier
            .offset { IntOffset(adjustedX.roundToPx(), adjustedY.roundToPx()) }
            .size(adjustedRadius * 2)
            .border(5.dp, borderColor, CircleShape)
            .background(
                if (viewModel.isSelected.value) Color.Yellow else Color.LightGray,
                shape = CircleShape
            )
            .clip(CircleShape)
            .pointerInput(Unit) {
                coroutineScope.launch {
                    detectDragGestures { change, dragAmount ->
                        viewModel.onDrag(
                            DpOffset(dragAmount.x.toDp(), dragAmount.y.toDp())
                        )
                        change.consume()
                    }
                }
                detectTapGestures(
                    onTap = { viewModel.isSelected.value = !viewModel.isSelected.value }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (viewModel.dataVisible.value) {
            Text(modifier = Modifier.align(Alignment.Center), text = viewModel.getVertexData)
        }
        if (viewModel.idVisible.value) {
            Text(
                modifier = Modifier.align(Alignment.Center).zIndex(3f),
                text = viewModel.getVertexID.toString(),
                color = Color.Black,
                style = MaterialTheme.typography.body1.copy(fontSize = 20.sp)
            )
        }
    }
}
