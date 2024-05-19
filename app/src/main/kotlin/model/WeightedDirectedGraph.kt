package model

import model.abstractGraph.Edge
import model.abstractGraph.Vertex
import java.util.*
import kotlin.NoSuchElementException

class WeightedDirectedGraph<D> : DirectedGraph<D>() {
    private val weightMap: MutableMap<Edge<D>, Int> = mutableMapOf()

    fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>, weight: Int): Edge<D> {
        val newEdge = super.addEdge(vertex1, vertex2)

        weightMap[newEdge] = weight

        return newEdge
    }

    // In case weight is not passed, set it to default value = 1
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>) = addEdge(vertex1, vertex2, 1)

    override fun removeEdge(edgeToRemove: Edge<D>): Edge<D> {
        val removedEdge = super.removeEdge(edgeToRemove)

        weightMap.remove(edgeToRemove)

        return removedEdge
    }

    fun getWeight(edge: Edge<D>): Int {
        val weight = weightMap[edge]
            ?: throw NoSuchElementException(
                "No weight found for edge between vertices (${edge.vertex1.id}, ${edge.vertex1.data}) " +
                    "and (${edge.vertex2.id}, ${edge.vertex2.data})"
            )

        return weight
    }

    fun findShortestPathDijkstra(srcVertex: Vertex<D>, destVertex: Vertex<D>): List<Pair<Vertex<D>, Edge<D>>> {
        val distanceMap = mutableMapOf<Vertex<D>, Int>().withDefault { Int.MAX_VALUE }
        val predecessorMap = mutableMapOf<Vertex<D>, Vertex<D>?>()
        val priorityQueue = PriorityQueue<Pair<Vertex<D>, Int>>(compareBy { it.second }).apply { add(destVertex to 0) }
        val visited = mutableSetOf<Pair<Vertex<D>, Int>>()

        distanceMap[srcVertex] = 0

        while (priorityQueue.isNotEmpty()) {
            val (currentVertex, currentDistance) = priorityQueue.poll()
            if (visited.add(currentVertex to currentDistance)) {
                adjacencyMap[currentVertex]?.forEach { adjacent ->
                    val currentEdge = getEdge(adjacent, currentVertex)

                    val totalDist = currentDistance + getWeight(currentEdge)

                    if (totalDist < distanceMap.getValue(adjacent)) {
                        distanceMap[adjacent] = totalDist
                        predecessorMap[adjacent] = currentVertex // Update predecessor
                        priorityQueue.add(adjacent to totalDist)
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

            val currentEdge = getEdge(predecessor, currentVertex)
            path.add(Pair(currentVertex, currentEdge))

            currentVertex = predecessor
        }
        return path.reversed()
    }
}
