package model

import kotlin.math.min
import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex

open class UndirectedGraph<D> : Graph<D>() {
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D> {
        if (vertex1 == vertex2)
            throw IllegalArgumentException("Can't add edge from vertex to itself.")

        if (vertex1 !in adjacencyMap.keys || vertex2 !in adjacencyMap.keys)
            throw NoSuchElementException("Vertex1 or vertex2 is not in the adjacency map.")

        val newEdge = Edge(vertex1, vertex2) as Edge<D>
        edges.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)
        adjacencyMap[vertex2]?.add(vertex1)

        return newEdge
    }

    override fun removeEdge(edgeToRemove: Edge<D>): Edge<D> {
        if (edgeToRemove !in edges)
            throw NoSuchElementException("Edge is not in the graph.")

        val vertex1 = edgeToRemove.vertex1
        val vertex2 = edgeToRemove.vertex2

        adjacencyMap[vertex1]?.remove(vertex2)
        adjacencyMap[vertex2]?.remove(vertex1)
        edges.remove(edgeToRemove)

        return edgeToRemove
    }

    fun findBridges(): List<Edge<D>> {
        val bridges = mutableListOf<Edge<D>>()

        val vertices = getVertices()
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
                    val bridgeFound = edges.find { it.isIncident(vertex) && it.isIncident(neighbour) }
                        ?: throw NoSuchElementException("Can't find edge between vertices with ids ${vertex.id} and ${neighbour.id}")

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
