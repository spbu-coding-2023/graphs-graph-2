package view.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.panBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import viewmodel.graph.GraphViewModel

@Composable
fun <D> GraphView(viewModel: GraphViewModel<D>, currentScaleState: MutableState<Float>) {
    var offset by remember { mutableStateOf(Offset.Zero) }
    val coroutineScope = rememberCoroutineScope { Dispatchers.Default }

    val updateRequired by remember { derivedStateOf { viewModel.updateIsRequired } }

    val transformationState = rememberTransformableState { zoomChange, offsetChange, _ ->
        currentScaleState.value *= zoomChange
        offset += offsetChange
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    coroutineScope.launch {
                        // update the transformation state using the gesture values
                        transformationState.panBy(pan)
                    }
                }
            }
            .graphicsLayer(
                scaleX = currentScaleState.value,
                scaleY = currentScaleState.value,
                translationX = offset.x,
                translationY = offset.y
            )
    ) {
        if (updateRequired.value) {
            viewModel.randomize(740.0, 650.0)
            viewModel.applyForceDirectedLayout(740.0, 650.0, 0.1, 8.0, 1.2)
        }
        viewModel.verticesVM.forEach { v -> VertexView(v, currentScaleState.value) }
        viewModel.edgesVM.forEach { e -> EdgeView(e, currentScaleState.value) }

        viewModel.updateIsRequired.value = false
    }
}
