package model.graphs

import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Vertex
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

    override fun getWeightMap() = weightMap.toMap()

    override fun hasNegativeEdges(): Boolean {
        val edgeWeighs = getWeightMap().values

        return edgeWeighs.any { it < 0 }
    }

    fun findShortestPathDijkstra(srcVertex: Vertex<D>, destVertex: Vertex<D>): List<Pair<Edge<D>, Vertex<D>>>? {
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
        val path: MutableList<Pair<Edge<D>, Vertex<D>>> = mutableListOf()
        var currentVertex = destVertex
        while (currentVertex != srcVertex) {
            val predecessor = predecessorMap[currentVertex]
                ?: return null // path doesn't exist

            val currentEdge = getEdge(predecessor, currentVertex)
            path.add(currentEdge to currentVertex)

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
