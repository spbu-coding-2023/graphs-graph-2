package model.graph

abstract class Graph<D> {
    val adjacencyMap: MutableMap<Vertex<D>, ArrayList<Vertex<D>>> = mutableMapOf()
    val edges: MutableSet<Edge<D>> = mutableSetOf()
    var currentId = 0uL
    // TODO("add visibility modifiers for properties")
    fun addVertex(data: D) {
        val newVertex = Vertex(currentId++, data)
        adjacencyMap[newVertex] = ArrayList()
    }

    fun deleteVertex(vertexToDelete: Vertex<D>): D? {
        val adjacentVertices = adjacencyMap[vertexToDelete] ?: return null
        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToDelete)
        adjacencyMap.remove(vertexToDelete)

        return vertexToDelete.data
    }

    abstract fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>)

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
