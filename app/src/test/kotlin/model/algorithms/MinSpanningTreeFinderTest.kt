package model.algorithms

import model.graphs.WeightedUndirectedGraph
import model.graphs.abstractGraph.Edge
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MinSpanningTreeFinderTest {
    private lateinit var graph: WeightedUndirectedGraph<Int>

    @BeforeEach
    fun init() {
        graph = WeightedUndirectedGraph<Int>()
    }

    val minSpanningTreeFinder = MinSpanningTreeFinder()

    @Nested
    inner class `An edge is picked over another` {
        @Test
        fun `if it has lesser weight but both have positive`() {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)

            val e01Heavy = graph.addEdge(v0, v1, 5)
            val e01Light = graph.addEdge(v0, v1, 3)

            val expectedReturn = listOf(e01Light)
            val actualReturn = minSpanningTreeFinder.findMinSpanningTree(graph)

            assertEquals(expectedReturn, actualReturn)
        }

        @Test
        fun `if it has lesser weight but both have negative`() {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)

            val e01Heavy = graph.addEdge(v0, v1, -5)
            val e01Light = graph.addEdge(v0, v1, -10)

            val expectedReturn = listOf(e01Light)
            val actualReturn = minSpanningTreeFinder.findMinSpanningTree(graph)

            assertEquals(expectedReturn, actualReturn)
        }

        @Test
        fun `if it has zero weight and other has positive`() {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)

            val e01Pos = graph.addEdge(v0, v1, 5)
            val e01Zero = graph.addEdge(v0, v1, 0)

            val expectedReturn = listOf(e01Zero)
            val actualReturn = minSpanningTreeFinder.findMinSpanningTree(graph)

            assertEquals(expectedReturn, actualReturn)
        }

        @Test
        fun `if it has negative weight and other has positive or zero`() {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)

            val e01Pos = graph.addEdge(v0, v1, 5)
            val e01Zero = graph.addEdge(v0, v1, 0)
            val e01Neg = graph.addEdge(v0, v1, -5)

            val expectedReturn = listOf(e01Neg)
            val actualReturn = minSpanningTreeFinder.findMinSpanningTree(graph)

            assertEquals(expectedReturn, actualReturn)
        }
    }

    @Nested
    inner class `An edge is not picked over another` {
        @Test
        fun `if it forms a cycle and has greatest weight in it`() {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)

            val e01 = graph.addEdge(v0, v1, 1)
            val e12 = graph.addEdge(v1, v2, 1)
            val e23 = graph.addEdge(v2, v3, 1)

            val e30 = graph.addEdge(v3, v0, 5)

            val expectedReturn = setOf(e01, e12, e23)
            val actualReturn = minSpanningTreeFinder.findMinSpanningTree(graph).toSet()

            assertEquals(expectedReturn, actualReturn)
        }
    }

    @Nested
    inner class `All edges should be returned` {
        @Test
        fun `if graph is a tree`() {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)

            val eva01 = graph.addEdge(v0, v1, 1)
            val eva02 = graph.addEdge(v0, v2, 10)
            val e23 = graph.addEdge(v2, v3, 0)
            val e24 = graph.addEdge(v2, v4, -20)

            val expectedResult = setOf(e24, e23, eva01, eva02)
            val actualResult = minSpanningTreeFinder.findMinSpanningTree(graph).toSet()

            assertEquals(expectedResult, actualResult)
        }
    }

    @Nested
    inner class `No edge should be returned` {
        @Test
        fun `if graph has no vertices`() {
            val expectedResult = listOf<Edge<Int>>()
            val actualResult = minSpanningTreeFinder.findMinSpanningTree(graph)

            assertEquals(expectedResult, actualResult)
        }

        @Test
        fun `if graph has no edges`() {
            graph.addVertex(0)
            graph.addVertex(1)

            val expectedResult = listOf<Edge<Int>>()
            val actualResult = minSpanningTreeFinder.findMinSpanningTree(graph)

            assertEquals(expectedResult, actualResult)
        }
    }
}
