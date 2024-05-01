package model.graph

class UndirectedEdge<V>(override val vertex1: Vertex< V>?, override val vertex2: Vertex< V>?) : Edge< V>

class UndirectedGraph<V>: Graph<V, UndirectedEdge<V>>() {
    val adjacencyMap: MutableMap<Vertex< V>, ArrayList<UndirectedEdge<V>>> = mutableMapOf()
    private val edges: MutableSet<Edge< V>> = mutableSetOf()
    private var currentId: Int = 0

    override fun addEdge(node1: V, node2: V) {
        val vertex1 = adjacencyMap.keys.find { it.data == node1 } ?: run {
            addVertex(node1)
            adjacencyMap.keys.find { it.data == node1 }
        }
        val vertex2 = adjacencyMap.keys.find { it.data == node2 } ?: run {
            addVertex(node2)
            adjacencyMap.keys.find { it.data == node2 }
        }

        val edge = UndirectedEdge(vertex1, vertex2)

        adjacencyMap[vertex1]?.add(edge)
        adjacencyMap[vertex2]?.add(edge)
    }

    override fun addVertex(value: V) {
        val vertex = Vertex(currentId, value)
        currentId++
        adjacencyMap[vertex] = ArrayList()
    }

    override fun getEdges(): List<UndirectedEdge<V>> {
        return adjacencyMap.values.flatten()
    }

    override fun getVertices(): List<Vertex<V>> {
        return adjacencyMap.keys.toList()
    }
}
