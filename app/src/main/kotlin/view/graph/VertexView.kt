package view.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import viewmodel.WindowViewModel
import viewmodel.graph.VertexViewModel
import kotlin.math.min
import kotlin.ranges.coerceIn

@Composable
fun <D> VertexView(viewModel: VertexViewModel<D>, scale: Float) {
    val coroutineScope = rememberCoroutineScope { Dispatchers.Default }
    val windowVM = WindowViewModel()
    windowVM.SetCurrentDimensions()
    val density = LocalDensity.current.density

    val maxRadius = 35.dp // TODO: move to shared const file
    val minRadius = 7.dp

    val adjustedX = (viewModel.x.value)
    val adjustedY = (viewModel.y.value)
    val adjustedRadius = (viewModel.radius * scale).coerceIn(minRadius, maxRadius)

    Box(
        modifier = Modifier
            .offset { IntOffset(adjustedX.roundToPx(), adjustedY.roundToPx()) }
            .size(adjustedRadius * 2)
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
