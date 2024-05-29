package viewmodel.graph

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Graph
import model.graphs.abstractGraph.Vertex

class GraphViewModel<D>(
    val currentGraph: Graph<D>,
    private val showVerticesData: State<Boolean>,
    var showVerticesID: MutableState<Boolean>,
    val graphType: MutableState<String>,
    private val isDirected: State<Boolean>
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

    fun addEdge(firstId: Int, secondId: Int, weight: Int = 1) {
        val firstVertex = graph.getVertices().find { it.id == firstId }
            ?: throw NoSuchElementException("No vertex found with id $firstId")
        val secondVertex = graph.getVertices().find { it.id == secondId }
            ?: throw NoSuchElementException("No vertex found with id $secondId")

        val firstVertexVM = _verticesViewModels[firstVertex]
            ?: throw NoSuchElementException("No ViewModel found for vertex1")
        val secondVertexVM = _verticesViewModels[secondVertex]
            ?: throw NoSuchElementException("No ViewModel found for vertex2")

        val newEdge = graph.addEdge(firstVertexVM.vertex, secondVertexVM.vertex)
        graph.getWeightMap()[newEdge] = weight
        updateEdgeViewModels(newEdge)
    }

    fun applyForceDirectedLayout(width: Double, height: Double, a: Double, b: Double, c: Double) {
        val layout = TFDPLayout()
        layout.place(width, height, verticesVM, 64, a, b, c)
    }

    fun randomize(width: Double, height: Double) {
        val layout = TFDPLayout()
        layout.randomize(width, height, verticesVM)
    }

    val verticesVM: List<VertexViewModel<D>> get() = _verticesViewModels.values.toList()
    val edgesVM: List<EdgeViewModel<D>> get() = _edgeViewModels.values.toList()

    val graph: Graph<D> get() = currentGraph
}
