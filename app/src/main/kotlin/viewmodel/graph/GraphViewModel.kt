package viewmodel.graph

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import model.graphs.*
import model.graphs.abstractGraph.*
import model.algorithms.*
import model.algorithms.clustering.CommunitiesFinder

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

    val verticesVM: List<VertexViewModel<D>> get() = _verticesViewModels.values.toList()
    val edgesVM: List<EdgeViewModel<D>> get() = _edgeViewModels.values.toList()

    val graph: Graph<D> get() = currentGraph

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
        val firstVertex = graph.getVertices()[firstId]
        val secondVertex = graph.getVertices()[secondId]

        val firstVertexVM = _verticesViewModels[firstVertex]
            ?: throw NoSuchElementException("No ViewModel found for vertex (${firstVertex.id}, ${firstVertex.data})")
        val secondVertexVM = _verticesViewModels[secondVertex]
            ?: throw NoSuchElementException("No ViewModel found for vertex (${secondVertex.id}, ${secondVertex.data})")

        val newEdge = graph.addEdge(firstVertexVM.vertex, secondVertexVM.vertex)
        graph.getWeightMap()[newEdge] = weight
        updateEdgeViewModels(newEdge)
    }

    fun applyForceDirectedLayout(width: Double, height: Double, a: Double, b: Double, c: Double) {
        val layout = TFDPLayout()
        layout.place(width, height, verticesVM, 128, a, b, c)
    }

    fun randomize(width: Double, height: Double) {
        val layout = TFDPLayout()
        layout.randomize(width, height, verticesVM)
    }

    fun findCommunities(): Boolean {
        val communitiesFinder = CommunitiesFinder()
        val communities = communitiesFinder.findCommunities(graph)

        return highlightVerticesSets(communities)
    }

    fun findKeyVertices(): Boolean {
        val keyVerticesFinder = KeyVerticesFinder()
        val keyVertices = keyVerticesFinder.findKeyVertices(graph)

        return highlightVertices(keyVertices)
    }

    fun findBridges(): Boolean {
        val bridgesFinder = BridgesFinder()
        if (graph is UndirectedGraph) {
            val bridges = bridgesFinder.findBridges(graph as UndirectedGraph)

            return highlightEdges(bridges.toSet())
        }

        return false
    }

    var cycles: List<List<Pair<Edge<D>, Vertex<D>>>>? = null
    var currentCycleIndex = 0

    fun findCycles(srcVertexId: Int): Boolean {
        val cyclesFinder = CyclesFinder()
        if (graph is DirectedGraph) {
            val foundCycles = cyclesFinder.findCycles(graph as DirectedGraph, graph.getVertices()[srcVertexId])
            cycles = foundCycles.toList()

            currentCycleIndex = 0
            if (cycles != null) return true
        }

        return false
    }

    fun findMinSpanningTree(): Boolean {
        val minSpanningTreeFinder = MinSpanningTreeFinder()
        if (graph is WeightedUndirectedGraph) {
            val minSpanningTree = minSpanningTreeFinder.findMinSpanningTree(graph as WeightedUndirectedGraph)

            return highlightEdges(minSpanningTree.toSet())
        }

        return false
    }

    fun findSCCs(): Boolean {
        val SCCFinder = SCCFinder()
        if (graph is DirectedGraph) {
            val SCCs = SCCFinder.findSCC(graph as DirectedGraph)

            return highlightVerticesSets(SCCs)
        }

        return false
    }

    fun findShortestPath(srcVertexId: Int, destVertexId: Int): Boolean {
        val shortestPathFinder = ShortestPathFinder()

        val src = graph.getVertices()[srcVertexId]
        val dest = graph.getVertices()[destVertexId]

        if (graph is WeightedDirectedGraph) {
            val shortestPath = shortestPathFinder.findShortestPath(graph as WeightedDirectedGraph, src, dest)

            return highlightPath(shortestPath)
        }
        else if (graph is WeightedUndirectedGraph) {
            val shortestPath = shortestPathFinder.findShortestPath(graph as WeightedUndirectedGraph, src, dest)
            return highlightPath(shortestPath)
        }

        return false
    }

    private fun highlightVertices(verticesSet: Set<Vertex<D>>?): Boolean {
        if (verticesSet == null) return false

        for (vertex in graph.getVertices()) {
            if (vertex in verticesSet) {
                _verticesViewModels[vertex]?.highlightColor?.value = Color.Black
            } else {
                _verticesViewModels[vertex]?.highlightColor?.value = Color.LightGray
            }
        }

        return true
    }

    private fun highlightEdges(edgesSet: Set<Edge<D>>?): Boolean {
        if (edgesSet == null) return false

        for (edge in graph.getEdges()) {
            if (edge in edgesSet) {
                _edgeViewModels[edge]?.highlightColor?.value = Color.Black
            } else {
                _edgeViewModels[edge]?.highlightColor?.value = Color.LightGray
            }
        }

        return true
    }

    private fun highlightVerticesSets(verticesSets: Set<Set<Vertex<D>>>?): Boolean {
        if (verticesSets == null) return false

        val colors = arrayOf(
            Color.Red,
            Color.Blue,
            Color.Green,
            Color.Yellow,
            Color.Cyan,
            Color.Magenta,
            Color.Black,
            Color.White,
            Color.DarkGray,
            Color(0xebab34),
            Color(0xaeeb34),
            Color(0x5e34eb),
            Color(0x8334eb),
            Color(0xd834eb),
            Color(0xeb34a1),
            )

        var i = 0
        for (verticesSet in verticesSets) {
            val currentColor = colors[i]
            for (vertex in verticesSet) {
                _verticesViewModels[vertex]?.highlightColor?.value = currentColor
            }

            i++
            if (i > colors.size - 1) throw ArrayIndexOutOfBoundsException("Only 15 colors supported")
        }

        return true
    }

    private fun highlightPath(path: List<Pair<Edge<D>, Vertex<D>>>?): Boolean {
        if (path == null) return false

        var verticesNotInPath = graph.getVertices()
        var edgesNotINPath = graph.getEdges()

        for (pair in path) {
            val edge = pair.first
            val vertex = pair.second

            _edgeViewModels[edge]?.highlightColor?.value = Color.Black
            _verticesViewModels[vertex]?.highlightColor?.value = Color.Black

            verticesNotInPath -= vertex
            edgesNotINPath -= edge
        }

        for (vertex in verticesNotInPath) {
            _verticesViewModels[vertex]?.highlightColor?.value = Color.LightGray
        }
        for (edge in edgesNotINPath) {
            _edgeViewModels[edge]?.highlightColor?.value = Color.LightGray
        }

        return true
    }

    fun highlighNextCycle(): Boolean {
        var returnValue = false
        returnValue = highlightPath(cycles?.get(currentCycleIndex))

        val size = cycles?.size
            ?: throw NullPointerException("cycles property isn't initialised")

        if (++currentCycleIndex > size - 1) currentCycleIndex = 0

        return returnValue
    }
}
