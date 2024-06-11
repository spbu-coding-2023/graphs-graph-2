package model.algorithms

import model.graphs.WeightedUndirectedGraph
import model.graphs.abstractGraph.Edge

class MinSpanningTreeFinder {
    fun <D> findMinSpanningTree(graph: WeightedUndirectedGraph<D>): List<Edge<D>> {
        val graphSize = graph.getVertices().size

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

        val edgeWeightPairs = graph.getWeightMap().toList()
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
