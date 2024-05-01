package model.graph

class WeightedDirectedEdge<D>(vertex1: Vertex<D>, vertex2: Vertex<D>, val weight: Int) :
    DirectedEdge<D>(vertex1, vertex2)

class WeightedDirectedGraph<D> : DirectedGraph<D>() {
    fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>, weight: Int): WeightedDirectedEdge<D>? {
        if (vertex1 == vertex2 ||
            vertex1 !in adjacencyMap.keys ||
            vertex2 !in adjacencyMap.keys
        ) return null

        val newEdge = WeightedDirectedEdge(vertex1, vertex2, weight)
        adjacencyMap[vertex1]?.add(vertex2)
        adjacencyMap[vertex2]?.add(vertex1)

        return newEdge
    }
}
