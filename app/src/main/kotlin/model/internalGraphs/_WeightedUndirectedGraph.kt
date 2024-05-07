package model.internalGraphs

import model.abstractGraph.Vertex
import model.edges.WeightedUndirectedEdge
import java.util.*

abstract class _WeightedUndirectedGraph<D, E : WeightedUndirectedEdge<D>> : _UndirectedGraph<D, E>() {
    fun addEdge(vertex1: Vertex<D>, vertex2: Vertex<D>, weight: Int): E {
        if (vertex1 == vertex2) throw IllegalArgumentException("Vertices are the same")
        if (vertex1 !in adjacencyMap.keys || vertex2 !in adjacencyMap.keys)
            throw IllegalArgumentException("Vertex1 or vertex2 are not in the graph")

        val newEdge = WeightedUndirectedEdge(vertex1, vertex2, weight) as E
        edges.add(newEdge)
        adjacencyMap[vertex1]?.add(vertex2)
        adjacencyMap[vertex2]?.add(vertex1)

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
                    val currentEdge = edges.find { (it.vertex1 == adjacent && it.vertex2 == node) ||
                            (it.vertex1 == node && it.vertex2 == adjacent) }
                    currentEdge?.let {
                        val totalDist = currentDistance + currentEdge.weight
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
            if (edges.find { it.vertex1 == predecessor && it.vertex2 == currentVertex ||
                        it.vertex2 == predecessor && it.vertex1 == currentVertex } == null) {
                throw IllegalArgumentException("Edge is not in the graph, path cannot be reconstructed.")
            }
            path.add(Pair(currentVertex, edges.find { it.vertex1 == predecessor && it.vertex2 == currentVertex  ||
                    it.vertex2 == predecessor && it.vertex1 == currentVertex}) as Pair<Vertex<D>, E>)
            currentVertex = predecessor
        }
        return path.reversed()
    }

    fun findMinSpanningTree(): List<E> {
        val vertexIdList = mutableListOf<Int>()
        for (v in getVertices()) vertexIdList.add(v.id)

        val graphSize = vertexIdList.size

        // set each vertex parent to be itself and each vertex rank to 0
        val parentList = MutableList(graphSize) { index: Int -> vertexIdList[index] }
        val rankList = MutableList(graphSize) { 0 }

        fun findTreeRootId(vId: Int): Int {
            if (parentList[vId] == vId) return vId

            parentList[vId] = findTreeRootId(parentList[vId])

            return parentList[vId]
        }

        fun uniteTwoTreesByVerticesIds(vId1: Int, vId2: Int) {
            val rootId1 = findTreeRootId(vId1)
            val rootId2 = findTreeRootId(vId2)

            if (rootId1 == rootId2) return

            if (rankList[rootId1] < rankList[rootId2]) {
                parentList[rootId1] = rootId2
            } else {
                parentList[rootId2] = rootId1
                if (rankList[rootId1] == rankList[rootId2]) rankList[rootId1]++
            }
        }

        val sortedEdges = edges.sortedBy { it.weight }

        val chosenEdges = mutableListOf<E>()

        for (edge in sortedEdges) {
            val id1 = edge.vertex1.id
            val id2 = edge.vertex2.id

            if (findTreeRootId(id1) != findTreeRootId(id2)) {
                uniteTwoTreesByVerticesIds(id1, id2)
                chosenEdges.add(edge)
            }
        }

        return chosenEdges
    }
}
