package model.algorithms.clustering

import model.WeightedDirectedGraph
import model.WeightedUndirectedGraph
import model.abstractGraph.Graph
import model.abstractGraph.Vertex
import model.algorithms.clustering.implementation.Link
import model.algorithms.clustering.implementation.getPartition

class LouvainClustering {
    fun <D> findClusters(graph: Graph<D>): Set<Set<Vertex<D>>> {
        val links = convertToAPIFormat(graph)

        val result = getPartition(links)

        return convertResult(result, graph)
    }

    private fun <D> convertToAPIFormat(graph: Graph<D>): List<Link> {
        val links = mutableListOf<Link>()

        for (edge in graph.getEdges()) {
            val id1 = edge.vertex1.id
            val id2 = edge.vertex2.id

            val weight: Double =
                when (graph) {
                    is WeightedUndirectedGraph -> graph.getWeight(edge).toDouble()
                    is WeightedDirectedGraph -> graph.getWeight(edge).toDouble()
                    else -> 1.0
                }

            links += EdgeLink(id1, id2, weight)
        }

        return links
    }

    private fun <D> convertResult(resultMap: Map<Int, Int>, graph: Graph<D>): Set<Set<Vertex<D>>> {
        val vertices = graph.getVertices()
        val communities = mutableSetOf<Set<Vertex<D>>>()

        var currentCommunityId = 0
        var currentCommunity = mutableSetOf<Vertex<D>>()

        for (vertexId in resultMap.keys) {
            if (resultMap[vertexId] == currentCommunityId) currentCommunity += vertices[vertexId]
            else {
                communities += currentCommunity

                currentCommunityId++
                currentCommunity = mutableSetOf()

                currentCommunity += vertices[vertexId]
            }
        }

        return communities
    }

    inner class EdgeLink(private val vertex1Id: Int, private val vertex2Id: Int, val weight: Double) : Link {
        override fun source() = vertex1Id
        override fun target() = vertex2Id
        override fun weight() = weight
    }
}
