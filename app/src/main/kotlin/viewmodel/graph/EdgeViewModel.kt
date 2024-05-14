package viewmodel.graph

import model.abstractGraph.Edge

class EdgeViewModel<D>(
    val firstVertex: VertexViewModel<D>,
    val secondVertex: VertexViewModel<D>,
    private val currentEdge: Edge<D>, // do we really need it?
) {}
