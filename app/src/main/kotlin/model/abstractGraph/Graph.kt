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

    private fun fixIdFragmentation(vertexToRemove: Vertex<D>) {
        currentId--
        val lastAddedVertex = getVertices().find { it.id == currentId }
            ?: throw NoSuchElementException("Vertex with id $currentId is not present in the adjacency map.")

        val copyOfLastAddedVertex = Vertex(vertexToRemove.id, lastAddedVertex.data)

        adjacencyMap[copyOfLastAddedVertex] = adjacencyMap[lastAddedVertex]
            ?: throw NoSuchElementException("Vertex with id ${lastAddedVertex.id} is not present in the adjacency map.")

        val adjacentVertices = adjacencyMap[copyOfLastAddedVertex]
            ?: throw NoSuchElementException("Vertex with id ${copyOfLastAddedVertex.id} is not present in the adjacency map.")

        for (adjacentVertex in adjacentVertices) {
            if (adjacencyMap[adjacentVertex]?.remove(lastAddedVertex) == true) {
                adjacencyMap[adjacentVertex]?.add(copyOfLastAddedVertex)
            }
        }

        removeVertexFromIncidentEdgesAndAdjacentVerticesMapValues(lastAddedVertex)
        adjacencyMap.remove(lastAddedVertex)
    }

    private fun removeVertexFromIncidentEdgesAndAdjacentVerticesMapValues(vertexToRemove: Vertex<D>) {
        val adjacentVertices = adjacencyMap[vertexToRemove]
            ?: throw NoSuchElementException("Vertex with id ${vertexToRemove.id} is not present in the adjacency map.")

        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToRemove)

        for (edge in getEdges()) {
            if (edge.isIncident(vertexToRemove)) edges.remove(edge)
        }
    }

    fun getVertices() = adjacencyMap.keys.toList()

    fun getEdges() = edges.toList()
}
