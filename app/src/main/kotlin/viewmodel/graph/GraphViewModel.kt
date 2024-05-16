package viewmodel.graph

import androidx.compose.runtime.State
import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex

class GraphViewModel<D>(
    private val graph: Graph<D>,
    private val showIds: State<Boolean>,
    private val showVerticesData: State<Boolean>,
) {

    private var _verticesViewModels =
        graph.getVertices().associateWith { vertex ->
            VertexViewModel(
                dataVisible = showVerticesData,
                idVisible = showIds,
                vertex = vertex,
            )
        }

    private var _edgeViewModels: Map<Edge<D>, EdgeViewModel<D>> =
        graph.getEdges().associateWith { edge ->
            val firstVertex: VertexViewModel<D> =
                _verticesViewModels[edge.vertex1]
                    ?: throw NoSuchElementException("No such View Model, with mentioned edges")

            val secondVertex: VertexViewModel<D> =
                _verticesViewModels[edge.vertex2]
                    ?: throw NoSuchElementException("No such View Model, with mentioned edges")

            EdgeViewModel(firstVertex, secondVertex, edge)
        }


    fun checkVertexById(id: Int): Boolean {
        return _verticesViewModels.keys.any { it.id == id }
    }

    fun addVertex(data: String): Int {
        val newVertex = graph.addVertex(data)
//        _verticesViewModels[newVertex] =
//            VertexViewModel(
//            dataVisible = showVerticesData,
//            idVisible = showIds,
//            vertex = newVertex,
//        )
        return newVertex
    }

    fun addEdge(firstId: Int, secondId: Int) {
        val firstVertexVM = _verticesViewModels[firstId]
            ?: throw NoSuchElementException("No ViewModel found for vertex1")
        val secondVertexVM = _verticesViewModels[secondId]
            ?: throw NoSuchElementException("No ViewModel found for vertex2")
        _edgeViewModels[edge] = EdgeViewModel(firstVertexVM, secondVertexVM, edge)
    }

    val verticesVM: Collection<VertexViewModel<D>>
        get() = _verticesViewModels.values

    val edgesVM: Collection<EdgeViewModel<D>>
        get() = _edgeViewModels.values
}
