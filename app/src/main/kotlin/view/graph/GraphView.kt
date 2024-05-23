package view.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.panBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.zoomBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import viewmodel.graph.GraphViewModel

@Composable
fun <D> GraphView(viewModel: GraphViewModel<D>) {
    val scale = remember { mutableStateOf(1f) }
    val offset = remember { mutableStateOf(Offset.Zero) }
    val coroutineScope = rememberCoroutineScope { Dispatchers.Default }

    val updateRequired = remember { derivedStateOf { viewModel.updateIsRequired } }

    // val density = LocalDensity.current.density

    val transformationState = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale.value *= zoomChange
        offset.value += offsetChange
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    coroutineScope.launch {
                        // update the transformation state using the gesture values
                        transformationState.zoomBy(zoom)
                        transformationState.panBy(pan)
                    }
                }
            }
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                translationX = offset.value.x,
                translationY = offset.value.y
            )
    ) {
        if (updateRequired.value.value) {
            viewModel.applyForceDirectedLayout(740.0, 650.0)
        }
        viewModel.verticesVM.forEach { v -> VertexView(v, scale.value, offset.value) }
        viewModel.edgesVM.forEach { e -> EdgeView(e, scale.value, offset.value) }

        viewModel.updateIsRequired.value = false
    }
}