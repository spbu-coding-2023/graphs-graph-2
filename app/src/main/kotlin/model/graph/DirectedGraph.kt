package model.graph

open class DirectedEdge<D>(override val vertex1: Vertex<D>, override val vertex2: Vertex<D>, override val weight: Int = 0) :
    Edge<D>

open class DirectedGraph<D>: Graph<D>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): DirectedEdge<D>? {
        if (vertex1 == vertex2 ||
            vertex1 !in adjacencyMap.keys ||
            vertex2 !in adjacencyMap.keys
        ) return null

        val newEdge = DirectedEdge(vertex1, vertex2)
        edges.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)

        return newEdge
    }

    override fun deleteEdge(edgeToDelete: Edge<D>): Edge<D>? {
        if (edgeToDelete !in edges) return null

        val vertex1 = edgeToDelete.vertex1
        val vertex2 = edgeToDelete.vertex2

        adjacencyMap[vertex1]?.remove(vertex2)
        edges.remove(edgeToDelete)

        return edgeToDelete
    }
}
