package model

import kotlin.math.min
import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex

open class UndirectedGraph<D> : Graph<D>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D> {
        if (vertex1 == vertex2)
            throw IllegalArgumentException("Can't add edge from vertex to itself.")

        if (vertex1.id > vertices.size || vertex2.id > vertices.size)
            throw NoSuchElementException(
                "One of vertices (${vertex1.id}, ${vertex1.data}) and " +
                    "(${vertex2.id}, ${vertex2.data}) is not in the vertices array."
            )

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
                    "(${edgeToRemove.vertex2.id}, ${edgeToRemove.vertex2.data}) is not in the graph"
            )

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
        val edge = outgoingEdgesMap[vertex1]?.find { it.isIncident(vertex2) }
            ?: outgoingEdgesMap[vertex2]?.find { it.isIncident(vertex1) }
            ?: throw NoSuchElementException(
                "No edge between vertices (${vertex1.id}, ${vertex1.data}) and (${vertex2.id}, ${vertex2.data})"
            )

        return edge
    }

    fun findBridges(): List<Edge<D>> {
        val bridges = mutableListOf<Edge<D>>()

        val graphSize = vertices.size

        val discoveryTime = MutableList(graphSize) { -1 }
        val minDiscoveryTime = MutableList(graphSize) { -1 }

        val visitedList = MutableList(graphSize) { false }
        val parentList = MutableList(graphSize) { -1 }

        var iterationCount = 0

        fun doDFSToFindBridgesFromVertex(vertex: Vertex<D>) {
            visitedList[vertex.id] = true

            iterationCount++
            discoveryTime[vertex.id] = iterationCount
            minDiscoveryTime[vertex.id] = iterationCount

            for (neighbour in getNeighbours(vertex)) {
                if (neighbour.id == parentList[vertex.id]) continue

                if (visitedList[neighbour.id]) {
                    minDiscoveryTime[vertex.id] =
                        min(minDiscoveryTime[vertex.id], discoveryTime[neighbour.id])

                    continue
                }

                parentList[neighbour.id] = vertex.id

                doDFSToFindBridgesFromVertex(neighbour)

                minDiscoveryTime[vertex.id] =
                    min(minDiscoveryTime[vertex.id], minDiscoveryTime[neighbour.id])

                if (minDiscoveryTime[neighbour.id] > discoveryTime[vertex.id]) {
                    val bridgeFound = getEdge(vertex, neighbour)
                    bridges.add(bridgeFound)
                }
            }
        }

        for (v in vertices) {
            if (visitedList[v.id]) continue

            doDFSToFindBridgesFromVertex(v)
        }

        return bridges
    }
}
