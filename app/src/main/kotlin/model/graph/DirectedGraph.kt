package model.graph

class DirectedEdge<D>(override val vertex1: Vertex<D>, override val vertex2: Vertex<D>) : Edge<D>

class DirectedGraph<D>: Graph<D> {
    private val adjacencyMap: MutableMap<Vertex<D>, ArrayList<DirectedEdge<D>>> = mutableMapOf()
    private val edges: MutableSet<Edge< D>> = mutableSetOf()
    override var currentId: ULong = 0u

    override fun addEdge(node1: D, node2: D) {
        val vertex1 = adjacencyMap.keys.find { it.data == node1 } ?: run {
            addVertex(node1)
            adjacencyMap.keys.find { it.data == node1 }
        }
        val vertex2 = adjacencyMap.keys.find { it.data == node2 } ?: run {
            addVertex(node2)
            adjacencyMap.keys.find { it.data == node2 }
        }
        if (vertex1 != null && vertex2 != null) {
            val edge = DirectedEdge(vertex1, vertex2)
            adjacencyMap[vertex1]?.add(edge)
        }
    }

    override fun addVertex(value: D) {
        val vertex = Vertex(currentId++, value)
        adjacencyMap[vertex] = ArrayList()
    }

    override fun getEdges(): List<UndirectedEdge<D>> {
        TODO()
    }

    override fun getVertices(): List<Vertex<D>> {
        return adjacencyMap.keys.toList()
    }

}