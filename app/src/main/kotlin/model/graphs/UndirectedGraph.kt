package model.graphs

import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Graph
import model.graphs.abstractGraph.Vertex

open class UndirectedGraph<D> : Graph<D>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D> {
        if (vertex1 == vertex2) throw IllegalArgumentException("Can't add edge from vertex to itself")

        if (vertex1 !in vertices || vertex2 !in vertices)
            throw IllegalArgumentException(
                "One of vertices (${vertex1.id}, ${vertex1.data}) and " +
                    "(${vertex2.id}, ${vertex2.data}) isn't in the graph")

        // Don't do anything if the edge is already in the graph
        if (vertex2 in getNeighbours(vertex1)) return getEdge(vertex1, vertex2)

        val newEdge = Edge(vertex1, vertex2)
        edges.add(newEdge)

        outgoingEdgesMap[vertex1]?.add(newEdge)
        outgoingEdgesMap[vertex2]?.add(newEdge)

        adjacencyMap[vertex1]?.add(vertex2)
        adjacencyMap[vertex2]?.add(vertex1)

        return newEdge
    }

    override fun removeEdge(edgeToRemove: Edge<D>): Edge<D> {
        if (edgeToRemove !in edges)
            throw NoSuchElementException(
                "Edge between vertices (${edgeToRemove.vertex1.id}, ${edgeToRemove.vertex1.data}) and " +
                    "(${edgeToRemove.vertex2.id}, ${edgeToRemove.vertex2.data}) isn't in the graph")

        val vertex1 = edgeToRemove.vertex1
        val vertex2 = edgeToRemove.vertex2

        edges.remove(edgeToRemove)

        outgoingEdgesMap[vertex1]?.remove(edgeToRemove)
        outgoingEdgesMap[vertex2]?.remove(edgeToRemove)

        adjacencyMap[vertex1]?.remove(vertex2)
        adjacencyMap[vertex2]?.remove(vertex1)

        return edgeToRemove
    }

    override fun getEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D> {
        val edge =
            outgoingEdgesMap[vertex1]?.find { it.isIncident(vertex2) }
                ?: outgoingEdgesMap[vertex2]?.find { it.isIncident(vertex1) }
                ?: throw NoSuchElementException(
                    "No edge between vertices (${vertex1.id}, ${vertex1.data}) and (${vertex2.id}, ${vertex2.data})")

        return edge
    }
}
