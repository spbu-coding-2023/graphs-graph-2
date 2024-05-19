package model

import model.abstractGraph.Edge
import model.abstractGraph.Vertex
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import util.annotations.TestAllDirectedGraphs
import util.emptyEdgesSet
import util.emptyVerticesList
import util.setup

class DirectedGraphTest {
    @Nested
    inner class GetEdgeTest {
        @Nested
        inner class `Edge is in the graph` {
            @TestAllDirectedGraphs
            fun `edge should be returned`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v2 = defaultVerticesList[2]

                val newEdge = graph.addEdge(v0, v2)

                val actualValue = newEdge
                val expectedValue = graph.getEdge(v0, v2)

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `graph shouldn't change`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v2 = defaultVerticesList[2]
                val v3 = defaultVerticesList[3]

                graph.getEdge(v2, v3)

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = graphStructure

                assertEquals(expectedGraph, actualGraph)
            }
        }

        @Nested
        inner class `Edge isn't in the graph` {
            @TestAllDirectedGraphs
            fun `order of arguments should matter`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v1 = defaultVerticesList[1]

                assertThrows(NoSuchElementException::class.java) {
                    graph.getEdge(v1, v0)
                }
            }

            @TestAllDirectedGraphs
            fun `trying to get non-existent edge should throw an exception`(graph: DirectedGraph<Int>) {
                assertThrows(NoSuchElementException::class.java) {
                    graph.getEdge(Vertex(2, 12), Vertex(85, 6))
                }
            }
        }
    }

    @Nested
    inner class GetNeighboursTest {
        @Nested
        inner class `Vertex is in the graph` {
            @TestAllDirectedGraphs
            fun `neighbours should be returned`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v1 = defaultVerticesList[1]
                val v2 = defaultVerticesList[2]
                val v3 = defaultVerticesList[3]
                val v4 = defaultVerticesList[4]

                val actualValue = graph.getNeighbours(v3).toSet()
                val expectedValue = setOf(v4, v1)

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `graph shouldn't change`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]

                graph.getNeighbours(v0)

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = graphStructure

                assertEquals(expectedGraph, actualGraph)
            }
        }

        @Nested
        inner class `Vertex isn't in the graph` {
            @TestAllDirectedGraphs
            fun `exception should be thrown`(graph: DirectedGraph<Int>) {
                assertThrows(NoSuchElementException::class.java) {
                    graph.getNeighbours(Vertex(2201, 2006))
                }
            }
        }
    }

    @Nested
    inner class GetOutgoingEdgesTest {
        @Nested
        inner class `Vertex is in the graph` {
            @TestAllDirectedGraphs
            fun `outgoing edges should be returned`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v1 = defaultVerticesList[1]
                val v2 = defaultVerticesList[2]
                val v3 = defaultVerticesList[3]
                val v4 = defaultVerticesList[4]

                val actualValue = graph.getOutgoingEdges(v3).toSet()
                val expectedValue = setOf(graph.getEdge(v3, v4), graph.getEdge(v3, v1))

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `graph shouldn't change`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v4 = defaultVerticesList[4]

                graph.getOutgoingEdges(v4)

                val actualGraph = graph.getVertices() to graph.getEdges().toSet()
                val expectedGraph = graphStructure

                assertEquals(expectedGraph, actualGraph)
            }
        }

        @Nested
        inner class `Vertex isn't in the graph` {
            @TestAllDirectedGraphs
            fun `exception should be thrown`(graph: DirectedGraph<Int>) {
                assertThrows(NoSuchElementException::class.java) {
                    graph.getOutgoingEdges(Vertex(2611, 2005))
                }
            }
        }
    }

    @Nested
    inner class AddEdgeTest {
        @Nested
        inner class `Two vertices are in the graph` {
            @Nested
            inner class `Vertices are different` {
                @TestAllDirectedGraphs
                fun `Added edge should be returned`(graph: DirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v0 = defaultVerticesList[0]
                    val v4 = defaultVerticesList[4]

                    val actualValue = graph.addEdge(v0, v4)
                    val expectedValue = graph.getEdge(v0, v4)

                    assertEquals(expectedValue, actualValue)
                }

                @TestAllDirectedGraphs
                fun `Edge should be added to graph`(graph: DirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first
                    val defaultEdgesSet = graphStructure.second

                    val v0 = defaultVerticesList[0]
                    val v4 = defaultVerticesList[4]

                    val newEdge = graph.addEdge(v4, v0)

                    val actualEdges = graph.getEdges().toSet()
                    val expectedEdges = defaultEdgesSet + newEdge

                    assertEquals(expectedEdges, actualEdges)
                }

                @TestAllDirectedGraphs
                fun `one vertex has to be added to the other's adjacency map value`(graph: DirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v0 = defaultVerticesList[0]
                    val v1 = defaultVerticesList[1]
                    val v2 = defaultVerticesList[2]
                    val v3 = defaultVerticesList[3]
                    val v4 = defaultVerticesList[4]

                    graph.addEdge(v0, v2)

                    val actualVertices = graph.getNeighbours(v0).toSet()
                    val expectedVertices = setOf(v1, v2)

                    assertEquals(expectedVertices, actualVertices)
                }

                @TestAllDirectedGraphs
                fun `edge has to be added to first vertex's outgoing edges map value`(graph: DirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v0 = defaultVerticesList[0]
                    val v1 = defaultVerticesList[1]
                    val v2 = defaultVerticesList[2]
                    val v3 = defaultVerticesList[3]
                    val v4 = defaultVerticesList[4]

                    graph.addEdge(v3, v0)

                    val actualEdges = graph.getOutgoingEdges(v3).toSet()
                    val expectedEdges = setOf(graph.getEdge(v3, v4), graph.getEdge(v3, v1), graph.getEdge(v3, v0))

                    assertEquals(expectedEdges, actualEdges)
                }

                @TestAllDirectedGraphs
                fun `adding already existing edge shouldn't change anything`(graph: DirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v1 = defaultVerticesList[1]
                    val v4 = defaultVerticesList[4]

                    val expectedNeighbours = graph.getNeighbours(v4).toSet()
                    val expectedOutgoingEdges = graph.getOutgoingEdges(v4).toSet()

                    graph.addEdge(v4, v1)

                    val actualNeighbours = graph.getNeighbours(v4).toSet()
                    val actualOutgoingEdges = graph.getOutgoingEdges(v4).toSet()

                    val expectedGraph = graphStructure
                    val actualGraph = graph.getVertices() to graph.getEdges().toSet()

                    assertEquals(expectedGraph, actualGraph)
                    assertEquals(expectedNeighbours, actualNeighbours)
                    assertEquals(expectedOutgoingEdges, actualOutgoingEdges)
                }

                @TestAllDirectedGraphs
                fun `second vertex's map values shouldn't change`(graph: DirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v0 = defaultVerticesList[0]
                    val v3 = defaultVerticesList[3]

                    val expectedNeighbours = graph.getNeighbours(v0).toSet()
                    val expectedOutgoingEdges = graph.getOutgoingEdges(v0).toSet()

                    graph.addEdge(v3, v0)

                    val actualNeighbours = graph.getNeighbours(v0).toSet()
                    val actualOutgoingEdges = graph.getOutgoingEdges(v0).toSet()

                    assertEquals(expectedNeighbours, actualNeighbours)
                    assertEquals(expectedOutgoingEdges, actualOutgoingEdges)
                }
            }

            @Nested
            inner class `Vertices are the same` {
                @TestAllDirectedGraphs
                fun `exception should be thrown`(graph: DirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v2 = defaultVerticesList[2]

                    assertThrows(IllegalArgumentException::class.java) {
                        graph.addEdge(v2, v2)
                    }
                }
            }
        }

        @Nested
        inner class `One of the vertices isn't in the graph` {
            @TestAllDirectedGraphs
            fun `first vertex isn't in the graph`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]

                assertThrows(IllegalArgumentException::class.java) {
                    graph.addEdge(Vertex(2210, 2005), v0)
                }
            }

            @TestAllDirectedGraphs
            fun `second vertex isn't in the graph`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]

                assertThrows(IllegalArgumentException::class.java) {
                    graph.addEdge(v0, Vertex(2510, 1917))
                }
            }

            @TestAllDirectedGraphs
            fun `both vertices aren't in the graph`(graph: DirectedGraph<Int>) {
                assertThrows(IllegalArgumentException::class.java) {
                    graph.addEdge(Vertex(3010, 1978), Vertex(1002, 1982))
                }
            }
        }
    }

    @Nested
    inner class RemoveEdgeTest {
        @Nested
        inner class `Edge is in the graph` {
            @TestAllDirectedGraphs
            fun `removed edge should be returned`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v1 = defaultVerticesList[1]
                val v3 = defaultVerticesList[3]

                val edgeToRemove = graph.getEdge(v3, v1)

                val actualValue = graph.removeEdge(edgeToRemove)
                val expectedValue = edgeToRemove

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `edge should be removed from graph`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first
                val defaultEdgesSet = graphStructure.second

                val v1 = defaultVerticesList[1]
                val v4 = defaultVerticesList[4]

                val edgeToRemove = graph.getEdge(v4, v1)
                graph.removeEdge(edgeToRemove)

                val actualEdges = graph.getEdges().toSet()
                val expectedEdges = defaultEdgesSet - edgeToRemove

                assertEquals(expectedEdges, actualEdges)
            }

            @TestAllDirectedGraphs
            fun `second vertex should be removed from first's adjacency map value`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v1 = defaultVerticesList[1]
                val v2 = defaultVerticesList[2]

                val edgeToRemove = graph.getEdge(v1, v2)
                graph.removeEdge(edgeToRemove)

                val actualVertices = graph.getNeighbours(v1).toSet()
                val expectedVertices = emptyVerticesList.toSet()

                assertEquals(expectedVertices, actualVertices)
            }

            @TestAllDirectedGraphs
            fun `edge should be removed from first vertex's outgoing edges map value`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v2 = defaultVerticesList[2]
                val v3 = defaultVerticesList[3]

                val edgeToRemove = graph.getEdge(v2, v3)
                graph.removeEdge(edgeToRemove)

                val actualEdges = graph.getOutgoingEdges(v2).toSet()
                val expectedEdges = emptyEdgesSet

                assertEquals(expectedEdges, actualEdges)
            }
        }

        @Nested
        inner class `Edge isn't in the graph` {
            @TestAllDirectedGraphs
            fun `wrong order of the arguments should throw an exception`(graph: DirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v3 = defaultVerticesList[3]
                val v4 = defaultVerticesList[4]

                assertThrows(NoSuchElementException::class.java) {
                    graph.removeEdge(graph.getEdge(v4, v3))
                }
            }

            @TestAllDirectedGraphs
            fun `non-existing edge should throw an exception`(graph: DirectedGraph<Int>) {
                assertThrows(NoSuchElementException::class.java) {
                    graph.removeEdge(Edge(Vertex(0,0), Vertex(1, 1)))
                }
            }
        }
    }

    @Nested
    inner class FindSCCTest {}
}
