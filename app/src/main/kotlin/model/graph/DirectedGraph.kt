package model.graph

class DirectedEdge<V>(vertex1: V, vertex2: V) : Edge<V>() {
    override val vertices: Pair<Vertex<V>, Vertex<V>> = Pair(Vertex(vertex1), Vertex(vertex2))
}

class DirectedGraph<V>: Graph<V>() {
    val adjacencyMap: MutableMap<Vertex<V>, ArrayList<Vertex<V>?>> = mutableMapOf()
    private val edges: MutableSet<Edge<V>> = mutableSetOf()

    override fun addEdge(node1: V, node2: V) {
        val vertex1 = adjacencyMap.keys.find { it.data == node1 } ?: run {
            addVertex(node1)
            adjacencyMap.keys.find { it.data == node1 }
        }
        val vertex2 = adjacencyMap.keys.find { it.data == node2 } ?: run {
            addVertex(node2)
            adjacencyMap.keys.find { it.data == node2 }
        }

        // val edge = UndirectedEdge(vertex1, vertex2)
        adjacencyMap[vertex1]?.add(vertex2)
    }

    override fun addVertex(value: V) {
        val vertex = Vertex(value)
        adjacencyMap[vertex] = ArrayList()
    }

    override fun getEdges(): List<Vertex<V>?> {
        return adjacencyMap.values.flatten()
    }

    override fun getVertices(): List<Vertex<V>> {
        return adjacencyMap.keys.toList()
    }

}