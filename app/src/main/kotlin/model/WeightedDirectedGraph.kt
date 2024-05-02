package model

import model.abstractGraph.Vertex
import model.edges.WeightedDirectedEdge

class WeightedDirectedGraph<D> : DirectedGraph<D>() {
    fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>, weight: Int): WeightedDirectedEdge<D> {
        if (vertex1 == vertex2) throw IllegalArgumentException("Vertices are the same")
        if (vertex1 !in adjacencyMap.keys || vertex2 !in adjacencyMap.keys)
            throw IllegalArgumentException("Vertex1 or vertex2 are not in the graph")

        val newEdge = WeightedDirectedEdge(vertex1, vertex2, weight)
        edges.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)

        return newEdge
    }

    /*
     * In case weight is not passed, set it to default value = 1
     */
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>) = addEdge(vertex1, vertex2, 1)
}
