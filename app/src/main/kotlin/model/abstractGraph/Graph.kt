package model.abstractGraph

abstract class Graph<D> {
    protected val vertices: ArrayList<Vertex<D>> = arrayListOf()
    protected val edges: MutableSet<Edge<D>> = mutableSetOf()

    protected val adjacencyMap: MutableMap<Vertex<D>, ArrayList<Vertex<D>>> = mutableMapOf()
    protected val outgoingEdgesMap: MutableMap<Vertex<D>, ArrayList<Edge<D>>> = mutableMapOf()

    private var nextId = 0

    fun addVertex(data: D): Vertex<D> {
        val newVertex = Vertex(nextId++, data)

        outgoingEdgesMap[newVertex] = ArrayList()
        adjacencyMap[newVertex] = ArrayList()

        vertices.add(newVertex)

        return newVertex
    }

    fun removeVertex(vertexToRemove: Vertex<D>): Vertex<D> {
        nextId--

        removeVertexFromEverywhere(vertexToRemove)
        fixIdFragmentation(vertexToRemove)

        return vertexToRemove
    }

    private fun fixIdFragmentation(vertexToRemove: Vertex<D>) {
        if (vertexToRemove.id == nextId) return

        val lastAddedVertex = vertices[nextId]

        val copyOfLastAddedVertex = Vertex(vertexToRemove.id, lastAddedVertex.data)

        vertices[copyOfLastAddedVertex.id] = copyOfLastAddedVertex
        adjacencyMap[copyOfLastAddedVertex] = getNeighbours(lastAddedVertex)
        outgoingEdgesMap[copyOfLastAddedVertex] = getOutgoingEdges(lastAddedVertex)

        val adjacentVertices = getNeighbours(copyOfLastAddedVertex)

        for (adjacentVertex in adjacentVertices) {
            if (adjacencyMap[adjacentVertex]?.remove(lastAddedVertex) == true) {
                adjacencyMap[adjacentVertex]?.add(copyOfLastAddedVertex)
            }
        }

        removeVertexFromEverywhere(lastAddedVertex)
    }

    private fun removeVertexFromEverywhere(vertexToRemove: Vertex<D>) {
        val adjacentVertices = getNeighbours(vertexToRemove)

        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToRemove)

        // If vertexToRemove isn't last, it will be overridden by its copy in fixIdFragmentation
        if (vertexToRemove.id == nextId) vertices.removeLast()

        for (edge in edges) {
            if (edge.isIncident(vertexToRemove)) {
                edges.remove(edge)

                val incidentVertex = if (edge.vertex1 == vertexToRemove) edge.vertex2 else edge.vertex1
                outgoingEdgesMap[incidentVertex]?.remove(edge)
            }
        }

        adjacencyMap.remove(vertexToRemove)
        outgoingEdgesMap.remove(vertexToRemove)
    }

    abstract fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D>

    abstract fun removeEdge(edgeToRemove: Edge<D>): Edge<D>

    fun getEdges() = edges.toList()

    fun getVertices() = vertices.toList()

    protected fun getEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D> {
        val edge = outgoingEdgesMap[vertex1]?.find { it.isIncident(vertex2) }
            ?: throw NoSuchElementException("Can't find edge between vertices with ids ${vertex1.id} and ${vertex2.id}")

        return edge
    }

    // This and next method are used to localize exceptions
    protected fun getNeighbours(vertex: Vertex<D>): ArrayList<Vertex<D>> {
        val neighbours = adjacencyMap[vertex]
            ?: throw NoSuchElementException("Vertex with id ${vertex.id} is not present in the adjacency map.")

        return neighbours
    }

    protected fun getOutgoingEdges(vertex: Vertex<D>): ArrayList<Edge<D>> {
        val outgoingEdges = outgoingEdgesMap[vertex]
            ?: throw NoSuchElementException("Vertex with id ${vertex.id} is not present in the outgoing edges map.")

        return outgoingEdges
    }
}
