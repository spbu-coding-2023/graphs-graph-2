package model

import model.abstractGraph.Edge
import model.abstractGraph.Vertex
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import util.setupWeightedDirected

class WeightedDirectedGraphTest {
    private lateinit var graph: WeightedDirectedGraph<Int>

    @BeforeEach
    fun init() {
        graph = WeightedDirectedGraph()
    }

    @Nested
    inner class GetWeightTest {
        @Nested
        inner class `Edge is in the graph` {
            @Test
            fun `edge's weight should be returned`() {
                val graphStructure = setupWeightedDirected(graph)
                val defaultVertices = graphStructure.first

                val v1 = defaultVertices[1]
                val v3 = defaultVertices[3]
                val edge = graph.getEdge(v3, v1)

                val actualValue = graph.getWeight(edge)
                val expectedValue = 3

                assertEquals(expectedValue, actualValue)
            }

            @Test
            fun `graph shouldn't change`() {
                val graphStructure = setupWeightedDirected(graph)
                val defaultVertices = graphStructure.first

                val v1 = defaultVertices[1]
                val v4 = defaultVertices[3]
                val edge = graph.getEdge(v4, v1)

                graph.getWeight(edge)

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = graphStructure

                assertEquals(expectedGraph, actualGraph)
            }
        }

        @Nested
        inner class `Edge isn't in the graph` {
            @Test
            fun `exception should be thrown`() {
                assertThrows(NoSuchElementException::class.java) {
                    graph.getWeight(Edge(Vertex(1505, 2), Vertex(9, 0)))
                }
            }
        }
    }

    // most of the functionality is tested in the DirectedGraphTest class,
    // as weighted graphs call super methods inside their methods
    @Nested
    inner class AddEdgeTest {
        @Test
        fun `added edge's weight should be added to weight map`() {
            val v0 = graph.addVertex(30)
            val v1 = graph.addVertex(31)

            val newEdge = graph.addEdge(v0, v1, 62)

            val actualValue = graph.getWeight(newEdge)
            val expectedValue = 62

            assertEquals(expectedValue, actualValue)
        }
    }

    @Nested
    inner class RemoveEdgeTest {
        @Test
        fun `removed edge should be removed from the weight map`() {
            val graphStructure = setupWeightedDirected(graph)
            val defaultVertices = graphStructure.first

            val v1 = defaultVertices[1]
            val v2 = defaultVertices[2]
            val edge = graph.getEdge(v1, v2)

            graph.removeEdge(edge)

            assertThrows(NoSuchElementException::class.java) {
                graph.getWeight(edge)
            }
        }
    }

    @Nested
    inner class FindShortestPathDijkstraTest {
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

                val expectedResult = listOf<Pair<Vertex<Int>, Edge<Int>>>(Pair(v1, e0), Pair(v2, e3), Pair(v4, e4))
                val actualResult = graph.findShortestPathDijkstra(v0, v4)

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

                val expectedResult = listOf(Pair(v2, e2))
                val actualResult = graph.findShortestPathDijkstra(v0, v2)
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

                val expectedResult1 = listOf(Pair(v1, e0), Pair(v3, e2))
                val expectedResult2 = listOf(Pair(v2, e1), Pair(v3, e3))

                val actualResult = graph.findShortestPathDijkstra(v0, v3)

                Assertions.assertTrue(actualResult == expectedResult1 || actualResult == expectedResult2)
            }

            @Test
            fun `if graph has single edge`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)

                val e0 = graph.addEdge(v0, v1, 5)

                val expectedResult = listOf(Pair(v1, e0))
                val actualResult = graph.findShortestPathDijkstra(v0, v1)

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

                val expectedResult = listOf(Pair(v1, e0), Pair(v2, e1), Pair(v3, e2))
                val actualResult = graph.findShortestPathDijkstra(v0, v3)

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

                val expectedResult1 = listOf(Pair(v1, e0), Pair(v3, e2), Pair(v4, e4))
                val expectedResult2 = listOf(Pair(v2, e1), Pair(v3, e3), Pair(v4, e4))
                val actualResult = graph.findShortestPathDijkstra(v0, v4)

                Assertions.assertTrue(actualResult == expectedResult1 || actualResult == expectedResult2)
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

                val actualResult = graph.findShortestPathDijkstra(v0, v2)
                val expectedResult = listOf(Pair(v2, e2))

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

                val expectedResult = listOf(Pair(v1, e0), Pair(v2, e1))
                val actualResult = graph.findShortestPathDijkstra(v0, v2)
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

                val actualResult = graph.findShortestPathDijkstra(v0, v3)
                assertEquals(actualResult, null)
            }

            @Test
            fun `if start and end vertices are the same`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)

                graph.addEdge(v0, v1, 1)
                graph.addEdge(v1, v2, 2)
                graph.addEdge(v2, v0, 2)

                val actualResult = graph.findShortestPathDijkstra(v0, v0)

                actualResult?.isEmpty()?.let { assertTrue(it) }
            }

            @Test
            fun `if graph has single vertex`() {
                val v0 = graph.addVertex(0)

                val actualResult = graph.findShortestPathDijkstra(v0, v0)

                actualResult?.isEmpty()?.let { assertTrue(it) }
            }

            @Test
            fun `if path is in other way (not how edges were set)`() {
                val graph = WeightedDirectedGraph<Int>()
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)

                val e0 = graph.addEdge(v0, v1, 0)
                val e1 = graph.addEdge(v1, v2, 0)

                val actualResult = graph.findShortestPathDijkstra(v2, v0)

                actualResult?.isEmpty()?.let { assertTrue(it) }
            }
        }
    }
}
