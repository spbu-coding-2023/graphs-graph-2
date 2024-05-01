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

    fun deleteEdge(edgeToDelete: Edge<D>): Edge<D>? {
        if (edgeToDelete !in edges) return null

        val vertex1 = edgeToDelete.vertex1
        val vertex2 = edgeToDelete.vertex2

        adjacencyMap[vertex1]?.remove(vertex2)
        adjacencyMap[vertex2]?.remove(vertex1)
        edges.remove(edgeToDelete)

        return edgeToDelete
    }

    fun getVertices() = adjacencyMap.keys.toList()

    fun getEdges() = edges.toList()
}
