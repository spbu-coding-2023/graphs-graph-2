package util

import model.abstractGraph.Edge
import model.abstractGraph.Graph
import model.abstractGraph.Vertex
import model.WeightedUndirectedGraph
import model.WeightedDirectedGraph

fun setup(graph: Graph<Int>): Pair<List<Vertex<Int>>, Set<Edge<Int>>> {
    val v0 = graph.addVertex(0)
    val v1 = graph.addVertex(1)
    val v2 = graph.addVertex(2)
    val v3 = graph.addVertex(3)
    val v4 = graph.addVertex(4)

    val defaultVerticesList = listOf(v0, v1, v2, v3, v4)

    val defaultEdgesSet = setOf(
        graph.addEdge(v0, v1),
        graph.addEdge(v1, v2),
        graph.addEdge(v2, v3),
        graph.addEdge(v3, v4),
        graph.addEdge(v4, v1),
        graph.addEdge(v3, v1)
    )

    return defaultVerticesList to defaultEdgesSet
}

fun setupWeightedDirected(graph: WeightedDirectedGraph<Int>): Pair<List<Vertex<Int>>, Set<Edge<Int>>> {
    val v0 = graph.addVertex(0)
    val v1 = graph.addVertex(1)
    val v2 = graph.addVertex(2)
    val v3 = graph.addVertex(3)
    val v4 = graph.addVertex(4)

    val defaultVerticesList = listOf(v0, v1, v2, v3, v4)

    val defaultEdgesSet = setOf(
        graph.addEdge(v0, v1, -3),
        graph.addEdge(v1, v2, -2),
        graph.addEdge(v2, v3, -1),
        graph.addEdge(v3, v4, 1),
        graph.addEdge(v4, v1, 2),
        graph.addEdge(v3, v1, 3)
    )

    return defaultVerticesList to defaultEdgesSet
}

fun setupWeightedUndirected(graph: WeightedUndirectedGraph<Int>): Pair<List<Vertex<Int>>, Set<Edge<Int>>> {
    val v0 = graph.addVertex(0)
    val v1 = graph.addVertex(1)
    val v2 = graph.addVertex(2)
    val v3 = graph.addVertex(3)
    val v4 = graph.addVertex(4)

    val defaultVerticesList = listOf(v0, v1, v2, v3, v4)

    val defaultEdgesSet = setOf(
        graph.addEdge(v0, v1, -3),
        graph.addEdge(v1, v2, -2),
        graph.addEdge(v2, v3, -1),
        graph.addEdge(v3, v4, 1),
        graph.addEdge(v4, v1, 2),
        graph.addEdge(v3, v1, 3)
    )

    return defaultVerticesList to defaultEdgesSet
}