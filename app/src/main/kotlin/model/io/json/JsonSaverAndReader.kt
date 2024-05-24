package model.io.json

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.path.Path
import model.DirectedGraph
import model.UndirectedGraph
import model.WeightedDirectedGraph
import model.WeightedUndirectedGraph
import model.abstractGraph.Graph
import kotlin.io.path.readText
import kotlin.io.path.writeText

class JsonSaverAndReader {
    fun <D> saveToJson(graph: Graph<D>, pathString: String) {
        val graphContent = readGraphContent(graph)

        val jsonString = Json.encodeToString(graphContent)

        Path(pathString).writeText(jsonString)
    }

    fun <D> readFromJson(pathString: String): Graph<D> {
        val jsonString = Path(pathString).readText()

        val graphContent = Json.decodeFromString<GraphContent<D>>(jsonString)

        val graph = createGraphFromContent(graphContent)

        return graph
    }

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

                else -> edgesEndsIds[id1 to id2] = null
            }
        }

        val graphContent = GraphContent(
            isDirected,
            isWeighted,
            verticesData,
            edgesEndsIds
        )

        return graphContent
    }

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
