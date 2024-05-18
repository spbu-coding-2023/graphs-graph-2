package model

import model.abstractGraph.Edge
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import util.setup

class WeightedUndirectedGraphTest {
    private lateinit var graph: WeightedUndirectedGraph<Int>

    @BeforeEach
    fun init() {
        graph = WeightedUndirectedGraph()
    }

    @Nested
    inner class GetWeightTest {}

    @Nested
    inner class AddEdgeTest {}

    @Nested
    inner class RemoveEdgeTest {}

    @Nested
    inner class FindShortestPathDijkstraTest {}

    @Nested
    inner class FindMinSpanningTreeTest {
        @Nested
        inner class `An edge is picked over another`() {
            @Test
            fun `if it has lesser weight but both have positive`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)

                graph.addEdge(v0, v1, 5)
                val lightEdge = graph.addEdge(v0, v1, 3)

                val expectedReturn = listOf(lightEdge)
                val actualReturn = graph.findMinSpanningTree()

                assertEquals(expectedReturn, actualReturn)
            }

            @Test
            fun `if it has lesser weight but both have negative`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)

                graph.addEdge(v0, v1, -5)
                val lightEdge = graph.addEdge(v0, v1, -10)

                val expectedReturn = listOf(lightEdge)
                val actualReturn = graph.findMinSpanningTree()

                assertEquals(expectedReturn, actualReturn)
            }

            @Test
            fun `if it has zero weight and other has positive`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)

                graph.addEdge(v0, v1, 5)
                val zeroEdge = graph.addEdge(v0, v1, 0)

                val expectedReturn = listOf(zeroEdge)
                val actualReturn = graph.findMinSpanningTree()

                assertEquals(expectedReturn, actualReturn)
            }

            @Test
            fun `if it has negative weight and other has positive or zero`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)

                graph.addEdge(v0, v1, 5)
                graph.addEdge(v0, v1, 0)
                val negativeEdge = graph.addEdge(v0, v1, -5)

                val expectedReturn = listOf(negativeEdge)
                val actualReturn = graph.findMinSpanningTree()

                assertEquals(expectedReturn, actualReturn)
            }
        }

        @Nested
        inner class `An edge is not picked over another`() {
            @Test
            fun `if it forms a cycle and has greatest weight in it`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                val e01 = graph.addEdge(v0, v1, 1)
                val e12 = graph.addEdge(v1, v2, 1)
                val e23 = graph.addEdge(v2, v3, 1)

                val cycleEdge30 = graph.addEdge(v3, v0, 5)

                val expectedReturn = setOf(e01, e12, e23)
                val actualReturn = graph.findMinSpanningTree().toSet()

                assertEquals(expectedReturn, actualReturn)
            }
        }

        @Nested
        inner class `All edges should be returned`() {
            @Test
            fun `if graph is a tree`() {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)

                val e01 = graph.addEdge(v0, v1, 1)
                val e02 = graph.addEdge(v0, v2, 10)
                val e23 = graph.addEdge(v2, v3, 0)
                val e24 = graph.addEdge(v2, v4, -20)

                val expectedResult = setOf(e24, e23, e01, e02)
                val actualResult = graph.findMinSpanningTree().toSet()

                assertEquals(expectedResult, actualResult)
            }
        }

        @Nested
        inner class `No edge should be returned`() {
            @Test
            fun `if graph has no vertices`() {
                val expectedResult = listOf<Edge<Int>>()
                val actualResult = graph.findMinSpanningTree()

                assertEquals(expectedResult, actualResult)
            }

            @Test
            fun `if graph has no edges`() {
                graph.apply {
                    addVertex(0)
                    addVertex(1)
                }

                val expectedResult = listOf<Edge<Int>>()
                val actualResult = graph.findMinSpanningTree()

                assertEquals(expectedResult, actualResult)
            }
        }
    }
}
