package model.abstractGraph

import model.DirectedGraph
import model.UndirectedGraph
import model.WeightedDirectedGraph
import model.WeightedUndirectedGraph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@ParameterizedTest
@MethodSource("provideAllGraphTypes")
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
private annotation class TestAllGraphTypes

fun provideAllGraphTypes(): Stream<Arguments> {
    return Stream.of(
        Arguments.of(UndirectedGraph<Int>()),
        Arguments.of(DirectedGraph<Int>()),
        Arguments.of(WeightedDirectedGraph<Int>()),
        Arguments.of(WeightedUndirectedGraph<Int>())
    )
}

fun setup(graph: Graph<Int>): Graph<Int> {
    graph.apply {
        val v0 = addVertex(0)
        val v1 = addVertex(1)
        val v2 = addVertex(2)
        val v3 = addVertex(3)
        val v4 = addVertex(4)

        addEdge(v0, v1)
        addEdge(v1, v2)
        addEdge(v2, v3)
        addEdge(v3, v4)
        addEdge(v4, v1)
    }

    return graph
}

val v0 = Vertex(0, 0)
val v1 = Vertex(1, 1)
val v2 = Vertex(2, 2)
val v3 = Vertex(3, 3)
val v4 = Vertex(4, 4)

val defaultVertices = listOf(v0, v1, v2, v3, v4)

val defaultEdgesSet = setOf(
    Edge(v0, v1),
    Edge(v1, v2),
    Edge(v2, v3),
    Edge(v3, v4),
    Edge(v4, v1)
)

class GraphTest {
    @Nested
    inner class GetVerticesTest {
        inner class `Graph is not empty` {
            @TestAllGraphTypes
            fun `non-empty list of vertices should be returned`(graph: Graph<Int>) {
                setup(graph)

                val actualList = graph.getVertices()
                val expectedList = defaultVertices

                assertEquals(expectedList, actualList)
            }

            @TestAllGraphTypes
            fun `graph should not change`(graph: Graph<Int>) {
                setup(graph)

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = defaultVertices to defaultEdgesSet

                assertEquals(expectedGraph, actualGraph)
            }
        }

        inner class `Graph is empty` {
            @TestAllGraphTypes
            fun `empty list is should be returned`(graph: Graph<Int>) {
                val actualList = graph.getVertices()
                val expectedList: List<Int> = listOf()

                assertEquals(expectedList, actualList)
            }

            @TestAllGraphTypes
            fun `graph should not change`(graph: Graph<Int>) {
                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = defaultVertices to defaultEdgesSet

                assertEquals(expectedGraph, actualGraph)
            }
        }
    }

    @Nested
    inner class GetEdgesTest {
        inner class `Graph is not empty` {
            @TestAllGraphTypes
            fun `non-empty list of edges should be returned`(graph: Graph<Int>) {
                setup(graph)

                val actualSet = graph.getEdges().toSet()
                val expectedSet = defaultEdgesSet

                assertEquals(expectedSet, actualSet)
            }

            @TestAllGraphTypes
            fun `graph should not change`(graph: Graph<Int>) {
                setup(graph)

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = defaultVertices to defaultEdgesSet

                assertEquals(expectedGraph, actualGraph)
            }
        }

        inner class `Graph is empty` {
            @TestAllGraphTypes
            fun `empty list is should be returned`(graph: Graph<Int>) {
                val actualSet = graph.getEdges().toSet()
                val expectedSet = defaultEdgesSet

                assertEquals(expectedSet, actualSet)
            }

            @TestAllGraphTypes
            fun `graph should not change`(graph: Graph<Int>) {
                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = defaultVertices to defaultEdgesSet

                assertEquals(expectedGraph, actualGraph)
            }
        }
    }

    @Nested
    inner class AddVertexTest {
        @TestAllGraphTypes
        fun `added vertex should be returned`(graph: Graph<Int>) {
            val actualValue = graph.addVertex(0)
            val expectedValue = Vertex(0, 0)

            assertEquals(expectedValue, actualValue)
        }

        @TestAllGraphTypes
        fun `vertex should be added to graph`(graph: Graph<Int>) {
            setup(graph)
            graph.addVertex(5)

            val actualGraph = graph.getVertices() to graph.getEdges().toSet()
            val expectedGraph = (defaultVertices + Vertex(5, 5)) to defaultEdgesSet

            assertEquals(expectedGraph, actualGraph)
        }
    }

    @Nested
    inner class RemoveVertexTest {}
}
