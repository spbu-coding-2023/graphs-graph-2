package model.abstractGraph

import model.DirectedGraph
import model.UndirectedGraph
import model.WeightedDirectedGraph
import model.WeightedUndirectedGraph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

@ParameterizedTest(name = "{0}")
@ArgumentsSource(AllGraphTypesProvider::class)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
private annotation class TestAllGraphTypes

class AllGraphTypesProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?) = Stream.of(
        Arguments.of(UndirectedGraph<Int>()),
        Arguments.of(DirectedGraph<Int>()),
        Arguments.of(WeightedDirectedGraph<Int>()),
        Arguments.of(WeightedUndirectedGraph<Int>())
    )
}

fun setup(graph: Graph<Int>) = graph.apply {
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
        @Nested
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

        @Nested
        inner class `Graph is empty` {
            @TestAllGraphTypes
            fun `empty list should be returned`(graph: Graph<Int>) {
                val actualList = graph.getVertices()
                val expectedList: List<Int> = listOf()

                assertEquals(expectedList, actualList)
            }

            @TestAllGraphTypes
            fun `empty graph should not change`(graph: Graph<Int>) {
                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = defaultVertices to defaultEdgesSet

                assertEquals(expectedGraph, actualGraph)
            }
        }
    }

    @Nested
    inner class GetEdgesTest {
        @Nested
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

        @Nested
        inner class `Graph is empty` {
            @TestAllGraphTypes
            fun `empty list should be returned`(graph: Graph<Int>) {
                val actualSet = graph.getEdges().toSet()
                val expectedSet = defaultEdgesSet

                assertEquals(expectedSet, actualSet)
            }

            @TestAllGraphTypes
            fun `empty graph should not change`(graph: Graph<Int>) {
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
    inner class RemoveVertexTest {
        @Nested
        inner class `Vertex is in the graph` {
            @TestAllGraphTypes
            fun `removed vertex should be returned`(graph: Graph<Int>) {
                setup(graph)

                val actualValue = graph.removeVertex(v2)
                val expectedValue = v2

                assertEquals(expectedValue, actualValue)
            }

            @TestAllGraphTypes
            fun `vertex added after removal should have right id`(graph: Graph<Int>) {
                setup(graph)
                graph.removeVertex(v3)
                val newVertex = graph.addVertex(5)

                val actualId = newVertex.id
                val expectedId = 4

                assertEquals(expectedId, actualId)
            }

            @Nested
            inner class `Vertex is last` {
                @TestAllGraphTypes
                fun `vertex should be removed from vertices list`(graph: Graph<Int>) {
                    setup(graph)
                    graph.removeVertex(v3)

                    val actualVertices = graph.getVertices()
                    val expectedVertices = defaultVertices - v3

                    assertEquals(expectedVertices, actualVertices)
                }

                @TestAllGraphTypes
                fun `incident edges should be removed`(graph: Graph<Int>) {
                    setup(graph)
                    graph.removeVertex(v4)

                    val actualEdges = graph.getEdges().toSet()
                    val expectedEdges = defaultEdgesSet - Edge(v3, v4) - Edge(v4, v1)

                    assertEquals(expectedEdges, actualEdges)
                }
            }

            @Nested
            inner class `Vertex isn't last` {
                @TestAllGraphTypes
                fun `last added vertex should be moved to removed vertex's place`(graph: Graph<Int>) {
                    setup(graph)
                    graph.removeVertex(v2)

                    val actualVertices = graph.getVertices()
                    val expectedVertices = listOf(
                        Vertex(0, 0),
                        Vertex(1, 1),
                        Vertex(2, 4),
                        Vertex(3, 3),
                    )

                    assertEquals(expectedVertices, actualVertices)
                }

                @TestAllGraphTypes
                fun `last added vertex's incident edges should change`(graph: Graph<Int>) {
                    setup(graph)
                    graph.removeVertex(v2)

                    val v4Copy = Vertex(2, 4)

                    val actualEdges = graph.getEdges().toSet()
                    val expectedEdges = setOf(
                        Edge(v0, v1),
                        Edge(v3, v4Copy),
                        Edge(v4Copy, v1)
                    )

                    assertEquals(expectedEdges, actualEdges)
                }
            }
        }

        @Nested
        inner class `Vertex is not in the graph` {
            @TestAllGraphTypes
            fun `removing vertex from an empty graph should cause exception`(graph: Graph<Int>) {
                assertThrows(NoSuchElementException::class.java) {
                    graph.removeVertex(Vertex(0,0))
                }
            }

            @TestAllGraphTypes
            fun `removing non-existing vertex from a non-empty graph should cause exception`(graph: Graph<Int>) {
                setup(graph)

                assertThrows(NoSuchElementException::class.java) {
                    graph.removeVertex(Vertex(1904,-360))
                }
            }

            @TestAllGraphTypes
            fun `removing vertex with wrong id should cause exception`(graph: Graph<Int>) {
                setup(graph)

                assertThrows(NoSuchElementException::class.java) {
                    graph.removeVertex(Vertex(6,3))
                }
            }

            @TestAllGraphTypes
            fun `removing vertex with wrong data should cause exception`(graph: Graph<Int>) {
                setup(graph)

                assertThrows(NoSuchElementException::class.java) {
                    graph.removeVertex(Vertex(0,35))
                }
            }
        }
    }
}
