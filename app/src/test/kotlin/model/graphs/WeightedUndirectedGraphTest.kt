package model.graphs

import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Vertex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import util.setupWeightedUndirected

class WeightedUndirectedGraphTest {
    private lateinit var graph: WeightedUndirectedGraph<Int>

    @BeforeEach
    fun init() {
        graph = WeightedUndirectedGraph()
    }

    @Nested
    inner class GetWeightTest {
        @Nested
        inner class `Edge is in the graph` {
            @Test
            fun `edge's weight should be returned`() {
                val graphStructure = setupWeightedUndirected(graph)
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
                val graphStructure = setupWeightedUndirected(graph)
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
            val graphStructure = setupWeightedUndirected(graph)
            val defaultVertices = graphStructure.first

            val v1 = defaultVertices[1]
            val v2 = defaultVertices[2]
            val edge = graph.getEdge(v1, v2)

            graph.removeEdge(edge)

            assertThrows(NoSuchElementException::class.java) { graph.getWeight(edge) }
        }
    }
}
