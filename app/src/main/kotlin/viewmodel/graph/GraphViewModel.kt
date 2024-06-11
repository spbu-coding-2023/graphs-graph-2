package viewmodel.graph

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import model.algorithms.*
import model.algorithms.clustering.CommunitiesFinder
import model.graphs.DirectedGraph
import model.graphs.UndirectedGraph
import model.graphs.WeightedDirectedGraph
import model.graphs.WeightedUndirectedGraph
import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Graph
import model.graphs.abstractGraph.Vertex


class GraphViewModel<D>(
    private val currentGraph: Graph<D>,
    private val showVerticesData: State<Boolean>,
    var showVerticesID: MutableState<Boolean>,
    val graphType: MutableState<String>,
    val isDirected: MutableState<Boolean>,
    val isWeighted: MutableState<Boolean>
) {

    val updateIsRequired = mutableStateOf(false)

    private var _verticesViewModels = mutableMapOf<Vertex<D>, VertexViewModel<D>>()
    var _edgeViewModels = mutableMapOf<Edge<D>, EdgeViewModel<D>>()

    val verticesVM: List<VertexViewModel<D>> get() = _verticesViewModels.values.toList()
    val edgesVM: List<EdgeViewModel<D>> get() = _edgeViewModels.values.toList()

    val graph: Graph<D> get() = currentGraph

    private fun updateEdgeViewModels(edge: Edge<D>) {
        val firstVertex: VertexViewModel<D> =
            _verticesViewModels[edge.vertex1]
                ?: throw NoSuchElementException("No such View Model, with mentioned edges")

        val secondVertex: VertexViewModel<D> =
            _verticesViewModels[edge.vertex2]
                ?: throw NoSuchElementException("No such View Model, with mentioned edges")

        _edgeViewModels[edge] = EdgeViewModel(firstVertex, secondVertex, isDirected)
    }

    private fun updateVertexViewModels(vertex: Vertex<D>) {
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
        if (communities.isEmpty()) return false

        return highlightVerticesSets(communities)
    }

    fun findKeyVertices(): Boolean {
        val keyVerticesFinder = KeyVerticesFinder()
        val keyVertices = keyVerticesFinder.findKeyVertices(graph)
        if (keyVertices?.isEmpty() == true) return false

        return highlightVertices(keyVertices)
    }

    fun findBridges(): Boolean {
        val bridgesFinder = BridgesFinder()
        if (graph is UndirectedGraph) {
            val bridges = bridgesFinder.findBridges(graph as UndirectedGraph)
            if (bridges.isEmpty()) return false

            return highlightEdges(bridges.toSet())
        }

        return false
    }

    private var cycles: List<List<Pair<Edge<D>, Vertex<D>>>>? = null
    private var currentCycleIndex = 0

    fun findCycles(srcVertexId: Int): Boolean {
        val cyclesFinder = CyclesFinder()
        if (graph is DirectedGraph) {
            val foundCycles = cyclesFinder.findCycles(graph as DirectedGraph, graph.getVertices()[srcVertexId])
            if (foundCycles.isEmpty()) return false

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
            if (minSpanningTree.isEmpty()) return false

            return highlightEdges(minSpanningTree.toSet())
        }

        return false
    }

    fun findSCCs(): Boolean {
        val sccFinder = SCCFinder()
        if (graph is DirectedGraph) {
            val SCCs = sccFinder.findSCC(graph as DirectedGraph)
            if (SCCs.isEmpty()) return false

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
            if (shortestPath?.isEmpty() == true) return false

            return highlightPath(shortestPath)
        }
        else if (graph is WeightedUndirectedGraph) {
            val shortestPath = shortestPathFinder.findShortestPath(graph as WeightedUndirectedGraph, src, dest)
            if (shortestPath?.isEmpty() == true) return false

            return highlightPath(shortestPath)
        }

        return false
    }

    private fun highlightVertices(verticesSet: Set<Vertex<D>>?): Boolean {
        if (verticesSet == null) return false

        clearGraph()

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

        clearGraph()

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

        clearGraph()

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

        clearGraph()

        val srcVertex = path.first().first.vertex1
        _verticesViewModels[srcVertex]?.highlightColor?.value = Color.Black

        for (pair in path) {
            val edge = pair.first
            val vertex = pair.second

            _edgeViewModels[edge]?.highlightColor?.value = Color.Black
            _verticesViewModels[vertex]?.highlightColor?.value = Color.Black
        }

        return true
    }

    fun highlighNextCycle(): Boolean {
        val returnValue: Boolean = highlightPath(cycles?.get(currentCycleIndex))

        val size = cycles?.size
            ?: return false

        if (++currentCycleIndex > size - 1) currentCycleIndex = 0

        return returnValue
    }

    fun clearGraph() {
        for (vertex in graph.getVertices()) {
            _verticesViewModels[vertex]?.highlightColor?.value = Color.LightGray
        }
        for (edge in graph.getEdges()) {
            _edgeViewModels[edge]?.highlightColor?.value = Color.LightGray
        }
    }

    fun getAvailableAlgorithms(): List<Algorithm> {
        val algorithms = mutableListOf(
            Algorithm.LAYOUT,
            Algorithm.FIND_COMMUNITIES,
            Algorithm.FIND_KEY_VERTICES
        )

        if (graph is DirectedGraph) {
            algorithms += Algorithm.FIND_SCCS
            algorithms += Algorithm.FIND_CYCLES
        }

        if (graph is UndirectedGraph) {
            algorithms += Algorithm.FIND_BRIDGES
        }

        if (graph is WeightedUndirectedGraph) {
            algorithms += Algorithm.MIN_SPANNING_TREE
            algorithms += Algorithm.FIND_SHORTEST_PATH
        }

        if (graph is WeightedDirectedGraph) {
            algorithms += Algorithm.FIND_SHORTEST_PATH
        }

        return algorithms
    }
}

enum class Algorithm {
    LAYOUT,
    FIND_COMMUNITIES,
    FIND_KEY_VERTICES,
    FIND_SCCS,
    FIND_CYCLES,
    FIND_BRIDGES,
    MIN_SPANNING_TREE,
    FIND_SHORTEST_PATH
}

fun getAlgorithmDisplayName(algorithm: Algorithm): String {
    return when (algorithm) {
        Algorithm.LAYOUT -> "Layout"
        Algorithm.FIND_COMMUNITIES -> "Find communities"
        Algorithm.FIND_KEY_VERTICES -> "Find key vertices"
        Algorithm.FIND_SCCS -> "Find SCCs"
        Algorithm.FIND_CYCLES -> "Find cycles"
        Algorithm.FIND_BRIDGES -> "Find bridges"
        Algorithm.MIN_SPANNING_TREE -> "Min spanning tree"
        Algorithm.FIND_SHORTEST_PATH -> "Find shortest path"
    }
}
