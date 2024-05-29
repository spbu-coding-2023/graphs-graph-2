package model.algorithms

import model.graphs.DirectedGraph
import model.graphs.WeightedDirectedGraph
import model.graphs.WeightedUndirectedGraph
import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Vertex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import util.setupAbstractGraph
import util.setupDirectedGraphWithCycle
import util.setupWeightedUndirected

class ShortestPathFinderTest {
    val shortestPathFinder = ShortestPathFinder()

    @Nested
    inner class `Graph is weighted and directed` {
        private lateinit var graph: WeightedDirectedGraph<Int>

        @BeforeEach
        fun init() {
            graph = WeightedDirectedGraph<Int>()
        }

        @Nested
        inner class `There are no negative weights (Dijkstra)` {
            @Nested
            inner class `Normal path should be returned`() {
                @Test
                fun `all is as usual, should return default`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)
                    val v3 = graph.addVertex(3)
                    val v4 = graph.addVertex(4)

                    val e0 = graph.addEdge(v0, v1, 10)
                    val e1 = graph.addEdge(v0, v4, 100)
                    val e2 = graph.addEdge(v0, v3, 30)
                    val e3 = graph.addEdge(v1, v2, 2)
                    val e4 = graph.addEdge(v2, v4, 10)
                    val e5 = graph.addEdge(v3, v2, 20)
                    val e6 = graph.addEdge(v3, v4, 60)

                    val expectedResult = listOf(e0 to v1, e3 to v2, e4 to v4)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v4)

                    assertEquals(expectedResult, actualResult)
                }

                @Test
                fun `direct path should be the shortest in directed graph`() {
                    val graph = WeightedDirectedGraph<Int>()
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)

                    val e0 = graph.addEdge(v0, v1, 5)
                    val e1 = graph.addEdge(v1, v2, 5)
                    val e2 = graph.addEdge(v0, v2, 10)

                    val expectedResult = listOf(e2 to v2)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v2)
                    assertEquals(expectedResult, actualResult)
                }

                @Test
                fun `if graph has multiple paths and equal weights`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)
                    val v3 = graph.addVertex(3)

                    val e0 = graph.addEdge(v0, v1, 1)
                    val e1 = graph.addEdge(v0, v2, 1)
                    val e2 = graph.addEdge(v1, v3, 1)
                    val e3 = graph.addEdge(v2, v3, 1)

                    val expectedResult1 = listOf(e0 to v1, e2 to v3)
                    val expectedResult2 = listOf(e1 to v2, e3 to v3)

                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v3)

                    assertTrue(actualResult == expectedResult1 || actualResult == expectedResult2)
                }

                @Test
                fun `if graph has single edge`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)

                    val e0 = graph.addEdge(v0, v1, 5)

                    val expectedResult = listOf(e0 to v1)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v1)

                    assertEquals(expectedResult, actualResult)
                }

                @Disabled("Dijkstra's algorithm doesn't work with negative weights")
                @Test
                fun `if graph has negative weights`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)
                    val v3 = graph.addVertex(3)

                    val e0 = graph.addEdge(v0, v1, -1)
                    val e1 = graph.addEdge(v1, v2, -2)
                    val e2 = graph.addEdge(v2, v3, -3)

                    val expectedResult = listOf(e0 to v1, e1 to v2, e2 to v3)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v3)

                    assertEquals(expectedResult, actualResult)
                }

                @Test
                fun `graph has multiple equal shortest paths`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)
                    val v3 = graph.addVertex(3)
                    val v4 = graph.addVertex(4)

                    val e0 = graph.addEdge(v0, v1, 1)
                    val e1 = graph.addEdge(v0, v2, 1)
                    val e2 = graph.addEdge(v1, v3, 1)
                    val e3 = graph.addEdge(v2, v3, 1)
                    val e4 = graph.addEdge(v3, v4, 1)

                    val expectedResult1 = listOf(e0 to v1, e2 to v3, e4 to v4)
                    val expectedResult2 = listOf(e1 to v2, e3 to v3, e4 to v4)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v4)

                    assertTrue(actualResult == expectedResult1 || actualResult == expectedResult2)
                }

                @Test
                fun `if graph has a cycle`() {
                    val graph = WeightedUndirectedGraph<Int>()
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)

                    val e0 = graph.addEdge(v0, v1, 1)
                    val e1 = graph.addEdge(v1, v2, 2)
                    val e2 = graph.addEdge(v2, v0, 3)

                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v2)
                    val expectedResult = listOf(e2 to v2)

                    assertEquals(expectedResult, actualResult)
                }

                @Test
                fun `if all the edges have zero weight in directed graph`() {
                    val graph = WeightedDirectedGraph<Int>()
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)

                    val e0 = graph.addEdge(v0, v1, 0)
                    val e1 = graph.addEdge(v1, v2, 0)

                    val expectedResult = listOf(e0 to v1, e1 to v2)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v2)
                    assertEquals(expectedResult, actualResult)
                }
            }

            @Nested
            inner class `No path should be returned`() {
                @Test
                fun `no path exists in directed graph`() {
                    val graph = WeightedDirectedGraph<Int>()
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)
                    val v3 = graph.addVertex(3)

                    graph.addEdge(v0, v1, 10)
                    graph.addEdge(v1, v2, 20)

                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v3)
                    assertEquals(actualResult, null)
                }

                @Test
                fun `if start and end vertices are the same`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)

                    graph.apply {
                        addEdge(v0, v1, 1)
                        addEdge(v1, v2, 2)
                        addEdge(v2, v0, 2)
                    }

                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v0)

                    actualResult?.isEmpty()?.let { assertTrue(it) }
                }

                @Test
                fun `if graph has single vertex`() {
                    val v0 = graph.addVertex(0)

                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v0)

                    actualResult?.isEmpty()?.let { assertTrue(it) }
                }

                @Test
                fun `if path is in other way (not how edges were set)`() {
                    val graph = WeightedDirectedGraph<Int>()
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)

                    graph.addEdge(v0, v1, 0)
                    graph.addEdge(v1, v2, 0)

                    val actualResult = shortestPathFinder.findShortestPath(graph, v2, v0)

                    actualResult?.isEmpty()?.let { assertTrue(it) }
                }
            }
        }

        @Nested
        inner class `There are negative weights (Ford-Bellman)` {
            @Nested
            inner class `Path exists` {
                @Test
                fun `path between neighbours should consist of one edge`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val edge = graph.addEdge(v0, v1, 12345)

                    val actualValue = shortestPathFinder.findShortestPath(graph, v0, v1)
                    val expectedValue = listOf(edge to v1)

                    assertEquals(expectedValue, actualValue)
                }

                @Test
                fun `shortest path should be returned`() {
                    val graphStructure = setupDirectedGraphWithCycle(graph)
                    val defaultVertices = graphStructure.first

                    val v0 = defaultVertices[0]
                    val v1 = defaultVertices[1]
                    val v2 = defaultVertices[2]
                    val v4 = defaultVertices[4]

                    val actualValue = shortestPathFinder.findShortestPath(graph, v0, v4)
                    val expectedValue =
                        listOf(
                            graph.getEdge(v0, v1) to v1,
                            graph.getEdge(v1, v2) to v2,
                            graph.getEdge(v2, v4) to v4
                        )

                    assertEquals(expectedValue, actualValue)
                }

                @Test
                fun `path from vertex to itself should exist and be empty`() {
                    val v0 = graph.addVertex(69)

                    val actualValue = shortestPathFinder.findShortestPath(graph, v0, v0)
                    val expectedValue = emptyList<Pair<Edge<Int>, Vertex<Int>>>()

                    assertEquals(expectedValue, actualValue)
                }

                @Test
                fun `graph shouldn't change`() {
                    val graphStructure = setupDirectedGraphWithCycle(graph)
                    val defaultVertices = graphStructure.first

                    val v3 = defaultVertices[3]
                    val v4 = defaultVertices[4]

                    val expectedGraph = graphStructure
                    shortestPathFinder.findShortestPath(graph, v3, v4)
                    val actualGraph = graphStructure

                    assertEquals(expectedGraph, actualGraph)
                }
            }

            @Nested
            inner class `Path doesn't exist` {
                @Test
                fun `there is simply no path between vertices`() {
                    val graphStructure = setupDirectedGraphWithCycle(graph)
                    val defaultVertices = graphStructure.first

                    val v1 = defaultVertices[1]
                    val v5 = defaultVertices[5]

                    val actualValue = shortestPathFinder.findShortestPath(graph, v1, v5)

                    assertNull(actualValue)
                }

                @Test
                fun `order of arguments should matter`() {
                    val graphStructure = setupDirectedGraphWithCycle(graph)
                    val defaultVertices = graphStructure.first

                    val v0 = defaultVertices[0]
                    val v2 = defaultVertices[2]

                    val actualValue = shortestPathFinder.findShortestPath(graph, v2, v0)

                    assertNull(actualValue)
                }

                @Test
                fun `there is a negative cycle on the path`() {
                    val graphStructure = setupDirectedGraphWithCycle(graph)
                    val defaultVertices = graphStructure.first

                    val v0 = defaultVertices[0]
                    val v8 = defaultVertices[8]

                    val actualValue = shortestPathFinder.findShortestPath(graph, v0, v8)

                    assertNull(actualValue)
                }

                @Test
                fun `srcVertex is a part of negative cycle`() {
                    val graphStructure = setupDirectedGraphWithCycle(graph)
                    val defaultVertices = graphStructure.first

                    val v6 = defaultVertices[6]
                    val v8 = defaultVertices[8]

                    val actualValue = shortestPathFinder.findShortestPath(graph, v6, v8)

                    assertNull(actualValue)
                }

                @Test
                fun `vertex without outgoing edges shouldn't have any paths`() {
                    val graphStructure = setupDirectedGraphWithCycle(graph)
                    val defaultVertices = graphStructure.first

                    val v0 = defaultVertices[0]
                    val v1 = defaultVertices[1]
                    val v2 = defaultVertices[2]
                    val v3 = defaultVertices[3]
                    val v4 = defaultVertices[4]
                    val v5 = defaultVertices[5]
                    val v6 = defaultVertices[6]
                    val v7 = defaultVertices[7]
                    val v8 = defaultVertices[8]

                    assertNull(shortestPathFinder.findShortestPath(graph, v8, v0))
                    assertNull(shortestPathFinder.findShortestPath(graph, v8, v1))
                    assertNull(shortestPathFinder.findShortestPath(graph, v8, v2))
                    assertNull(shortestPathFinder.findShortestPath(graph, v8, v3))
                    assertNull(shortestPathFinder.findShortestPath(graph, v8, v4))
                    assertNull(shortestPathFinder.findShortestPath(graph, v8, v5))
                    assertNull(shortestPathFinder.findShortestPath(graph, v8, v6))
                    assertNull(shortestPathFinder.findShortestPath(graph, v8, v7))
                }

                @Test
                fun `graph shouldn't change`() {
                    val graphStructure = setupDirectedGraphWithCycle(graph)
                    val defaultVertices = graphStructure.first

                    val v1 = defaultVertices[1]
                    val v8 = defaultVertices[8]

                    val expectedGraph = graphStructure
                    shortestPathFinder.findShortestPath(graph, v8, v1)
                    val actualGraph = graphStructure

                    assertEquals(expectedGraph, actualGraph)
                }
            }
        }
    }

    @Nested
    inner class `Graph is weighted and undirected` {
        private lateinit var graph: WeightedUndirectedGraph<Int>

        @BeforeEach
        fun init() {
            graph = WeightedUndirectedGraph<Int>()
        }

        @Nested
        inner class `There are no negative weights (Dijkstra)` {
            @Nested
            inner class `Normal path should be returned` {
                @Test
                fun `all is as usual, should return default`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)
                    val v3 = graph.addVertex(3)
                    val v4 = graph.addVertex(4)

                    val e0 = graph.addEdge(v0, v1, 10)
                    val e1 = graph.addEdge(v0, v4, 100)
                    val e2 = graph.addEdge(v0, v3, 30)
                    val e3 = graph.addEdge(v1, v2, 2)
                    val e4 = graph.addEdge(v2, v4, 10)
                    val e5 = graph.addEdge(v3, v2, 20)
                    val e6 = graph.addEdge(v3, v4, 60)

                    val expectedResult = listOf(e0 to v1, e3 to v2, e4 to v4)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v4)

                    assertEquals(expectedResult, actualResult)
                }

                @Test
                fun `if graph has multiple paths and equal weights`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)
                    val v3 = graph.addVertex(3)

                    val e0 = graph.addEdge(v0, v1, 1)
                    val e1 = graph.addEdge(v0, v2, 1)
                    val e2 = graph.addEdge(v1, v3, 1)
                    val e3 = graph.addEdge(v2, v3, 1)

                    val expectedResult1 = listOf(e0 to v1, e2 to v3)
                    val expectedResult2 = listOf(e1 to v2, e3 to v3)

                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v3)

                    assertTrue(actualResult == expectedResult1 || actualResult == expectedResult2)
                }

                @Test
                fun `if graph has single edge`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)

                    val e0 = graph.addEdge(v0, v1, 5)

                    val expectedResult = listOf(e0 to v1)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v1)

                    assertEquals(expectedResult, actualResult)
                }

                @Disabled("Dijkstra's algorithm doesn't work with negative weights")
                @Test
                fun `if graph has negative weights`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)
                    val v3 = graph.addVertex(3)

                    val e0 = graph.addEdge(v0, v1, -1)
                    val e1 = graph.addEdge(v1, v2, -2)
                    val e2 = graph.addEdge(v2, v3, -3)

                    val expectedResult = listOf(e0 to v1, e1 to v2, e2 to v3)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v3)

                    assertEquals(expectedResult, actualResult)
                }

                @Test
                fun `graph has multiple equal shortest paths`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)
                    val v3 = graph.addVertex(3)
                    val v4 = graph.addVertex(4)

                    val e0 = graph.addEdge(v0, v1, 1)
                    val e1 = graph.addEdge(v0, v2, 1)
                    val e2 = graph.addEdge(v1, v3, 1)
                    val e3 = graph.addEdge(v2, v3, 1)
                    val e4 = graph.addEdge(v3, v4, 1)

                    val expectedResult1 = listOf(e0 to v1, e2 to v3, e4 to v4)
                    val expectedResult2 = listOf(e1 to v2, e3 to v3, e4 to v4)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v4)

                    assertTrue(actualResult == expectedResult1 || actualResult == expectedResult2)
                }

                @Test
                fun `if graph has a cycle`() {
                    val graph = WeightedUndirectedGraph<Int>()
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)

                    val e0 = graph.addEdge(v0, v1, 1)
                    val e1 = graph.addEdge(v1, v2, 2)
                    val e2 = graph.addEdge(v2, v0, 3)

                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v2)
                    val expectedResult = listOf(e2 to v2)

                    assertEquals(expectedResult, actualResult)
                }

                @Test
                fun `if path is in other way (not how edges were set)`() {
                    val graph = WeightedUndirectedGraph<Int>()
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)

                    val e0 = graph.addEdge(v0, v1, 0)
                    val e1 = graph.addEdge(v1, v2, 0)

                    val expectedResult = listOf(e1 to v1, e0 to v0)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v2, v0)

                    assertEquals(expectedResult, actualResult)
                }

                @Test
                fun `if all the edges have zero weight in undirected graph`() {
                    val graph = WeightedUndirectedGraph<Int>()
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)

                    val e0 = graph.addEdge(v0, v1, 0)
                    val e1 = graph.addEdge(v1, v2, 0)

                    val expectedResult = listOf(e0 to v1, e1 to v2)
                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v2)

                    assertEquals(expectedResult, actualResult)
                }
            }

            @Nested
            inner class `No path should be returned`() {
                @Test
                fun `no path exists in undirected graph`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)
                    val v3 = graph.addVertex(3)

                    graph.addEdge(v0, v1, 1)
                    graph.addEdge(v1, v2, 2)

                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v3)

                    assertNull(actualResult)
                }

                @Test
                fun `if start and end vertices are the same`() {
                    val v0 = graph.addVertex(0)
                    val v1 = graph.addVertex(1)
                    val v2 = graph.addVertex(2)

                    graph.addEdge(v0, v1, 1)
                    graph.addEdge(v1, v2, 2)

                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v0)

                    actualResult?.isEmpty()?.let { assertTrue(it) }
                }

                @Test
                fun `if graph has single vertex`() {
                    val v0 = graph.addVertex(0)

                    val actualResult = shortestPathFinder.findShortestPath(graph, v0, v0)

                    actualResult?.isEmpty()?.let { assertTrue(it) }
                }
            }
        }

        @Nested
        inner class `There are negative weights` {
            @Test
            fun `shortest path shouldn't exist (null should be returned)`() {
                val graphStructure = setupWeightedUndirected(graph)
                val defaultVertices = graphStructure.first

                val v1 = defaultVertices[1]
                val v4 = defaultVertices[4]

                assertNull(shortestPathFinder.findShortestPath(graph, v1, v4))
            }
        }
    }

    @Nested
    inner class `Graph is unweighted` {
        @Test
        fun `directed unweighted graphs aren't supported`() {
            val graph = DirectedGraph<Int>()
            val graphStructure = setupAbstractGraph(graph)
            val defaultVertices = graphStructure.first

            val v0 = defaultVertices[0]
            val v2 = defaultVertices[2]

            assertNull(shortestPathFinder.findShortestPath(graph, v0, v2))
        }

        @Test
        fun `undirected unweighted graphs aren't supported`() {
            val graph = DirectedGraph<Int>()
            val graphStructure = setupAbstractGraph(graph)
            val defaultVertices = graphStructure.first

            val v1 = defaultVertices[1]
            val v3 = defaultVertices[3]

            assertNull(shortestPathFinder.findShortestPath(graph, v1, v3))
        }
    }
}
