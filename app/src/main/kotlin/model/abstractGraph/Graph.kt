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
        removeVertexFromIncidentEdgesAndAdjacentVerticesMapValues(vertexToRemove)
        fixIdFragmentation(vertexToRemove)

        adjacencyMap.remove(vertexToRemove)

        return vertexToRemove
    }

    abstract fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): E

    abstract fun removeEdge(edgeToRemove: E): E

    fun getVertices() = adjacencyMap.keys.toList()

    fun getEdges() = edges.toList()

    private fun fixIdFragmentation(vertexToRemove: Vertex<D>) {
        currentId--
        val lastAddedVertex =
            getVertices().find { it.id == currentId } ?: throw NoSuchElementException("No vertex with id $currentId")

        val copyOfLastAddedVertex = Vertex<D>(vertexToRemove.id, lastAddedVertex.data)
        adjacencyMap[copyOfLastAddedVertex] =
            adjacencyMap[lastAddedVertex] ?: throw NoSuchElementException("No vertex with id $currentId")

        val adjacentVertices =
            adjacencyMap[copyOfLastAddedVertex] ?: throw NoSuchElementException("No vertex with id $currentId")

        for (adjacentVertex in adjacentVertices) {
            if (adjacencyMap[adjacentVertex]?.remove(lastAddedVertex) ?: false) {
                adjacencyMap[adjacentVertex]?.add(copyOfLastAddedVertex)
            }
        }

        removeVertexFromIncidentEdgesAndAdjacentVerticesMapValues(lastAddedVertex)
        adjacencyMap.remove(lastAddedVertex)
    }

    private fun removeVertexFromIncidentEdgesAndAdjacentVerticesMapValues(vertexToRemove: Vertex<D>) {
        val adjacentVertices =
            adjacencyMap[vertexToRemove] ?: throw NoSuchElementException("Vertex is not in the graph")

        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToRemove)

        for (edge in getEdges()) {
            if (edge.isIncident(vertexToRemove)) edges.remove(edge)
        }
    }
}
