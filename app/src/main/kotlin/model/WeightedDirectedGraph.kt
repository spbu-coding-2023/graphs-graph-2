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

    fun findShortestPathDijkstra(srcVertex: Vertex<D>, destVertex: Vertex<D>): List<Pair<Vertex<D>, Edge<D>>>? {
        val distanceMap = mutableMapOf<Vertex<D>, Int>().withDefault { Int.MAX_VALUE }
        val predecessorMap = mutableMapOf<Vertex<D>, Vertex<D>?>()
        val priorityQueue = PriorityQueue<Pair<Vertex<D>, Int>>(compareBy { it.second }).apply { add(srcVertex to 0) }
        val visited = mutableSetOf<Pair<Vertex<D>, Int>>()

        if (srcVertex == destVertex) return emptyList()

        distanceMap[srcVertex] = 0

        while (priorityQueue.isNotEmpty()) {
            val (currentVertex, currentDistance) = priorityQueue.poll()
            if (visited.add(currentVertex to currentDistance)) {
                getNeighbours(currentVertex).forEach { adjacent ->
                    val currentEdge = getEdge(currentVertex, adjacent) // Ensure correct edge direction

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
                ?: return null // path doesn't exist

            val currentEdge = getEdge(predecessor, currentVertex)
            path.add(Pair(currentVertex, currentEdge))

            currentVertex = predecessor
        }
        return path.reversed()
    }

    // returns null if path doesn't exist
    fun findShortestPathFordBellman(srcVertex: Vertex<D>, destVertex: Vertex<D>): List<Pair<Edge<D>, Vertex<D>>>? {
        val NEG_INF = -1000000

        val distance = MutableList(vertices.size) { Int.MAX_VALUE }
        val predecessor = MutableList<Vertex<D>?>(vertices.size) { null }

        distance[srcVertex.id] = 0

        for (i in 0..vertices.size - 1) {
            for (edge in edges) {
                val v1 = edge.vertex1
                val v2 = edge.vertex2

                if (distance[v1.id] != Int.MAX_VALUE && distance[v2.id] > distance[v1.id] + getWeight(edge)) {
                    // distance will equal negative infinity if there is negative cycle
                    distance[v2.id] = maxOf(distance[v1.id] + getWeight(edge), NEG_INF)

                    predecessor[v2.id] = v1
                }
            }
        }

        // check for negative cycles, determine if path to destVertex exists
        for (i in 0..vertices.size - 1) {
            for (edge in edges) {
                val v1 = edge.vertex1
                val v2 = edge.vertex2

                if (distance[v1.id] != Int.MAX_VALUE && distance[v2.id] > distance[v1.id] + getWeight(edge)) {
                    distance[v2.id] = NEG_INF

                }
            }
        }

        // there is a negative cycle on the way, so path doesn't exist
        if (distance[destVertex.id] == NEG_INF) return null

        if (srcVertex == destVertex) return emptyList()

        // reconstruct the path from srcVertex to destVertex
        val path: MutableList<Pair<Edge<D>, Vertex<D>>> = mutableListOf()
        var currentVertex = destVertex
        while (currentVertex != srcVertex) {
            val currentPredecessor = predecessor[currentVertex.id]
                ?: return null // path doesn't exist

            path.add(getEdge(currentPredecessor, currentVertex) to currentVertex)

            currentVertex = currentPredecessor
        }

        return path.reversed()
    }
}
