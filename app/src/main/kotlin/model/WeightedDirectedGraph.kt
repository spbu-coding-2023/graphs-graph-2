package model

import model.abstractGraph.Edge
import model.abstractGraph.Vertex
import java.util.*
import kotlin.NoSuchElementException

class WeightedDirectedGraph<D> : DirectedGraph<D>() {
    val weightMap: MutableMap<Edge<D>, Int> = mutableMapOf()

    fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>, weight: Int): Edge<D> {
        val newEdge = super.addEdge(vertex1, vertex2)

        weightMap[newEdge] = weight

        return newEdge
    }

    /*
     * In case weight is not passed, set it to default value = 1
     */
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>) = addEdge(vertex1, vertex2, 1)

    fun findShortestPathDijkstra(srcVertex: Vertex<D>, destVertex: Vertex<D>): List<Pair<Vertex<D>, Edge<D>>> {
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
                        var totalDist = currentDistance
                        totalDist += weightMap[it] ?: throw NoSuchElementException("Current edge doesn't have weight.")
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
        val path: MutableList<Pair<Vertex<D>, Edge<D>>> = mutableListOf()
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
                Pair(currentVertex,
                    edges.find { it.vertex1 == predecessor && it.vertex2 == currentVertex }
                        ?: throw NoSuchElementException("There is no edge between these vertices")))
            currentVertex = predecessor
        }
        return path.reversed()
    }
}
