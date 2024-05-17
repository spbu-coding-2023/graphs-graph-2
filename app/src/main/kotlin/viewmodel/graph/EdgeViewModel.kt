package viewmodel.graph

import model.abstractGraph.Edge
import androidx.compose.runtime.State

class EdgeViewModel<D>(
    val firstVertex: VertexViewModel<D>,
    val secondVertex: VertexViewModel<D>,
    var isDirected: State<Boolean>,
    private val currentEdge: Edge<D> // do we really need it?
) {

}