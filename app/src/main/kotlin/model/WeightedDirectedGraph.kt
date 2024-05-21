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

    // SPFA variant
    fun findShortestPathFordBellman(srcVertex: Vertex<D>, destVertex: Vertex<D>): List<Pair<Vertex<D>, Edge<D>>> {
        val distanceList = MutableList(vertices.size) { Int.MAX_VALUE }
        val queue: Queue<Vertex<D>> = ArrayDeque()
        val isInQueueList = MutableList(vertices.size) { false }
        val parentList = MutableList<Vertex<D>?>(vertices.size) { null }

        distanceList[srcVertex.id] = 0
        queue.add(srcVertex)
        isInQueueList[srcVertex.id] = true

        while (queue.isNotEmpty()) {
            val currentVertex = queue.remove()
            isInQueueList[currentVertex.id] = false

            val currentVertexDistance = distanceList[currentVertex.id]

            for (edge in getOutgoingEdges(currentVertex)) {
                val neighbour = if (currentVertex == edge.vertex1) edge.vertex2 else edge.vertex1

                if (distanceList[neighbour.id] > distanceList[currentVertex.id] + getWeight(edge)) {
                    distanceList[neighbour.id] = distanceList[currentVertex.id] + getWeight(edge)

                    parentList[neighbour.id] = currentVertex

                    if (!isInQueueList[neighbour.id]) {
                        queue.add(neighbour)
                        isInQueueList[neighbour.id] = true
                    }
                }
            }
        }

        // path doesn't exist
        if (parentList[destVertex.id] == null) return emptyList()

        val path: MutableList<Pair<Vertex<D>, Edge<D>>> = mutableListOf()

        var currentVertex = destVertex
        while (currentVertex != srcVertex) {
            val parent = parentList[currentVertex.id]
                ?: throw NoSuchElementException("Vertex (${currentVertex.id}, ${currentVertex.data}) has no parent :(")

            path.add(parent to getEdge(parent, currentVertex))

            currentVertex = parent
        }

        return path.reversed()
    }
}
