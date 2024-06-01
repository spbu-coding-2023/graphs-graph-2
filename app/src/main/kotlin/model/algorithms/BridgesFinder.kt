package model.algorithms

import kotlin.math.min
import model.graphs.UndirectedGraph
import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Vertex

class BridgesFinder {
    fun <D> findBridges(graph: UndirectedGraph<D>): List<Edge<D>> {
        val vertices = graph.getVertices()
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

            for (neighbour in graph.getNeighbours(vertex)) {
                if (neighbour.id == parentList[vertex.id]) continue

                if (visitedList[neighbour.id]) {
                    minDiscoveryTime[vertex.id] = min(minDiscoveryTime[vertex.id], discoveryTime[neighbour.id])

                    continue
                }

                parentList[neighbour.id] = vertex.id

                doDFSToFindBridgesFromVertex(neighbour)

                minDiscoveryTime[vertex.id] = min(minDiscoveryTime[vertex.id], minDiscoveryTime[neighbour.id])

                if (minDiscoveryTime[neighbour.id] > discoveryTime[vertex.id]) {
                    val bridgeFound = graph.getEdge(vertex, neighbour)
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
