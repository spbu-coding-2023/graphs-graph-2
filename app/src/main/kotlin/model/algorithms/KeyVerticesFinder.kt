package model.algorithms

import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Graph
import model.graphs.abstractGraph.Vertex
import java.util.*
import java.util.ArrayDeque
import kotlin.math.roundToInt

class KeyVerticesFinder {
    /**
     * For every vertex, calculates normalized closeness centrality, based on which the key vertices are picked.
     * Formula was taken from "Efficient Top-k Closeness Centrality Search" by Paul W. Olsen et al.,
     * yet an easier algorithm for traversal was chosen.
     */
    fun <D> findKeyVertices(graph: Graph<D>): Set<Vertex<D>>? {
        val vertices = graph.getVertices()
        val graphSize = vertices.size

        val distanceMap = graph.getWeightMap()
        if (graph.hasNegativeEdges()) return null

        val centralityMap = mutableMapOf<Vertex<D>, Double>()

        for (currVertex in vertices) {
            val currSumOfDistances = calcSumOfDistancesFromVertex(graph, currVertex, distanceMap, graphSize)

            val reachableNum = findReachableVerticesNumFromVertex(graph, currVertex, graphSize)

            val currCentrality = calcCentralityOfVertex(currSumOfDistances, reachableNum, graphSize)
            centralityMap[currVertex] = currCentrality
        }

        val keyVertices = pickMostKeyVertices(centralityMap, graphSize)

        return keyVertices
    }

    /**
     * Uses modified Dijkstra's algorithm to calculate the sum of all weights (distances)
     * of shortest paths from source vertex to every other reachable one
     */
    private fun <D> calcSumOfDistancesFromVertex(
        graph: Graph<D>,
        srcVertex: Vertex<D>,
        distanceMap: Map<Edge<D>, Int>,
        graphSize: Int
    ): Int {
        val POS_INF = 100_000_000   // to infinity and beyond

        val visited = Array(graphSize) { false }

        val distances = Array(graphSize) { POS_INF }
        distances[srcVertex.id] = 0

        // stores pairs of vertices and total distances to them, sorted by distances ascending
        val priorityQueue = PriorityQueue<Pair<Vertex<D>, Int>>(compareBy { it.second })
        priorityQueue.add(srcVertex to distances[srcVertex.id])

        while (priorityQueue.isNotEmpty()) {
            val (currVertex, currDistance) = priorityQueue.poll()

            if (visited[currVertex.id]) continue

            visited[currVertex.id] = true

            val neighbours = graph.getNeighbours(currVertex)
            for (neighbour in neighbours) {
                val edgeToNeighbour = graph.getEdge(currVertex, neighbour)

                val edgeToNeighbourDistance = distanceMap[edgeToNeighbour] ?: POS_INF

                val totalDistanceToNeighbour = currDistance + edgeToNeighbourDistance

                if (totalDistanceToNeighbour < distances[neighbour.id]) {
                    distances[neighbour.id] = totalDistanceToNeighbour
                    priorityQueue.add(neighbour to totalDistanceToNeighbour)
                }
            }
        }

        for (i in distances.indices) {
            if (distances[i] == POS_INF) distances[i] = 0
        }

        val sum = distances.sum()

        return sum
    }

    private fun <D> findReachableVerticesNumFromVertex(graph: Graph<D>, vertex: Vertex<D>, graphSize: Int): Int {
        var reachableVerticesNum = 0

        val verticesToVisit = ArrayDeque<Vertex<D>>()
        verticesToVisit.add(vertex)

        val visited = Array(graphSize) { false }

        while (verticesToVisit.isNotEmpty()) {
            val vertexToVisit = verticesToVisit.poll()

            if (visited[vertexToVisit.id]) continue

            for (neighbour in graph.getNeighbours(vertexToVisit)) {
                verticesToVisit.add(neighbour)
            }

            visited[vertexToVisit.id] = true
            verticesToVisit.add(vertexToVisit)

            reachableVerticesNum++
        }

        return reachableVerticesNum
    }

    private fun calcCentralityOfVertex(sumOfDistances: Int, reachableNum: Int, graphSize: Int): Double {
        if (sumOfDistances == 0) return 0.0

        val centrality =
            ((reachableNum - 1) * (reachableNum - 1)) / ((graphSize - 1) * sumOfDistances).toDouble()

        return centrality
    }

    private fun <D> pickMostKeyVertices(centralityMap: Map<Vertex<D>, Double>, graphSize: Int): Set<Vertex<D>> {
        val keyVertices = mutableSetOf<Vertex<D>>()

        val percent = 0.2
        val keyVerticesNum = (graphSize * percent).roundToInt()     // rounds up

        var currKeyVerticesNum = 0

        val vertexCentralityPairs = centralityMap.toList()
        val vertexCentralityPairsSorted = vertexCentralityPairs.sortedByDescending { it.second }

        for ((vertex, _) in vertexCentralityPairsSorted) {
            if (currKeyVerticesNum >= keyVerticesNum) break

            keyVertices.add(vertex)
            currKeyVerticesNum++
        }

        return keyVertices
    }
}
