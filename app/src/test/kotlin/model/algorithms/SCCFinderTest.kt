package model.algorithms

import model.graphs.DirectedGraph
import model.graphs.abstractGraph.Vertex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import util.annotations.TestAllDirectedGraphs

class SCCFinderTest {
    val SCCFinder = SCCFinder()

    @Nested
    inner class `SCC should return not empty array` {
        @TestAllDirectedGraphs
        fun `graph has two connected vertices`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)

            graph.addEdge(v1, v2)
            graph.addEdge(v2, v1)

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v1, v2))

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `complex graph`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v3)
                addEdge(v3, v1)
                addEdge(v3, v4)
            }

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v1, v2, v3), mutableSetOf(v4))

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `graph has multiple SCCs`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)
            val v5 = graph.addVertex(5)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v1)
                addEdge(v3, v4)
                addEdge(v4, v3)
                addEdge(v5, v1)
            }

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v3, v4), mutableSetOf(v1, v2), mutableSetOf(v5))

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `graph with nested cycles`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)
            val v5 = graph.addVertex(5)
            val v6 = graph.addVertex(6)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v3)
                addEdge(v3, v1)
                addEdge(v3, v4)
                addEdge(v4, v5)
                addEdge(v5, v6)
                addEdge(v6, v4)
            }

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v1, v2, v3), mutableSetOf(v4, v5, v6))

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `graph with cross connections`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)
            val v5 = graph.addVertex(5)
            val v6 = graph.addVertex(6)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v3)
                addEdge(v3, v1)
                addEdge(v3, v4)
                addEdge(v4, v5)
                addEdge(v5, v6)
                addEdge(v6, v4)
            }

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v1, v2, v3), mutableSetOf(v4, v5, v6))

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `graph with disconnected subgraphs`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)
            val v5 = graph.addVertex(5)
            val v6 = graph.addVertex(6)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v1)
                addEdge(v3, v4)
                addEdge(v4, v3)
                addEdge(v5, v6)
                addEdge(v6, v5)
            }

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v1, v2), mutableSetOf(v3, v4), mutableSetOf(v5, v6))
            assertEquals(expectedValue, actualValue)
        }

        @Disabled("Our model doesn't support edge from vertex to itself, check DirectedGraph.kt")
        @TestAllDirectedGraphs
        fun `graph with single vertex cycle`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v3)
                addEdge(v3, v1)
                addEdge(v3, v3)
            }

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v1, v2, v3))

            assertEquals(expectedValue, actualValue)
        }
    }

    @Nested
    inner class `SCC should return single-element SCCs` {
        @TestAllDirectedGraphs
        fun `graph has single vertex`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v1))

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `graph with multiple disconnected vertices`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue =
                mutableSetOf(
                    mutableSetOf(v1),
                    mutableSetOf(v2),
                    mutableSetOf(v3),
                    mutableSetOf(v4)
                )

            assertEquals(expectedValue, actualValue)
        }
    }

    @Nested
    inner class `Additional edge cases`() {
        @TestAllDirectedGraphs
        fun `empty graph`(graph: DirectedGraph<Int>) {

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf<MutableSet<Vertex<Int>>>()

            assertEquals(expectedValue, actualValue)
        }

        @Disabled("Our model doesn't support edge from vertex to itself, check DirectedGraph.kt")
        @TestAllDirectedGraphs
        fun `graph with self-loops`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)

            graph.addEdge(v1, v1)
            graph.addEdge(v2, v2)

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v1), mutableSetOf(v2))

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `linear graph`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)

            graph.addEdge(v1, v2)
            graph.addEdge(v2, v3)

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v3), mutableSetOf(v2), mutableSetOf(v1))

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `graph with cycles and tail`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)
            val v5 = graph.addVertex(5)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v3)
                addEdge(v3, v1)
                addEdge(v4, v3)
                addEdge(v4, v5)
            }

            val actualValue = SCCFinder.findSCC(graph)
            val expectedValue = mutableSetOf(mutableSetOf(v1, v2, v3), mutableSetOf(v4), mutableSetOf(v5))

            assertEquals(expectedValue, actualValue)
        }
    }

    @Nested
    inner class `Side-effects check` {
        @TestAllDirectedGraphs
        fun `check vertices in complex graph`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v3)
                addEdge(v3, v1)
                addEdge(v3, v4)
            }

            val expectedValue = graph.getVertices()
            SCCFinder.findSCC(graph)
            val actualValue = graph.getVertices()

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `check edges in complex graph`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v3)
                addEdge(v3, v1)
                addEdge(v3, v4)
            }

            val expectedValue = graph.getEdges()
            SCCFinder.findSCC(graph)
            val actualValue = graph.getEdges()

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `check edges in graph with cycles and tail`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)
            val v5 = graph.addVertex(5)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v3)
                addEdge(v3, v1)
                addEdge(v4, v3)
                addEdge(v4, v5)
            }

            val expectedValue = graph.getEdges()
            SCCFinder.findSCC(graph)
            val actualValue = graph.getEdges()

            assertEquals(expectedValue, actualValue)
        }

        @TestAllDirectedGraphs
        fun `check vertices graph with cycles and tail`(graph: DirectedGraph<Int>) {
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)
            val v5 = graph.addVertex(5)

            graph.apply {
                addEdge(v1, v2)
                addEdge(v2, v3)
                addEdge(v3, v1)
                addEdge(v4, v3)
                addEdge(v4, v5)
            }

            val expectedValue = graph.getVertices()
            SCCFinder.findSCC(graph)
            val actualValue = graph.getVertices()

            assertEquals(expectedValue, actualValue)
        }
    }
}

