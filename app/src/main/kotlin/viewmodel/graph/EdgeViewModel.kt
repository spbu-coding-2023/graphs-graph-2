package viewmodel.graph

import androidx.compose.runtime.State
import model.abstractGraph.Edge
import model.abstractGraph.Vertex

class EdgeViewModel<D>(
    val firstVertex: VertexViewModel<D>,
    val secondVertex: VertexViewModel<D>,
    private val currentEdge: Edge<D>, // do we really need it?
) {
}