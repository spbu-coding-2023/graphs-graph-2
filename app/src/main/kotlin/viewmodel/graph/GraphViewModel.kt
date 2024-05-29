package viewmodel.graph

import androidx.compose.runtime.*
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

    var verticesVMsSets: Set<Set<VertexViewModel<D>>>? = setOf()
    var pathsVMs: Set<List<Pair<EdgeViewModel<D>, VertexViewModel<D>>>>? = setOf()
    var edgesVMsSet: Set<EdgeViewModel<D>>? = setOf()

    var highlightPath = mutableStateOf(pathsVMs)
    var highlightVertices = mutableStateOf(verticesVMsSets)
    var highlightEdges = mutableStateOf(edgesVMsSet)

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

    fun findCommunities() {
        val communitiesFinder = CommunitiesFinder()
        val communities = communitiesFinder.findCommunities(graph)
        verticesVMsSets = getVMs(communities)
    }

    fun findKeyVertices() {
        val keyVerticesFinder = KeyVerticesFinder()
        val keyVertices = keyVerticesFinder.findKeyVertices(graph)
        verticesVMsSets = if (keyVertices != null) getVMs(setOf(keyVertices)) else null
    }

    fun findBridges() {
        val bridgesFinder = BridgesFinder()
        if (graph is UndirectedGraph) {
            val bridges = bridgesFinder.findBridges(graph as UndirectedGraph)
            edgesVMsSet = getVMs(bridges.toSet())
        }
    }

    fun findCycles(srcVertexId: Int) {
        val cyclesFinder = CyclesFinder()
        if (graph is DirectedGraph) {
            val cycles = cyclesFinder.findCycles(graph as DirectedGraph, graph.getVertices()[srcVertexId])
            pathsVMs = getVMs(cycles)
        }
    }

    fun findMinSpanningTree() {
        val minSpanningTreeFinder = MinSpanningTreeFinder()
        if (graph is WeightedUndirectedGraph) {
            val minSpanningTree = minSpanningTreeFinder.findMinSpanningTree(graph as WeightedUndirectedGraph)
            edgesVMsSet = getVMs(minSpanningTree.toSet())
        }
    }

    fun findSCCs() {
        val SCCFinder = SCCFinder()
        if (graph is DirectedGraph) {
            val SCCs = SCCFinder.findSCC(graph as DirectedGraph)
            verticesVMsSets = getVMs(SCCs)
        }
    }

    fun findShortestPath(srcVertexId: Int, destVertexId: Int) {
        val shortestPathFinder = ShortestPathFinder()

        val src = graph.getVertices()[srcVertexId]
        val dest = graph.getVertices()[destVertexId]

        if (graph is WeightedDirectedGraph) {
            val shortestPath = shortestPathFinder.findShortestPath(graph as WeightedDirectedGraph, src, dest)
            pathsVMs = if (shortestPath != null) getVMs(setOf(shortestPath)) else null
        }
        else if (graph is WeightedUndirectedGraph) {
            val shortestPath = shortestPathFinder.findShortestPath(graph as WeightedUndirectedGraph, src, dest)
            pathsVMs = if (shortestPath != null) getVMs(setOf(shortestPath)) else null
        }
    }

    private fun getVMs(verticesSets: Set<Set<Vertex<D>>>?): Set<Set<VertexViewModel<D>>>? {
        if (verticesSets == null) return null

        val verticesVMsSets = mutableSetOf<Set<VertexViewModel<D>>>()
        for (verticesSet in verticesSets) {
            val verticesVMsSet = mutableSetOf<VertexViewModel<D>>()
            for (vertex in verticesSet) {
                verticesVMsSet += _verticesViewModels[vertex]
                    ?: throw NoSuchElementException("No ViewModel found for vertex (${vertex.id}, ${vertex.data})")
            }

            verticesVMsSets += verticesVMsSets
        }

        return verticesVMsSets
    }

    private fun getVMs(paths: Set<List<Pair<Edge<D>, Vertex<D>>>?>?):
            Set<List<Pair<EdgeViewModel<D>, VertexViewModel<D>>>>? {
        if (paths == null) return null

        val pathsVMs = mutableSetOf<List<Pair<EdgeViewModel<D>, VertexViewModel<D>>>>()
        for (path in paths) {
            if (path == null) return null
            val pathVM = mutableListOf<Pair<EdgeViewModel<D>, VertexViewModel<D>>>()
            for (pair in path) {
                val edge = pair.first
                val vertex = pair.second

                val edgeVm = _edgeViewModels[edge]
                    ?: throw NoSuchElementException("No ViewModel found for edge between vertices" +
                            " (${edge.vertex1.id}, ${edge.vertex1.data}) and" +
                            " (${edge.vertex2.id}, ${edge.vertex2.data})")
                val vertexVm = _verticesViewModels[vertex]
                    ?: throw NoSuchElementException("No ViewModel found for vertex (${vertex.id}, ${vertex.data})")

                pathVM += edgeVm to vertexVm
            }

            pathsVMs += pathVM
        }

        return pathsVMs
    }

    private fun getVMs(edgesSet: Set<Edge<D>>?): Set<EdgeViewModel<D>>? {
        if (edgesSet == null) return null

        val edgesVMsSet = mutableSetOf<EdgeViewModel<D>>()
        for (edge in edgesSet) {
            edgesVMsSet += _edgeViewModels[edge]
                ?: throw NoSuchElementException("No ViewModel found for edge between vertices" +
                        " (${edge.vertex1.id}, ${edge.vertex1.data}) and (${edge.vertex2.id}, ${edge.vertex2.data})")
        }

        return edgesVMsSet
    }
}
