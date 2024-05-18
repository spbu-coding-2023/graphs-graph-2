package model

import model.abstractGraph.Vertex
import org.junit.jupiter.api.Nested
import util.annotations.TestAllDirectedGraphs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.assertThrows
import util.setup

class DirectedGraphTest {
    @Nested
    inner class GetEdgeTest {
        @Nested
        inner class `Edge is in the graph` {
            @TestAllDirectedGraphs
            fun `edge should be returned`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v2 = defaultVerticesList[2]

                val expectedValue = graph.addEdge(v0, v2)
                val actualValue = graph.getEdge(v0, v2)

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `graph shouldn't change`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v2 = defaultVerticesList[2]
                val v3 = defaultVerticesList[3]

                graph.getEdge(v2, v3)

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = graphStructure

                assertEquals(expectedGraph, actualGraph)
            }
        }

        @Nested
        inner class `Edge isn't in the graph` {
            @TestAllDirectedGraphs
            fun `order of arguments should matter`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v1 = defaultVerticesList[1]

                assertThrows(NoSuchElementException::class.java) {
                    graph.getEdge(v1, v0)
                }
            }

            @TestAllDirectedGraphs
            fun `getting non-existent edge should throw an exception`(graph: DirectedGraph<Int>) {
                assertThrows(NoSuchElementException::class.java) {
                    graph.getEdge(Vertex(2, 12), Vertex(85, 6))
                }
            }
        }
    }

    @Nested
    inner class GetNeighboursTest {}

    @Nested
    inner class GetOutgoingEdgesTest {}

    @Nested
    inner class AddEdgeTest {}

    @Nested
    inner class RemoveEdgeTest {}

    @Nested
    inner class FindSCCTest {}
}
