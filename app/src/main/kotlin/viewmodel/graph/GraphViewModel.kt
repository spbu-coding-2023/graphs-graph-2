package viewmodel.graph

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import model.abstractGraph.Edge
import model.abstractGraph.Graph

class GraphViewModel<D>(
    private val graph: Graph<D>,
    private val showIds: State<Boolean>,
    private val showVerticesData: State<Boolean>,
    val graphType: MutableState<String>,
) {

    private var _verticesViewModels =
        graph.getVertices().associateWith { vertex ->
            VertexViewModel(
                dataVisible = showVerticesData,
                idVisible = showIds,
                vertex = vertex,
            )
        }.toMutableMap()

    private var _edgeViewModels: Map<Edge<D>, EdgeViewModel<D>> =
        graph.getEdges().associateWith { edge ->
            val firstVertex: VertexViewModel<D> =
                _verticesViewModels[edge.vertex1]
                    ?: throw NoSuchElementException("No such View Model, with mentioned edges")

            val secondVertex: VertexViewModel<D> =
                _verticesViewModels[edge.vertex2]
                    ?: throw NoSuchElementException("No such View Model, with mentioned edges")

            EdgeViewModel(firstVertex, secondVertex, edge)
        }.toMutableMap()


    fun checkVertexById(id: Int): Boolean {
        return _verticesViewModels.keys.any { it.id == id }
    }

    fun addVertex(data: String): Int {
        val newVertex = graph.addVertex(data as D)
        _verticesViewModels[newVertex] =
            VertexViewModel(
            dataVisible = showVerticesData,
            idVisible = showIds,
            vertex = newVertex,
        )
        TestRepresentation().place(740.0, 650.0, verticesVM)
        return newVertex.id
    }

    fun addEdge(firstId: Int, secondId: Int) {
        val firstVertex =graph.getVertices().find { it.id == firstId }
            ?: throw NoSuchElementException("No vertex found with id $firstId")
        val secondVertex = graph.getVertices().find { it.id == secondId }
            ?: throw NoSuchElementException("No vertex found with id $secondId")

        val firstVertexVM = _verticesViewModels[firstVertex]
            ?: throw NoSuchElementException("No ViewModel found for vertex1")
        val secondVertexVM = _verticesViewModels[secondVertex]
            ?: throw NoSuchElementException("No ViewModel found for vertex2")
        val edge = graph.addEdge(firstVertexVM.vertex, secondVertexVM.vertex)

        _edgeViewModels = _edgeViewModels.toMutableMap().apply {
            this[edge] = EdgeViewModel(firstVertexVM, secondVertexVM, edge)
        }
        TestRepresentation().place(740.0, 650.0, verticesVM)
    }

    val verticesVM: Collection<VertexViewModel<D>>
        get() = _verticesViewModels.values

    val edgesVM: Collection<EdgeViewModel<D>>
        get() = _edgeViewModels.values
}
