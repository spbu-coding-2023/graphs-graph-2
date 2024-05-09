package model.internalGraphs

import java.util.*
import model.abstractGraph.Vertex
import model.edges.WeightedDirectedEdge
import kotlin.NoSuchElementException

abstract class _WeightedDirectedGraph<D, E : WeightedDirectedEdge<D>> : _DirectedGraph<D, E>() {
    fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>, weight: Int): E {
        if (vertex1 == vertex2)
            throw IllegalArgumentException("Can't add edge from vertex to itself.")

        if (vertex1 !in adjacencyMap.keys || vertex2 !in adjacencyMap.keys)
            throw NoSuchElementException("Vertex1 or vertex2 is not in the adjacency map.")

        val newEdge = WeightedDirectedEdge(vertex1, vertex2, weight) as E
        edges.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)

        return newEdge
    }

    /*
     * In case weight is not passed, set it to default value = 1
     */
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>) = addEdge(vertex1, vertex2, 1)

    fun findShortestPathDijkstra(srcVertex: Vertex<D>, destVertex: Vertex<D>): List<Pair<Vertex<D>, E>> {
        val vertices = getVertices()
        val distanceMap = mutableMapOf<Vertex<D>, Int>().withDefault { Int.MAX_VALUE }
        val predecessorMap = mutableMapOf<Vertex<D>, Vertex<D>?>()
        val priorityQueue = PriorityQueue<Pair<Vertex<D>, Int>>(compareBy { it.second }).apply { add(destVertex to 0) }
        val visited = mutableSetOf<Pair<Vertex<D>, Int>>()

        distanceMap[srcVertex] = 0

        while (priorityQueue.isNotEmpty()) {
            val (node, currentDistance) = priorityQueue.poll()
            if (visited.add(node to currentDistance)) {
                adjacencyMap[node]?.forEach { adjacent ->
                    val currentEdge = edges.find { it.vertex1 == adjacent }
                    currentEdge?.let {
                        val totalDist = currentDistance + it.weight
                        if (totalDist < distanceMap.getValue(adjacent)) {
                            distanceMap[adjacent] = totalDist
                            predecessorMap[adjacent] = node // Update predecessor
                            priorityQueue.add(adjacent to totalDist)
                        }
                    }
                }
            }
        }

        // Reconstruct the path from srcVertex to destVertex
        val path: MutableList<Pair<Vertex<D>, E>> = mutableListOf()
        var currentVertex = destVertex
        while (currentVertex != srcVertex) {
            val predecessor = predecessorMap[currentVertex]
            if (predecessor == null) {
                // If no path exists
                return emptyList()
            }
            if (edges.find { it.vertex1 == predecessor && it.vertex2 == currentVertex } == null) {
                throw NoSuchElementException("Edge is not in the graph, path cannot be reconstructed.")
            }
            path.add(
                Pair(currentVertex, edges.find { it.vertex1 == predecessor && it.vertex2 == currentVertex })
                    as Pair<Vertex<D>, E>)
            currentVertex = predecessor
        }
        return path.reversed()
    }
}
