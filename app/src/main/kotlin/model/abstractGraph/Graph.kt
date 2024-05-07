package model.abstractGraph

abstract class Graph<D, E : Edge<D>> {
    protected val adjacencyMap: MutableMap<Vertex<D>, ArrayList<Vertex<D>>> = mutableMapOf()
    protected val edges: MutableSet<E> = mutableSetOf()
    private var currentId = 0

    fun addVertex(data: D): Vertex<D> {
        val newVertex = Vertex(currentId++, data)
        adjacencyMap[newVertex] = ArrayList()

        return newVertex
    }

    fun removeVertex(vertexToRemove: Vertex<D>): Vertex<D> {
        val adjacentVertices =
            adjacencyMap[vertexToRemove] ?: throw IllegalArgumentException("Vertex is not in the graph")

        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToRemove)
        adjacencyMap.remove(vertexToRemove)

        for (edge in getEdges()) {
            if (edge.isIncident(vertexToRemove)) edges.remove(edge)
        }

        return vertexToRemove
    }

    abstract fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): E

    abstract fun removeEdge(edgeToRemove: E): E

    fun getVertices() = adjacencyMap.keys.toList()

    fun getEdges() = edges.toList()
}
