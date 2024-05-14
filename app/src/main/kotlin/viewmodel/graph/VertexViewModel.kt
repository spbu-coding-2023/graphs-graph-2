package viewmodel.graph

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
    private val vertex: Vertex<D>,
    val CurrentWindowVM: WindowViewModel,
    val radius: Dp = 30.dp,
) {
    var isSelected = mutableStateOf(false)

    val getVertexData
        get() = vertex.data.toString()

    fun onDrag(dragAmount: DpOffset) {

        if (x.value + dragAmount.x > CurrentWindowVM.getWidth)
            x.value = CurrentWindowVM.getWidth - radius
        else x.value += dragAmount.x

        if (y.value + dragAmount.y > CurrentWindowVM.getWidth)
            y.value = CurrentWindowVM.getWidth - radius
        else y.value += dragAmount.y
    }
}
