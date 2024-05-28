package model.algorithms

import model.graphs.DirectedGraph
import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Vertex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import util.annotations.TestAllDirectedGraphs

class CyclesFinderTest {
    val cyclesFinder = CyclesFinder()

    @Nested
    inner class `There are some cycles` {
        @TestAllDirectedGraphs
        fun `all cycles should be returned`(graph: DirectedGraph<Int>) {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)
            val v5 = graph.addVertex(5)
            val v6 = graph.addVertex(6)
            val v7 = graph.addVertex(7)
            val v8 = graph.addVertex(8)

            val e01 = graph.addEdge(v0, v1)
            val e07 = graph.addEdge(v0, v7)
            val e04 = graph.addEdge(v0, v4)
            val e18 = graph.addEdge(v1, v8)
            val e12 = graph.addEdge(v1, v2)
            val e20 = graph.addEdge(v2, v0)
            val e21 = graph.addEdge(v2, v1)
            val e25 = graph.addEdge(v2, v5)
            val e23 = graph.addEdge(v2, v3)
            val e53 = graph.addEdge(v5, v3)
            val e34 = graph.addEdge(v3, v4)
            val e41 = graph.addEdge(v4, v1)
            val e78 = graph.addEdge(v7, v8)
            val e87 = graph.addEdge(v8, v7)

            val expectedCycle1 = listOf(e12 to v2, e21 to v1)
            val expectedCycle2 = listOf(e12 to v2, e20 to v0, e01 to v1)
            val expectedCycle3 = listOf(e12 to v2, e20 to v0, e04 to v4, e41 to v1)
            val expectedCycle4 = listOf(e12 to v2, e23 to v3, e34 to v4, e41 to v1)
            val expectedCycle5 = listOf(e12 to v2, e25 to v5, e53 to v3, e34 to v4, e41 to v1)

            val actualValue = cyclesFinder.findCycles(graph, v1)
            val expectedValue =
                setOf(
                    expectedCycle1,
                    expectedCycle2,
                    expectedCycle3,
                    expectedCycle4,
                    expectedCycle5
                )

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `SCC of 2 vertices should have one cycle`(graph: DirectedGraph<Int>) {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)

            val e01 = graph.addEdge(v0, v1)
            val e12 = graph.addEdge(v1, v2)
            val e21 = graph.addEdge(v2, v1)
            val e23 = graph.addEdge(v2, v3)

            val actualValue = cyclesFinder.findCycles(graph, v1)
            val expectedValue = setOf(listOf(e12 to v2, e21 to v1))

            assertEquals(expectedValue, actualValue)
        }
    }

    @Nested
    inner class `There are no cycles` {
        @TestAllDirectedGraphs
        fun `vertex without outgoing edges shouldn't have cycles`(graph: DirectedGraph<Int>) {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)

            graph.addEdge(v0, v1)

            val actualValue = cyclesFinder.findCycles(graph, v1)
            val expectedValue = emptySet<List<Pair<Edge<Int>, Vertex<Int>>>>()

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `SCC of 1 vertex shouldn't have cycles`(graph: DirectedGraph<Int>) {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)

            graph.addEdge(v0, v1)

            val actualValue = cyclesFinder.findCycles(graph, v0)
            val expectedValue = emptySet<List<Pair<Edge<Int>, Vertex<Int>>>>()

            assertEquals(expectedValue, actualValue)
        }
    }

    @TestAllDirectedGraphs
    fun `graph shouldn't change`(graph: DirectedGraph<Int>) {
        val v0 = graph.addVertex(0)
        val v1 = graph.addVertex(1)
        val v2 = graph.addVertex(2)
        val v3 = graph.addVertex(3)
        val v4 = graph.addVertex(4)
        val v5 = graph.addVertex(5)
        val v6 = graph.addVertex(6)
        val v7 = graph.addVertex(7)
        val v8 = graph.addVertex(8)

        graph.apply {
            addEdge(v0, v1)
            addEdge(v0, v7)
            addEdge(v0, v4)
            addEdge(v1, v8)
            addEdge(v1, v2)
            addEdge(v2, v0)
            addEdge(v2, v1)
            addEdge(v2, v5)
            addEdge(v2, v3)
            addEdge(v5, v3)
            addEdge(v3, v4)
            addEdge(v4, v1)
            addEdge(v7, v8)
            addEdge(v8, v7)
        }

        val expectedGraph = graph.getVertices() to graph.getEdges().toSet()

        cyclesFinder.findCycles(graph, v1)

        val actualGraph = graph.getVertices() to graph.getEdges().toSet()

        assertEquals(expectedGraph, actualGraph)
    }
}

