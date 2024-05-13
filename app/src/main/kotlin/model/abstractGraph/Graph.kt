package model.abstractGraph

abstract class Graph<D> {
    protected val vertices: ArrayList<Vertex<D>> = arrayListOf()
    protected val edges: MutableSet<Edge<D>> = mutableSetOf()

    protected val adjacencyMap: MutableMap<Vertex<D>, ArrayList<Vertex<D>>> = mutableMapOf()
    protected val outEdgesMap: MutableMap<Vertex<D>, ArrayList<Edge<D>>> = mutableMapOf()

    private var nextId = 0

    fun addVertex(data: D): Vertex<D> {
        val newVertex = Vertex(nextId++, data)

        outEdgesMap[newVertex] = ArrayList()
        adjacencyMap[newVertex] = ArrayList()

        vertices.add(newVertex)

        return newVertex
    }

    fun removeVertex(vertexToRemove: Vertex<D>): Vertex<D> {
        removeVertexFromIncidentEdgesAndAdjacentVerticesMapValues(vertexToRemove)
        fixIdFragmentation(vertexToRemove)

        adjacencyMap.remove(vertexToRemove)

        return vertexToRemove
    }

    private fun fixIdFragmentation(vertexToRemove: Vertex<D>) {
        nextId--
        val lastAddedVertex = vertices[nextId]

        val copyOfLastAddedVertex = Vertex(vertexToRemove.id, lastAddedVertex.data)

        adjacencyMap[copyOfLastAddedVertex] = getNeighbours(lastAddedVertex)

        val adjacentVertices = getNeighbours(copyOfLastAddedVertex)

        for (adjacentVertex in adjacentVertices) {
            if (adjacencyMap[adjacentVertex]?.remove(lastAddedVertex) == true) {
                adjacencyMap[adjacentVertex]?.add(copyOfLastAddedVertex)
            }
        }

        removeVertexFromIncidentEdgesAndAdjacentVerticesMapValues(lastAddedVertex)
        adjacencyMap.remove(lastAddedVertex)
    }

    private fun removeVertexFromIncidentEdgesAndAdjacentVerticesMapValues(vertexToRemove: Vertex<D>) {
        val adjacentVertices = getNeighbours(vertexToRemove)

        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToRemove)

        for (edge in getEdges()) {
            if (edge.isIncident(vertexToRemove)) edges.remove(edge)
        }
    }

    abstract fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D>

    abstract fun removeEdge(edgeToRemove: Edge<D>): Edge<D>

    fun getEdges() = edges.toList()

    protected fun getNeighbours(vertex: Vertex<D>): ArrayList<Vertex<D>> {
        val neighbours = adjacencyMap[vertex]
            ?: throw NoSuchElementException("Vertex with id ${vertex.id} is not present in the adjacency map.")

        return neighbours
    }
}
