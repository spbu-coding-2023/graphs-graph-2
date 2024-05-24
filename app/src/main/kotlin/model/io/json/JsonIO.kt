package model.io.json

import model.DirectedGraph
import model.UndirectedGraph
import model.WeightedDirectedGraph
import model.WeightedUndirectedGraph
import model.abstractGraph.Graph

class JsonIO {
    fun saveToJson() {}

    private fun <D> readGraphContent(graph: Graph<D>): GraphContent<D> {
        val isDirected = graph is DirectedGraph
        val isWeighted = graph is WeightedDirectedGraph || graph is WeightedUndirectedGraph

        val vertices = graph.getVertices()
        val edges = graph.getEdges()

        val verticesData = mutableListOf<D>()
        val edgesEndsIds = mutableMapOf<Pair<Int, Int>, Int?>()

        for (vertex in vertices) verticesData += vertex.data

        for (edge in edges) {
            val id1 = edge.vertex1.id
            val id2 = edge.vertex2.id

            when (graph) {
                is WeightedDirectedGraph -> {
                    edgesEndsIds[id1 to id2] = graph.getWeight(edge)
                }

                is WeightedUndirectedGraph -> {
                    edgesEndsIds[id1 to id2] = graph.getWeight(edge)
                }

                else -> edgesEndsIds[id1 to id2]
            }
        }

        val graphContent = GraphContent<D>(
            isDirected,
            isWeighted,
            verticesData,
            edgesEndsIds
        )

        return graphContent
    }

    fun readFromJson() {}

    private fun <D> createGraphFromContent(graphContent: GraphContent<D>): Graph<D> {
        val isDirected = graphContent.isDirected
        val isWeighted = graphContent.isWeighted

        val graph: Graph<D> =
            if (isDirected && !isWeighted) DirectedGraph<D>()
            else if (isDirected && isWeighted) WeightedDirectedGraph<D>()
            else if (!isDirected && !isWeighted) UndirectedGraph<D>()
            else WeightedUndirectedGraph<D>()

        for (data in graphContent.verticesData) graph.addVertex(data)

        val vertices = graph.getVertices()

        for (edgeEndsIds in graphContent.edgesEndsIds) {
            val id1 = edgeEndsIds.key.first
            val id2 = edgeEndsIds.key.second
            val weight = edgeEndsIds.value

            when (graph) {
                is WeightedDirectedGraph -> {
                    weight?.let { graph.addEdge(vertices[id1], vertices[id2], weight) }
                }

                is WeightedUndirectedGraph -> {
                    weight?.let { graph.addEdge(vertices[id1], vertices[id2], weight) }
                }

                else -> graph.addEdge(vertices[id1], vertices[id2])
            }
        }

        return graph
    }
}
