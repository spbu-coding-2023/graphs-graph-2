package model.graph

class WeightedUndirectedEdge<D>(vertex1: Vertex<D>, vertex2: Vertex<D>, val weight: Int) : UndirectedEdge<D>(vertex1, vertex2)

class WeightedUndirectedGraph<D>: Graph<D> { // its a sub-class of undirected so mb inherit from it
    private val adjacencyMap: MutableMap<Vertex<D>, ArrayList<WeightedUndirectedEdge<D>>> = mutableMapOf()
    private val edges: MutableSet<UndirectedEdge<D>> = mutableSetOf()
    override var currentId: ULong = 0u


    override fun getVertices(): List<Vertex<D>> {
        TODO("Not yet implemented")
    }

    override fun getEdges(): List<Edge<D>> {
        TODO("Not yet implemented")
    }

    override fun addEdge(node1: D, node2: D) {
        TODO("Not yet implemented")
    }

    fun addEdge(node1: D, node2: D, weight: Int) {
        val vertex1 = adjacencyMap.keys.find { it.data == node1 } ?: run {
            addVertex(node1)
            adjacencyMap.keys.find { it.data == node1 }
        }
        val vertex2 = adjacencyMap.keys.find { it.data == node2 } ?: run {
            addVertex(node2)
            adjacencyMap.keys.find { it.data == node2 }
        }

        if (vertex1 != null && vertex2 != null) {
            val newEdge = WeightedUndirectedEdge(vertex1, vertex2, weight)
            adjacencyMap[vertex1]?.add(newEdge)
            adjacencyMap[vertex2]?.add(newEdge)
        }
    }

    override fun addVertex(value: D) {
        val vertex = Vertex(currentId++, value)
        adjacencyMap[vertex] = ArrayList()
    }
}