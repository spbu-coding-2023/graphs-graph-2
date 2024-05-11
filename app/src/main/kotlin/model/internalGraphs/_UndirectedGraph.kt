package model.internalGraphs

import model.abstractGraph.Graph
import model.abstractGraph.Vertex
import model.edges.UndirectedEdge

abstract class _UndirectedGraph<D, E : UndirectedEdge<D>> : Graph<D, E>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): E {
        if (vertex1 == vertex2)
            throw IllegalArgumentException("Can't add edge from vertex to itself.")

        if (vertex1 !in adjacencyMap.keys || vertex2 !in adjacencyMap.keys)
            throw NoSuchElementException("Vertex1 or vertex2 is not in the adjacency map.")

        val newEdge = UndirectedEdge(vertex1, vertex2) as E
        edges.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)
        adjacencyMap[vertex2]?.add(vertex1)

        return newEdge
    }

    override fun removeEdge(edgeToRemove: E): E {
        if (edgeToRemove !in edges)
            throw NoSuchElementException("Edge is not in the graph.")

        val vertex1 = edgeToRemove.vertex1
        val vertex2 = edgeToRemove.vertex2

        adjacencyMap[vertex1]?.remove(vertex2)
        adjacencyMap[vertex2]?.remove(vertex1)
        edges.remove(edgeToRemove)

        return edgeToRemove
    }
}
