package viewmodel.graph

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import model.abstractGraph.Graph

class GraphViewModel<D>(
    private val graph: Graph<D>,
    private val isDirected: State<Boolean>,
    showIds: State<Boolean>,
    showVerticesData: State<Boolean>,
) {
    private val _verticesViewModels =
        graph.getVertices().associateWith { vertex ->
            VertexViewModel(
                dataVisible = showVerticesData,
                idVisible = showIds,
                vertex = vertex,
            )
        }

    private val _edgeViewModels =
        graph.getEdges().associateWith { edge ->
            val firstVertex: VertexViewModel<D> =
                _verticesViewModels[edge.vertex1]
                    ?: throw NoSuchElementException("No such View Model, with mentioned edges")

            val secondVertex: VertexViewModel<D> =
                _verticesViewModels[edge.vertex2]
                    ?: throw NoSuchElementException("No such View Model, with mentioned edges")

            EdgeViewModel(firstVertex, secondVertex, isDirected)
        }

    val verticesVM: Collection<VertexViewModel<D>>
        get() = _verticesViewModels.values

    val edgesVM: Collection<EdgeViewModel<D>>
        get() = _edgeViewModels.values
}
