package model.graphs.abstractGraph

import java.util.*
import java.util.ArrayDeque
import kotlin.NoSuchElementException
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

abstract class Graph<D> {
    protected val vertices: ArrayList<Vertex<D>> = arrayListOf()
    protected val edges: MutableSet<Edge<D>> = mutableSetOf()

    protected val adjacencyMap: MutableMap<Vertex<D>, MutableSet<Vertex<D>>> = mutableMapOf()
    protected val outgoingEdgesMap: MutableMap<Vertex<D>, MutableSet<Edge<D>>> = mutableMapOf()

    private var nextId = 0

    fun addVertex(data: D): Vertex<D> {
        val newVertex = Vertex(nextId++, data)

        outgoingEdgesMap[newVertex] = mutableSetOf()
        adjacencyMap[newVertex] = mutableSetOf()

        vertices.add(newVertex)

        return newVertex
    }

    fun removeVertex(vertexToRemove: Vertex<D>): Vertex<D> {
        nextId--

        val adjacentVertices = getNeighbours(vertexToRemove)
        for (adjacentVertex in adjacentVertices) adjacencyMap[adjacentVertex]?.remove(vertexToRemove)

        // iterator is used because an element can't be removed in a for loop
        val iterator = edges.iterator()
        while (iterator.hasNext()) {
            val edge = iterator.next()
            if (edge.isIncident(vertexToRemove)) {
                iterator.remove()

                val incidentVertex = if (edge.vertex1 == vertexToRemove) edge.vertex2 else edge.vertex1
                outgoingEdgesMap[incidentVertex]?.remove(edge)
                adjacencyMap[incidentVertex]?.remove(vertexToRemove)
            }
        }

        val lastAddedVertex = vertices[nextId]
        lastAddedVertex.id = vertexToRemove.id
        vertices[vertexToRemove.id] = lastAddedVertex

        vertices.remove(vertexToRemove)
        adjacencyMap.remove(vertexToRemove)
        outgoingEdgesMap.remove(vertexToRemove)

        return vertexToRemove
    }

    abstract fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D>

    abstract fun removeEdge(edgeToRemove: Edge<D>): Edge<D>

    fun getEdges() = edges.toList()

    fun getVertices() = vertices.toList()

    /* In undirected graph, returns a map with every edge as a key and 1 as a value
     * In a directed graph, returns copy of weightMap property */
    open fun getWeightMap(): MutableMap<Edge<D>, Int> {
        val weightMap = mutableMapOf<Edge<D>, Int>()

        for (edge in edges) weightMap[edge] = 1

        return weightMap
    }

    fun getNeighbours(vertex: Vertex<D>): List<Vertex<D>> {
        val neighbours = adjacencyMap[vertex]
            ?: throw NoSuchElementException("Vertex (${vertex.id}, ${vertex.data}) isn't in the adjacency map.")

        return neighbours.toList()
    }

    fun getOutgoingEdges(vertex: Vertex<D>): List<Edge<D>> {
        val outgoingEdges = outgoingEdgesMap[vertex]
            ?: throw NoSuchElementException("Vertex (${vertex.id}, ${vertex.data}) isn't in the outgoing edges map.")

        return outgoingEdges.toList()
    }

    abstract fun getEdge(vertex1: Vertex<D>, vertex2: Vertex<D>): Edge<D>

    open fun hasNegativeEdges() = false

    /* For every vertex, calculates normalized closeness centrality, based on which the key vertices are picked.
     * Formula was taken from "Efficient Top-k Closeness Centrality Search" by Paul W. Olsen et al.,
     * yet an easier algorithm for traversal was chosen. */
    fun findKeyVertices(): Set<Vertex<D>>? {
        val graphSize = vertices.size

        val distanceMap = getWeightMap()
        if (this.hasNegativeEdges()) return null

        val centralityMap = mutableMapOf<Vertex<D>, Double>()

        for (currVertex in vertices) {
            val currSumOfDistances = calcSumOfDistancesFromVertex(currVertex, distanceMap, graphSize)

            val reachableNum = findReachableVerticesNumFromVertex(currVertex, graphSize)

            val currCentrality = calcCentralityOfVertex(currSumOfDistances, reachableNum, graphSize)
            centralityMap[currVertex] = currCentrality
        }

        val keyVertices = pickMostKeyVertices(centralityMap, graphSize)

        return keyVertices
    }

    /* Uses modified Dijkstra's algorithm to calculate the sum of all weights (distances)
     * of shortest paths from source vertex to every other reachable one */
    private fun calcSumOfDistancesFromVertex(
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

            val neighbours = getNeighbours(currVertex)
            for (neighbour in neighbours) {
                val edgeToNeighbour = getEdge(currVertex, neighbour)

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

    private fun findReachableVerticesNumFromVertex(vertex: Vertex<D>, graphSize: Int): Int {
        var reachableVerticesNum = 0

        val verticesToVisit = ArrayDeque<Vertex<D>>()
        verticesToVisit.add(vertex)

        val visited = Array(graphSize) { false }

        while (verticesToVisit.isNotEmpty()) {
            val vertexToVisit = verticesToVisit.poll()

            if (visited[vertexToVisit.id]) continue

            for (neighbour in getNeighbours(vertexToVisit)) {
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

    private fun pickMostKeyVertices(centralityMap: Map<Vertex<D>, Double>, graphSize: Int): Set<Vertex<D>> {
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
