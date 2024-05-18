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

        val adjacentVertices = getNeighbours(vertexToRemove)
        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToRemove)

        // iterator is used because an element can't be removed in a for loop
        val iterator = edges.iterator()
        while (iterator.hasNext()) {
            val edge = iterator.next()
            if (edge.isIncident(vertexToRemove)) {
                iterator.remove()

                val incidentVertex = if (edge.vertex1 == vertexToRemove) edge.vertex2 else edge.vertex1
                outgoingEdgesMap[incidentVertex]?.remove(edge)
                adjacencyMap[incidentVertex]?.remove(vertexToRemove)
            }
        }

        val lastAddedVertex = vertices[nextId]
        lastAddedVertex.id = vertexToRemove.id
        vertices[vertexToRemove.id] = lastAddedVertex

        vertices.remove(vertexToRemove)
        adjacencyMap.remove(vertexToRemove)
        outgoingEdgesMap.remove(vertexToRemove)

        return vertexToRemove
    }

    abstract fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D>

    abstract fun removeEdge(edgeToRemove: Edge<D>): Edge<D>

    fun getEdges() = edges.toList()

    fun getVertices() = vertices.toList()

    fun getNeighbours(vertex: Vertex<D>): ArrayList<Vertex<D>> {
        val neighbours = adjacencyMap[vertex]
            ?: throw NoSuchElementException("Vertex (${vertex.id}, ${vertex.data}) isn't in the adjacency map.")

        return neighbours
    }

    fun getOutgoingEdges(vertex: Vertex<D>): ArrayList<Edge<D>> {
        val outgoingEdges = outgoingEdgesMap[vertex]
            ?: throw NoSuchElementException("Vertex (${vertex.id}, ${vertex.data}) isn't in the adjacency map.")

        return outgoingEdges
    }

    abstract fun getEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D>
}
