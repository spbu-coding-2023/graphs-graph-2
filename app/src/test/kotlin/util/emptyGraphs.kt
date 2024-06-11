package util

import model.graphs.DirectedGraph
import model.graphs.UndirectedGraph
import model.graphs.WeightedDirectedGraph
import model.graphs.WeightedUndirectedGraph
import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Vertex

val emptyDirectedGraph = DirectedGraph<Int>()
val emptyUndirectedGraph = UndirectedGraph<Int>()

val emptyWDGrapgh = WeightedDirectedGraph<Int>()
val emptyWUGrapgh = WeightedUndirectedGraph<Int>()

val emptyVerticesList = listOf<Vertex<Int>>()
val emptyEdgesSet = setOf<Edge<Int>>()

val emptyGraph = emptyVerticesList to emptyEdgesSet