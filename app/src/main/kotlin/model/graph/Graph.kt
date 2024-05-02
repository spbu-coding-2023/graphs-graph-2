package model.graph

abstract class Graph<D> {
    protected val adjacencyMap: MutableMap<Vertex<D>, ArrayList<Vertex<D>>> = mutableMapOf()
    protected val edges: MutableSet<Edge<D>> = mutableSetOf()
    private var currentId = 0uL

    fun addVertex(data: D) {
        val newVertex = Vertex(currentId++, data)
        adjacencyMap[newVertex] = ArrayList()
    }

    fun deleteVertex(vertexToDelete: Vertex<D>): Vertex<D>? {
        val adjacentVertices = adjacencyMap[vertexToDelete] ?: return null
        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToDelete)
        adjacencyMap.remove(vertexToDelete)

        return vertexToDelete
    }

    abstract fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D>?

    abstract fun removeEdge(edgeToRemove: Edge<D>): Edge<D>?

    fun getVertices() = adjacencyMap.keys.toList()

    fun getEdges() = edges.toList()
}
