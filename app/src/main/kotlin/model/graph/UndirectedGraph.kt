package model.graph

open class UndirectedEdge<D>(override val vertex1: Vertex<D>, override val vertex2: Vertex<D>) :
    Edge<D>

open class UndirectedGraph<D> : Graph<D>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): UndirectedEdge<D>? {
        if (vertex1 == vertex2 ||
            vertex1 !in adjacencyMap.keys ||
            vertex2 !in adjacencyMap.keys
        ) return null

        val newEdge = UndirectedEdge(vertex1, vertex2)
        adjacencyMap[vertex1]?.add(vertex2)
        adjacencyMap[vertex2]?.add(vertex1)

        return newEdge
    }
}
