package util

import model.DirectedGraph
import model.UndirectedGraph
import model.WeightedDirectedGraph
import model.WeightedUndirectedGraph
import model.abstractGraph.Edge
import model.abstractGraph.Vertex

val emptyDirectedGraph = DirectedGraph<Int>()
val emptyUndirectedGrap = UndirectedGraph<Int>()

val emptyWDGrapgh = WeightedDirectedGraph<Int>()
val emptyWUGrapgh = WeightedUndirectedGraph<Int>()

val emptyVerticesList = listOf<Vertex<Int>>()
val emptyEdgesSet = setOf<Edge<Int>>()

val emptyGraph = emptyVerticesList to emptyEdgesSet