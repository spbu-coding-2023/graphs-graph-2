package model.internalGraphs

import model.abstractGraph.Graph
import model.abstractGraph.Vertex
import model.edges.DirectedEdge

abstract class _DirectedGraph<D, E : DirectedEdge<D>> : Graph<D, E>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): E {
        if (vertex1 == vertex2) throw IllegalArgumentException("Vertices are the same")
        if (vertex1 !in adjacencyMap.keys || vertex2 !in adjacencyMap.keys)
            throw IllegalArgumentException("Vertex1 or vertex2 are not in the graph")

        val newEdge = DirectedEdge(vertex1, vertex2) as E
        edges.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)

        return newEdge
    }

    override fun removeEdge(edgeToRemove: E): E {
        if (edgeToRemove !in edges) throw IllegalArgumentException("Edge is not in the graph")

        val vertex1 = edgeToRemove.vertex1
        val vertex2 = edgeToRemove.vertex2

        adjacencyMap[vertex1]?.remove(vertex2)
        edges.remove(edgeToRemove)

        return edgeToRemove
    }
}