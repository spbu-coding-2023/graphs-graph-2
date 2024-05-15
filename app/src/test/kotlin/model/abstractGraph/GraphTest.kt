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
    override fun provideArguments(context: ExtensionContext?): Stream<Arguments> = Stream.of(
        Arguments.of(UndirectedGraph<Int>()),
        Arguments.of(DirectedGraph<Int>()),
        Arguments.of(WeightedDirectedGraph<Int>()),
        Arguments.of(WeightedUndirectedGraph<Int>())
    )
}

fun setup(graph: Graph<Int>): Pair<List<Vertex<Int>>, Set<Edge<Int>>> {
    val v0 = graph.addVertex(0)
    val v1 = graph.addVertex(1)
    val v2 = graph.addVertex(2)
    val v3 = graph.addVertex(3)
    val v4 = graph.addVertex(4)

    val defaultVerticesList = listOf(v0, v1, v2, v3, v4)

    val defaultEdgesSet = setOf(
        graph.addEdge(v0, v1),
        graph.addEdge(v1, v2),
        graph.addEdge(v2, v3),
        graph.addEdge(v3, v4),
        graph.addEdge(v4, v1)
    )

    return defaultVerticesList to defaultEdgesSet
}

val emptyVerticesList = listOf<Vertex<Int>>()
val emptyEdgesSet = setOf<Edge<Int>>()

class GraphTest {
    @Nested
    inner class GetVerticesTest {
        @Nested
        inner class `Graph is not empty` {
            @TestAllGraphTypes
            fun `non-empty list of vertices should be returned`(graph: Graph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first
                val defaultEdgesSet = graphStructure.second

                val actualList = graph.getVertices()
                val expectedList = defaultVerticesList

                assertEquals(expectedList, actualList)
            }

            @TestAllGraphTypes
            fun `graph should not change`(graph: Graph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first
                val defaultEdgesSet = graphStructure.second

                graph.getVertices()

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = defaultVerticesList to defaultEdgesSet

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
                graph.getVertices()

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = emptyVerticesList to emptyEdgesSet

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
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first
                val defaultEdgesSet = graphStructure.second

                val actualSet = graph.getEdges().toSet()
                val expectedSet = defaultEdgesSet

                assertEquals(expectedSet, actualSet)
            }

            @TestAllGraphTypes
            fun `graph should not change`(graph: Graph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first
                val defaultEdgesSet = graphStructure.second

                graph.getEdges()

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = defaultVerticesList to defaultEdgesSet

                assertEquals(expectedGraph, actualGraph)
            }
        }

        @Nested
        inner class `Graph is empty` {
            @TestAllGraphTypes
            fun `empty list should be returned`(graph: Graph<Int>) {
                val actualSet = graph.getEdges().toSet()
                val expectedSet = emptyEdgesSet

                assertEquals(expectedSet, actualSet)
            }

            @TestAllGraphTypes
            fun `empty graph should not change`(graph: Graph<Int>) {
                graph.getEdges()

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = emptyVerticesList to emptyEdgesSet

                assertEquals(expectedGraph, actualGraph)
            }
        }
    }

    @Nested
    inner class AddVertexTest {
        @TestAllGraphTypes
        fun `added vertex should be returned`(graph: Graph<Int>) {
            val returnedVertex = graph.addVertex(0)

            assertTrue(returnedVertex.id == 0 && returnedVertex.data == 0)
        }

        @TestAllGraphTypes
        fun `vertex should be added to graph`(graph: Graph<Int>) {
            val graphStructure = setup(graph)
            val defaultVerticesList = graphStructure.first
            val defaultEdgesSet = graphStructure.second

            val newVertex = graph.addVertex(5)

            val actualGraph = graph.getVertices() to graph.getEdges().toSet()
            val expectedGraph = (defaultVerticesList + newVertex) to defaultEdgesSet

            assertEquals(expectedGraph, actualGraph)
        }
    }

    @Nested
    inner class RemoveVertexTest {
        @Nested
        inner class `Vertex is in the graph` {
            @TestAllGraphTypes
            fun `removed vertex should be returned`(graph: Graph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first
                val defaultEdgesSet = graphStructure.second

                val returnedVertex = graph.removeVertex(defaultVerticesList[2])

                assertTrue(returnedVertex.id == 2 && returnedVertex.data == 2)
            }

            @TestAllGraphTypes
            fun `vertex added after removal should have right id`(graph: Graph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first
                val defaultEdgesSet = graphStructure.second

                graph.removeVertex(defaultVerticesList[3])
                val newVertex = graph.addVertex(5)

                assertTrue(newVertex.id == 4)
            }

            @Nested
            inner class `Vertex is last` {
                @TestAllGraphTypes
                fun `vertex should be removed from vertices list`(graph: Graph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first
                    val defaultEdgesSet = graphStructure.second

                    val removedVertex = graph.removeVertex(defaultVerticesList[4])

                    val actualVertices = graph.getVertices()
                    val expectedVertices = defaultVerticesList - removedVertex

                    assertEquals(expectedVertices, actualVertices)
                }

                @TestAllGraphTypes
                fun `incident edges should be removed`(graph: Graph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first
                    val defaultEdgesSet = graphStructure.second

                    val v0 = defaultVerticesList[0]
                    val v1 = defaultVerticesList[1]
                    val v2 = defaultVerticesList[2]
                    val v3 = defaultVerticesList[3]
                    val v4 = defaultVerticesList[4]

                    val e0 = graph.getEdge(v3, v4)
                    val e1 = graph.getEdge(v4, v1)

                    graph.removeVertex(v4)

                    val actualEdges = graph.getEdges().toSet()
                    val expectedEdges = defaultEdgesSet - e0 - e1

                    assertEquals(expectedEdges, actualEdges)
                }
            }

            @Nested
            inner class `Vertex isn't last` {
                @TestAllGraphTypes
                fun `last added vertex should be moved to removed vertex's place`(graph: Graph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first
                    val defaultEdgesSet = graphStructure.second

                    val oldV0 = defaultVerticesList[0]
                    val oldV1 = defaultVerticesList[1]
                    val oldV2 = defaultVerticesList[2]
                    val oldV3 = defaultVerticesList[3]
                    val oldV4 = defaultVerticesList[4]

                    graph.removeVertex(oldV2)

                    val newVertices = graph.getVertices()

                    val newV0 = newVertices[0]
                    val newV1 = newVertices[1]
                    val newV2 = newVertices[2]
                    val newV3 = newVertices[3]

                    assertTrue(
                        newV0 == oldV0 &&
                        newV1 == oldV1 &&
                        newV2.id == 2 && newV2.data == 4 &&
                        newV3 == oldV3
                    )
                }

                @TestAllGraphTypes
                fun `last added vertex's incident edges should change`(graph: Graph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first
                    val defaultEdgesSet = graphStructure.second

                    val oldV0 = defaultVerticesList[0]
                    val oldV1 = defaultVerticesList[1]
                    val oldV2 = defaultVerticesList[2]
                    val oldV3 = defaultVerticesList[3]
                    val oldV4 = defaultVerticesList[4]

                    graph.removeVertex(oldV2)

                    val newVertices = graph.getVertices()

                    val newV0 = newVertices[0]
                    val newV1 = newVertices[1]
                    val newV2 = newVertices[2]
                    val newV3 = newVertices[3]

                    val actualEdges = graph.getEdges().toSet()
                    val expectedEdges = setOf(
                        graph.getEdge(newV0, newV1),
                        graph.getEdge(newV3, newV2),
                        graph.addEdge(newV2, newV1)
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
                val graphStructure = setup(graph)

                assertThrows(NoSuchElementException::class.java) {
                    graph.removeVertex(Vertex(1904,-360))
                }
            }

            @TestAllGraphTypes
            fun `removing vertex with wrong id should cause exception`(graph: Graph<Int>) {
                val graphStructure = setup(graph)

                assertThrows(NoSuchElementException::class.java) {
                    graph.removeVertex(Vertex(6,3))
                }
            }

            @TestAllGraphTypes
            fun `removing vertex with wrong data should cause exception`(graph: Graph<Int>) {
                val graphStructure = setup(graph)

                assertThrows(NoSuchElementException::class.java) {
                    graph.removeVertex(Vertex(0,35))
                }
            }
        }
    }
}
