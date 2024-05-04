package model.internalGraphs

import model.abstractGraph.Vertex
import model.edges.WeightedDirectedEdge
import java.util.*

abstract class _WeightedDirectedGraph<D, E : WeightedDirectedEdge<D>> : _DirectedGraph<D, E>() {
    fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>, weight: Int): E {
        if (vertex1 == vertex2) throw IllegalArgumentException("Vertices are the same")
        if (vertex1 !in adjacencyMap.keys || vertex2 !in adjacencyMap.keys)
            throw IllegalArgumentException("Vertex1 or vertex2 are not in the graph")

        val newEdge = WeightedDirectedEdge(vertex1, vertex2, weight) as E
        edges.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)

        return newEdge
    }

    /*
     * In case weight is not passed, set it to default value = 1
     */
    override fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>) = addEdge(vertex1, vertex2, 1)

    fun findShortestPath(srcVertex: Vertex<D>, destVertex: Vertex<D>): Map<Vertex<D>, Int> {
        val vertices = getVertices()
        val distanceMap = mutableMapOf<Vertex<D>, Int>().withDefault{ Int.MAX_VALUE }
        val priorityQueue = PriorityQueue<Pair<Vertex<D>, Int>>(compareBy { it.second }).apply { add(destVertex to 0) }
        val visited = mutableSetOf<Pair<Vertex<D>, Int>>()

        distanceMap[srcVertex] = 0

        while (priorityQueue.isNotEmpty()) {
            val (node, currentDistance) = priorityQueue.poll()
            if (visited.add(node to currentDistance)) {
                adjacencyMap[node]?.forEach{ adjacent ->
                    val currentEdge = edges.find { it.vertex1 == adjacent }
                    currentEdge?.let {
                        val totalDist = currentDistance + currentEdge.weight
                        if (totalDist < distanceMap.getValue(adjacent)) {
                            distanceMap[adjacent] = totalDist
                            priorityQueue.add(adjacent to totalDist)
                        }
                    }
                }
            }
        }
        return distanceMap;
    }
}
