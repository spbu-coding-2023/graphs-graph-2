package viewmodel.graph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import model.abstractGraph.Vertex
import viewmodel.WindowViewModel

class VertexViewModel<D>(
    var x: MutableState<Dp> = mutableStateOf(0.dp),
    var y: MutableState<Dp> = mutableStateOf(0.dp),
    var dataVisible: State<Boolean>,
    var idVisible: State<Boolean>,
    val vertex: Vertex<D>,
    val radius: Dp = 20.dp,
) {
    var isSelected = mutableStateOf(false)

    val getVertexData
        get() = vertex.data.toString()

    fun onDrag(dragAmount: DpOffset, currentWindowVM: WindowViewModel, density: Float, scale: Float) {

        val maxX = currentWindowVM.getWidth - 360.dp - radius * 2
        val maxY = currentWindowVM.getHeight - radius * 2

        // calculate the new position after dragging
        val newX = (x.value + dragAmount.x).coerceIn(0.dp, maxX)
        val newY = (y.value + dragAmount.y).coerceIn(0.dp, maxY)

        // update the position
        x.value = newX
        y.value = newY
    }

    val getVertexID
        get() = vertex.id
}

