package model

import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex
import kotlin.math.min

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

        val discTime = MutableList(graphSize) { -1 }
        val minDiscTime = MutableList(graphSize) { -1 }

        val visited = MutableList(graphSize) { false }
        val parent = MutableList(graphSize) { -1 }

        var iterationCount = 0

        fun doDFSToFindBrigesFromVertex(vertex: Vertex<D>) {
            visited[vertex.id] = true

            iterationCount++
            discTime[vertex.id] = iterationCount
            minDiscTime[vertex.id] = iterationCount

            for (neighbour in getNeighbours(vertex)) {
                if (!visited[neighbour.id]) {
                    parent[neighbour.id] = vertex.id

                    doDFSToFindBrigesFromVertex(neighbour)

                    minDiscTime[vertex.id] =
                        min(minDiscTime[vertex.id], minDiscTime[neighbour.id])

                    if (minDiscTime[neighbour.id] > discTime[vertex.id]) {
                        val bridgeFound = edges.find { it.isIncident(vertex) && it.isIncident(neighbour) }
                            ?: throw NoSuchElementException("Can't find edge between vertices with ids ${vertex.id} and ${neighbour.id}")

                        bridges.add(bridgeFound)
                    }
                } else if (neighbour.id != parent[vertex.id]) {
                    minDiscTime[vertex.id] =
                        min(minDiscTime[vertex.id], discTime[neighbour.id])
                }
            }
        }

        for (v in vertices) {
            if (visited[v.id]) continue

            doDFSToFindBrigesFromVertex(v)
        }

        return bridges
    }
}

