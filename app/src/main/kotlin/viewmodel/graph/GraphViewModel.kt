package viewmodel.graph

import androidx.compose.runtime.*
import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex

class GraphViewModel<D>(
    val graph: Graph<D>,
    private val showVerticesData: State<Boolean>,
    var showVerticesID: MutableState<Boolean>,
    val graphType: MutableState<String>,
    private val isDirected: State<Boolean>,
) {

    val updateIsRequired = mutableStateOf(false)

    var _verticesViewModels = mutableMapOf<Vertex<D>, VertexViewModel<D>>()
    var _edgeViewModels = mutableMapOf<Edge<D>, EdgeViewModel<D>>()

    fun updateEdgeViewModels(edge: Edge<D>) {
        val firstVertex: VertexViewModel<D> =
            _verticesViewModels[edge.vertex1]
                ?: throw NoSuchElementException("No such View Model, with mentioned edges")

        val secondVertex: VertexViewModel<D> =
            _verticesViewModels[edge.vertex2]
                ?: throw NoSuchElementException("No such View Model, with mentioned edges")

        _edgeViewModels[edge] = EdgeViewModel(firstVertex, secondVertex, isDirected)
    }

    fun updateVertexViewModels(vertex: Vertex<D>) {
        _verticesViewModels[vertex] = VertexViewModel(
            dataVisible = showVerticesData,
            idVisible = showVerticesID,
            vertex = vertex,
            )
    }

    fun checkVertexById(id: Int): Boolean {
        return _verticesViewModels.keys.any { it.id == id }
    }

    fun addVertex(data: String): Int {
        val newVertex = graph.addVertex(data as D)

        updateVertexViewModels(newVertex)

        return newVertex.id
    }

    fun addEdge(firstId: Int, secondId: Int) {
        val firstVertex = graph.getVertices().find { it.id == firstId }
            ?: throw NoSuchElementException("No vertex found with id $firstId")
        val secondVertex = graph.getVertices().find { it.id == secondId }
            ?: throw NoSuchElementException("No vertex found with id $secondId")

        val firstVertexVM = _verticesViewModels[firstVertex]
            ?: throw NoSuchElementException("No ViewModel found for vertex1")
        val secondVertexVM = _verticesViewModels[secondVertex]
            ?: throw NoSuchElementException("No ViewModel found for vertex2")

        val newEdge = graph.addEdge(firstVertexVM.vertex, secondVertexVM.vertex)

        updateEdgeViewModels(newEdge)
    }

    fun applyForceDirectedLayout(width: Double, height: Double) {
        val layout = TFDPLayout()
        layout.place(width, height, verticesVM)
    }

    val verticesVM: List<VertexViewModel<D>> get() = _verticesViewModels.values.toList()
    val edgesVM: List<EdgeViewModel<D>> get() = _edgeViewModels.values.toList()
}
