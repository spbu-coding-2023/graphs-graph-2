package viewmodel.graph

import androidx.compose.runtime.State
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import model.abstractGraph.Vertex


class VertexViewModel<D>(
    var x: Dp = 0.dp,
    var y: Dp = 0.dp,
    var dataVisible: State<Boolean>,
    var idVisble: State<Boolean>,
    private val vertex: Vertex<D>,
    val radius: Dp = 30.dp,
) {
    var isSelected = false

    val getVertexData
        get() = vertex.data.toString()

    fun onDrag(dragAmount: DpOffset) {
        x += dragAmount.x
        y += dragAmount.y
    }
}