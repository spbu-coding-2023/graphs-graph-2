package model.graph

class WeightedUndirectedEdge<D>(vertex1: Vertex<D>, vertex2: Vertex<D>, val weight: Int) :
    UndirectedEdge<D>(vertex1, vertex2)

class WeightedUndirectedGraph<D> : UndirectedGraph<D>() {
    fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>, weight: Int): WeightedUndirectedEdge<D>? {
        if (vertex1 == vertex2 ||
            vertex1 !in adjacencyMap.keys ||
            vertex2 !in adjacencyMap.keys
        ) return null

        val newEdge = WeightedUndirectedEdge(vertex1, vertex2, weight)
        edges.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)
        adjacencyMap[vertex2]?.add(vertex1)

        return newEdge
    }

    /*
     * In case weight is not passed, set it to default value = 1
     */
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>) = addEdge(vertex1, vertex2, 1)
}
