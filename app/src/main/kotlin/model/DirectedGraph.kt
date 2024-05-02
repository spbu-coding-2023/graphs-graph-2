package model

import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex
import model.edges.DirectedEdge

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

    override fun removeEdge(edgeToRemove: Edge<D>): Edge<D>? {
        if (edgeToRemove !in edges) return null

        val vertex1 = edgeToRemove.vertex1
        val vertex2 = edgeToRemove.vertex2

        adjacencyMap[vertex1]?.remove(vertex2)
        edges.remove(edgeToRemove)

        return edgeToRemove
    }
}
