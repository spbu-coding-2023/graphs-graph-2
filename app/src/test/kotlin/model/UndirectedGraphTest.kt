package model

import model.abstractGraph.Edge
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import util.annotations.TestAllUndirectedGraphs
import util.setup

class UndirectedGraphTest {
    private lateinit var graph: UndirectedGraph<Int>

    @BeforeEach
    fun setup() {
        graph = UndirectedGraph()
    }

    @Nested
    inner class GetEdgeTest {}

    @Nested
    inner class GetNeighboursTest {}

    @Nested
    inner class GetOutgoingEdgesTest {}

    @Nested
    inner class AddEdgeTest {}

    @Nested
    inner class RemoveEdgeTest {}

    @Nested
    inner class FindBridgesTest {
        @Nested
        inner class `All bridges should be found`() {
            @Test
            fun `if graph has one edge`() {
                val vertex0 = graph.addVertex(0)
                val vertex1 = graph.addVertex(1)

                val expectedBridges = listOf(graph.addEdge(vertex0, vertex1))
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }

            @Test
            fun `if two components are connected via one edge`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)

                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)

                graph.apply {
                    addEdge(v0, v1)
                    addEdge(v0, v2)
                    addEdge(v1, v2)

                    addEdge(v3, v4)
                    addEdge(v3, v5)
                    addEdge(v4, v5)
                }

                val expectedBridges = listOf(graph.addEdge(v0, v3))
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }

            @Test
            fun `if graph is chain-like`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                val e01 = graph.addEdge(v0, v1)
                val e12 = graph.addEdge(v1, v2)
                val e23 = graph.addEdge(v2, v3)

                val expectedBridges = setOf(e01, e12, e23)
                val actualBridges = graph.findBridges().toSet()

                assertEquals(expectedBridges, actualBridges)
            }

            @Test
            fun `if graph is star-like`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                val e01 = graph.addEdge(v0, v1)
                val e02 = graph.addEdge(v0, v2)
                val e03 = graph.addEdge(v0, v3)

                val expectedBridges = setOf(e01, e02, e03)
                val actualBridges = graph.findBridges().toSet()

                assertEquals(expectedBridges, actualBridges)
            }
        }

        @Nested
        inner class `No bridge should be found`() {
            @Test
            fun `if graph has no vertices`() {
                val expectedBridges = listOf<Edge<Int>>()
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }

            @Test
            fun `if graph has no edges`() {
                graph.apply {
                    addVertex(0)
                    addVertex(1)
                    addVertex(2)
                }

                val expectedBridges = listOf<Edge<Int>>()
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }

            @Test
            fun `if graph is circle-like`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)

                graph.apply {
                    addEdge(v0, v1)
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v4)
                    addEdge(v4, v0)
                }

                val expectedBridges = listOf<Edge<Int>>()
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }

            @Test
            fun `if two components are connected via more than one edge`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)

                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)

                graph.apply {
                    addEdge(v0, v1)
                    addEdge(v0, v2)
                    addEdge(v1, v2)

                    addEdge(v3, v4)
                    addEdge(v3, v5)
                    addEdge(v4, v5)
                }

                graph.addEdge(v0, v3)
                graph.addEdge(v1, v4)

                val expectedBridges = listOf<Edge<Int>>()
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }
        }
    }
}
