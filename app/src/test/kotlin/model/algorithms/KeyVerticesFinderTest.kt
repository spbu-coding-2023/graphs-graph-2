package model.algorithms

import model.graphs.DirectedGraph
import model.graphs.WeightedDirectedGraph
import model.graphs.WeightedUndirectedGraph
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class KeyVerticesFinderTest {
    val keyVerticesFinder = KeyVerticesFinder()

    @Nested
    inner class `Graph is directed and unweighted` {
        @Nested
        inner class `One vertex is picked over another`() {
            @Test
            fun `if it can reach more vertices`() {
                val graph = DirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v0, v1)
                    addEdge(v0, v2)
                    addEdge(v0, v3)
                    addEdge(v1, v2)
                }

                val expectedResult = setOf(v0)
                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertEquals(expectedResult, actualResult)
            }

            @Test
            fun `if it can reach other vertices with fewer edges`() {
                val graph = DirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v0, v2)
                    addEdge(v0, v3)
                }

                val expectedResult = setOf(v0)
                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Nested
    inner class `Graph is undirected and unweighted` {
        @Nested
        inner class `One vertex is picked over another`() {
            @Test
            fun `if it can reach other vertices with fewer edges`() {
                val graph = DirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v0, v1)
                    addEdge(v0, v2)
                    addEdge(v0, v3)
                }

                val expectedResult = setOf(v0)
                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Nested
    inner class `Graph is directed and weighted` {
        @Nested
        inner class `One vertex is picked over another`() {
            @Test
            fun `if it can reach more vertices`() {
                val graph = WeightedDirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v0, v1, 1)
                    addEdge(v0, v2, 1)
                    addEdge(v0, v3, 1)
                    addEdge(v1, v2, 1)
                }

                val expectedResult = setOf(v0)
                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertEquals(expectedResult, actualResult)
            }

            @Test
            fun `if it can reach other vertices with fewer edges`() {
                val graph = WeightedDirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v0, v2, 1)
                    addEdge(v0, v3, 1)
                    addEdge(v1, v2, 1)
                    addEdge(v2, v3, 1)
                }

                val expectedResult = setOf(v0)
                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertEquals(expectedResult, actualResult)
            }

            @Test
            fun `if its sum of distances to other vertices is less`() {
                val graph = WeightedDirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v0, v2, 1)
                    addEdge(v0, v3, 1)
                    addEdge(v1, v2, 2)
                    addEdge(v2, v3, 2)
                }

                val expectedResult = setOf(v0)
                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertEquals(expectedResult, actualResult)
            }
        }

        @Nested
        inner class `Returns null`() {
            @Test
            fun `if graph has negative edges`() {
                val graph = WeightedDirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)

                graph.addEdge(v0, v1, 1)
                graph.addEdge(v0, v2, -1)

                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertNull(actualResult)
            }
        }
    }

    @Nested
    inner class `Graph is undirected and weighted` {
        @Nested
        inner class `One vertex is picked over another`() {
            @Test
            fun `if it can reach more vertices`() {
                val graph = WeightedUndirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v0, v1, 1)
                    addEdge(v0, v2, 1)
                    addEdge(v0, v3, 1)
                    addEdge(v1, v2, 1)
                }

                val expectedResult = setOf(v0)
                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertEquals(expectedResult, actualResult)
            }

            @Test
            fun `if it can reach other vertices with fewer edges`() {
                val graph = WeightedUndirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v0, v1)
                    addEdge(v0, v2)
                    addEdge(v0, v3)
                }

                val expectedResult = setOf(v0)
                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertEquals(expectedResult, actualResult)
            }

            @Test
            fun `if its sum of distances to other vertices is less`() {
                val graph = WeightedUndirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v0, v2, 1)
                    addEdge(v0, v3, 1)
                    addEdge(v1, v2, 2)
                    addEdge(v2, v3, 2)
                }

                val expectedResult = setOf(v0)
                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertEquals(expectedResult, actualResult)
            }
        }

        @Nested
        inner class `Returns null`() {
            @Test
            fun `if graph has negative edges`() {
                val graph = WeightedUndirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)

                graph.addEdge(v0, v1, 1)
                graph.addEdge(v0, v2, -1)

                val actualResult = keyVerticesFinder.findKeyVertices(graph)

                assertNull(actualResult)
            }
        }
    }
}
