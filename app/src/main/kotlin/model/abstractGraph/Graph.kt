package model.abstractGraph

abstract class Graph<D, E : Edge<D>> {
    protected val adjacencyMap: MutableMap<Vertex<D>, ArrayList<Vertex<D>>> = mutableMapOf()
    protected val edges: MutableSet<E> = mutableSetOf()
    private var currentId = 0uL

    fun addVertex(data: D): Vertex<D> {
        val newVertex = Vertex(currentId++, data)
        adjacencyMap[newVertex] = ArrayList()

        return newVertex
    }

    fun deleteVertex(vertexToDelete: Vertex<D>): Vertex<D> {
        val adjacentVertices =
            adjacencyMap[vertexToDelete] ?: throw IllegalArgumentException("Vertex is not in the graph")

        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToDelete)
        adjacencyMap.remove(vertexToDelete)

        return vertexToDelete
    }

    abstract fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): E

    abstract fun removeEdge(edgeToRemove: E): E

    fun getVertices() = adjacencyMap.keys.toList()

    fun getEdges() = edges.toList()
}
