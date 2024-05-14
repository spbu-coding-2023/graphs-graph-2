package view.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import viewmodel.graph.GraphViewModel

@Composable
fun <D> GraphView(viewModel: GraphViewModel<D>) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        viewModel.verticesVM.forEach { v -> VertexView(v) }
        viewModel.edgesVM.forEach { e -> EdgeView(e) }
    }
}
