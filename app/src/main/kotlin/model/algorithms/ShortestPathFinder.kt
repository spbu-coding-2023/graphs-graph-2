package model.algorithms

import model.graphs.WeightedDirectedGraph
import model.graphs.WeightedUndirectedGraph
import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Graph
import model.graphs.abstractGraph.Vertex
import java.util.*

class ShortestPathFinder {
    fun <D> findShortestPath(graph: Graph<D>, srcVertex: Vertex<D>, destVertex: Vertex<D>):
            List<Pair<Edge<D>, Vertex<D>>>? {
        if (graph.hasNegativeEdges()) {
            // there is no shortest path in undirected graphs with negative edges
            if (graph is WeightedUndirectedGraph) return null
            else return findShortestPathFordBellman(graph as WeightedDirectedGraph, srcVertex, destVertex)
        }

        if (graph is WeightedDirectedGraph) return findShortestPathDijkstra(graph, srcVertex, destVertex)
        if (graph is WeightedUndirectedGraph) return findShortestPathDijkstra(graph, srcVertex, destVertex)

        // if graph isn't weighted
        return null
    }

    private fun <D> findShortestPathDijkstra(
        graph: WeightedDirectedGraph<D>,
        srcVertex: Vertex<D>,
        destVertex: Vertex<D>
    ): List<Pair<Edge<D>, Vertex<D>>>? {
        val distanceMap = mutableMapOf<Vertex<D>, Int>().withDefault { Int.MAX_VALUE }
        val predecessorMap = mutableMapOf<Vertex<D>, Vertex<D>?>()
        val priorityQueue = PriorityQueue<Pair<Vertex<D>, Int>>(compareBy { it.second }).apply { add(srcVertex to 0) }
        val visited = mutableSetOf<Pair<Vertex<D>, Int>>()

        if (srcVertex == destVertex) return emptyList()

        distanceMap[srcVertex] = 0

        while (priorityQueue.isNotEmpty()) {
            val (currentVertex, currentDistance) = priorityQueue.poll()
            if (visited.add(currentVertex to currentDistance)) {
                graph.getNeighbours(currentVertex).forEach { adjacent ->
                    val currentEdge = graph.getEdge(currentVertex, adjacent) // Ensure correct edge direction

                    val totalDist = currentDistance + graph.getWeight(currentEdge)

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

            val currentEdge = graph.getEdge(predecessor, currentVertex)
            path.add(currentEdge to currentVertex)

            currentVertex = predecessor
        }
        return path.reversed()
    }

    private fun <D> findShortestPathDijkstra(
        graph: WeightedUndirectedGraph<D>,
        srcVertex: Vertex<D>,
        destVertex: Vertex<D>
    ): List<Pair<Edge<D>, Vertex<D>>>? {
        val distanceMap = mutableMapOf<Vertex<D>, Int>().withDefault { Int.MAX_VALUE }
        val predecessorMap = mutableMapOf<Vertex<D>, Vertex<D>?>()
        val priorityQueue = PriorityQueue<Pair<Vertex<D>, Int>>(compareBy { it.second }).apply { add(srcVertex to 0) }
        val visited = mutableSetOf<Pair<Vertex<D>, Int>>()

        if (srcVertex == destVertex) return emptyList()

        distanceMap[srcVertex] = 0

        while (priorityQueue.isNotEmpty()) {
            val (currentVertex, currentDistance) = priorityQueue.poll()
            if (visited.add(currentVertex to currentDistance)) {
                graph.getNeighbours(currentVertex).forEach { adjacent ->
                    val currentEdge = graph.getEdge(currentVertex, adjacent) // Ensure correct edge direction

                    val totalDist = currentDistance + graph.getWeight(currentEdge)

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

            val currentEdge = graph.getEdge(predecessor, currentVertex)
            path.add(currentEdge to currentVertex)

            currentVertex = predecessor
        }
        return path.reversed()
    }

    // returns null if path doesn't exist
    private fun <D> findShortestPathFordBellman(
        graph: WeightedDirectedGraph<D>,
        srcVertex: Vertex<D>,
        destVertex: Vertex<D>
    ): List<Pair<Edge<D>, Vertex<D>>>? {
        val NEG_INF = -1000000

        val vertices = graph.getVertices()
        val edges = graph.getEdges()

        val distance = MutableList(vertices.size) { Int.MAX_VALUE }
        val predecessor = MutableList<Vertex<D>?>(vertices.size) { null }

        distance[srcVertex.id] = 0

        for (i in 0..vertices.size - 1) {
            for (edge in edges) {
                val v1 = edge.vertex1
                val v2 = edge.vertex2

                if (distance[v1.id] != Int.MAX_VALUE && distance[v2.id] > distance[v1.id] + graph.getWeight(edge)) {
                    // distance will equal negative infinity if there is negative cycle
                    distance[v2.id] = maxOf(distance[v1.id] + graph.getWeight(edge), NEG_INF)

                    predecessor[v2.id] = v1
                }
            }
        }

        // check for negative cycles, determine if path to destVertex exists
        for (i in 0..vertices.size - 1) {
            for (edge in edges) {
                val v1 = edge.vertex1
                val v2 = edge.vertex2

                if (distance[v1.id] != Int.MAX_VALUE && distance[v2.id] > distance[v1.id] + graph.getWeight(edge)) {
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

            path.add(graph.getEdge(currentPredecessor, currentVertex) to currentVertex)

            currentVertex = currentPredecessor
        }

        return path.reversed()
    }
}
