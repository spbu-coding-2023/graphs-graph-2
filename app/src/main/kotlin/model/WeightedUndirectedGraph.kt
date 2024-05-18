package model

import model.abstractGraph.Edge
import model.abstractGraph.Vertex
import java.util.*
import kotlin.NoSuchElementException

class WeightedUndirectedGraph<D> : UndirectedGraph<D>() {
    private val weightMap: MutableMap<Edge<D>, Int> = mutableMapOf()

    fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>, weight: Int): Edge<D> {
        val newEdge = super.addEdge(vertex1, vertex2)

        weightMap[newEdge] = weight

        return newEdge
    }


    // In case weight is not passed, set it to default value = 1
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>) = addEdge(vertex1, vertex2, 1)

    fun getWeight(edge: Edge<D>): Int {
        val weight = weightMap[edge]
            ?: throw NoSuchElementException(
                "No weight found for edge between vertices (${edge.vertex1.id}, ${edge.vertex1.data})" +
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
            val (vertex, currentDistance) = priorityQueue.poll()
            if (visited.add(vertex to currentDistance)) {
                adjacencyMap[vertex]?.forEach { adjacent ->
                    val currentEdge = getEdge(adjacent, vertex)
                    currentEdge.let {
                        var totalDist = currentDistance
                        totalDist += getWeight(it)

                        if (totalDist < distanceMap.getValue(adjacent)) {
                            distanceMap[adjacent] = totalDist
                            predecessorMap[adjacent] = vertex // Update predecessor
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

            val currentEdge = getEdge(predecessor, currentVertex)
            path.add(Pair(currentVertex, currentEdge))

            currentVertex = predecessor
        }
        return path.reversed()
    }

    fun findMinSpanningTree(): List<Edge<D>> {
        val graphSize = vertices.size

        // set each vertex parent to be itself and each vertex rank to 0
        val parentIdList = Array(graphSize) { i: Int -> i }
        val rankList = Array(graphSize) { 0 }

        fun findRootIdByVertexId(vId: Int): Int {
            if (parentIdList[vId] == vId) return vId

            parentIdList[vId] = findRootIdByVertexId(parentIdList[vId])

            return parentIdList[vId]
        }

        fun uniteTwoTreesByVerticesIds(vId1: Int, vId2: Int) {
            val rootId1 = findRootIdByVertexId(vId1)
            val rootId2 = findRootIdByVertexId(vId2)

            if (rootId1 == rootId2) return

            if (rankList[rootId1] < rankList[rootId2]) {
                parentIdList[rootId1] = rootId2
            } else {
                parentIdList[rootId2] = rootId1
                if (rankList[rootId1] == rankList[rootId2]) rankList[rootId1]++
            }
        }

        val edgeWeightPairs = weightMap.toList()
        val sortedEdgeWeightPairs = edgeWeightPairs.sortedBy { it.second }

        val chosenEdges = mutableListOf<Edge<D>>()

        for ((edge, _) in sortedEdgeWeightPairs) {
            val id1 = edge.vertex1.id
            val id2 = edge.vertex2.id

            if (findRootIdByVertexId(id1) != findRootIdByVertexId(id2)) {
                uniteTwoTreesByVerticesIds(id1, id2)
                chosenEdges.add(edge)
            }
        }

        return chosenEdges
    }
}
