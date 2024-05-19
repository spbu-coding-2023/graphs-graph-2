package model

import model.abstractGraph.Edge
import model.abstractGraph.Vertex
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import util.annotations.TestAllUndirectedGraphs
import util.emptyEdgesSet
import util.emptyVerticesList
import util.setup

class UndirectedGraphTest {
    @Nested
    inner class GetEdgeTest {
        @Nested
        inner class `Edge is in the graph` {
            @TestAllUndirectedGraphs
            fun `edge should be returned`(graph: UndirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v2 = defaultVerticesList[2]

                val newEdge = graph.addEdge(v0, v2)

                val actualValue = newEdge
                val expectedValue = graph.getEdge(v0, v2)

                assertEquals(expectedValue, actualValue)
            }

            @TestAllUndirectedGraphs
            fun `order of the arguments shouldn't matter`(graph: UndirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v1 = defaultVerticesList[1]

                assertEquals(graph.getEdge(v0, v1), graph.getEdge(v1, v0))
            }

            @TestAllUndirectedGraphs
            fun `graph shouldn't change`(graph: UndirectedGraph<Int>) {
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
            @TestAllUndirectedGraphs
            fun `trying to get non-existent edge should throw an exception`(graph: UndirectedGraph<Int>) {
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
            @TestAllUndirectedGraphs
            fun `neighbours should be returned`(graph: UndirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v1 = defaultVerticesList[1]
                val v2 = defaultVerticesList[2]
                val v3 = defaultVerticesList[3]
                val v4 = defaultVerticesList[4]

                val actualValue = graph.getNeighbours(v3).toSet()
                val expectedValue = setOf(v1, v2, v4)

                assertEquals(expectedValue, actualValue)
            }

            @TestAllUndirectedGraphs
            fun `graph shouldn't change`(graph: UndirectedGraph<Int>) {
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
            @TestAllUndirectedGraphs
            fun `exception should be thrown`(graph: UndirectedGraph<Int>) {
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
            @TestAllUndirectedGraphs
            fun `outgoing edges should be returned`(graph: UndirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v1 = defaultVerticesList[1]
                val v2 = defaultVerticesList[2]
                val v3 = defaultVerticesList[3]
                val v4 = defaultVerticesList[4]

                val actualValue = graph.getOutgoingEdges(v3).toSet()
                val expectedValue = setOf(
                    graph.getEdge(v3, v4),
                    graph.getEdge(v3, v1),
                    graph.getEdge(v3, v2)
                )

                assertEquals(expectedValue, actualValue)
            }

            @TestAllUndirectedGraphs
            fun `graph shouldn't change`(graph: UndirectedGraph<Int>) {
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
            @TestAllUndirectedGraphs
            fun `exception should be thrown`(graph: UndirectedGraph<Int>) {
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
                @TestAllUndirectedGraphs
                fun `Added edge should be returned`(graph: UndirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v0 = defaultVerticesList[0]
                    val v4 = defaultVerticesList[4]

                    val actualValue = graph.addEdge(v0, v4)
                    val expectedValue = graph.getEdge(v0, v4)

                    assertEquals(expectedValue, actualValue)
                }

                @TestAllUndirectedGraphs
                fun `Edge should be added to graph`(graph: UndirectedGraph<Int>) {
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

                @TestAllUndirectedGraphs
                fun `vertices have to be added to each other's adjacency map values`(graph: UndirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v0 = defaultVerticesList[0]
                    val v1 = defaultVerticesList[1]
                    val v2 = defaultVerticesList[2]
                    val v3 = defaultVerticesList[3]
                    val v4 = defaultVerticesList[4]

                    graph.addEdge(v0, v2)

                    val actualVertices1 = graph.getNeighbours(v0).toSet()
                    val expectedVertices1 = setOf(v1, v2)

                    val actualVertices2 = graph.getNeighbours(v2).toSet()
                    val expectedVertices2 = setOf(v0, v1, v3)

                    assertEquals(expectedVertices1, actualVertices1)
                    assertEquals(expectedVertices2, actualVertices2)
                }

                @TestAllUndirectedGraphs
                fun `edge has to be added to both vertices' outgoing edges map values`(graph: UndirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v0 = defaultVerticesList[0]
                    val v1 = defaultVerticesList[1]
                    val v2 = defaultVerticesList[2]
                    val v3 = defaultVerticesList[3]
                    val v4 = defaultVerticesList[4]

                    graph.addEdge(v3, v0)

                    val actualEdges1 = graph.getOutgoingEdges(v0).toSet()
                    val expectedEdges1 = setOf(graph.getEdge(v0, v1), graph.getEdge(v0, v3))

                    val actualEdges2 = graph.getOutgoingEdges(v3).toSet()
                    val expectedEdges2 = setOf(
                        graph.getEdge(v3, v0),
                        graph.getEdge(v3, v1),
                        graph.getEdge(v3, v2),
                        graph.getEdge(v3, v4)
                    )


                    assertEquals(expectedEdges1, actualEdges1)
                    assertEquals(expectedEdges2, actualEdges2)
                }

                @TestAllUndirectedGraphs
                fun `adding already existing edge shouldn't change graph`(graph: UndirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v1 = defaultVerticesList[1]
                    val v4 = defaultVerticesList[4]

                    graph.addEdge(v4, v1)

                    val expectedGraph = graphStructure
                    val actualGraph = graph.getVertices() to graph.getEdges().toSet()

                    assertEquals(expectedGraph, actualGraph)
                }

                @TestAllUndirectedGraphs
                fun `adding already existing edge shouldn't change adjacency map`(graph: UndirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v1 = defaultVerticesList[1]
                    val v4 = defaultVerticesList[4]

                    val expectedNeighbours1 = graph.getNeighbours(v1).toSet()
                    val expectedNeighbours2 = graph.getNeighbours(v4).toSet()

                    graph.addEdge(v4, v1)

                    val actualNeighbours1 = graph.getNeighbours(v1).toSet()
                    val actualNeighbours2 = graph.getNeighbours(v4).toSet()

                    assertEquals(expectedNeighbours1, actualNeighbours1)
                    assertEquals(expectedNeighbours2, actualNeighbours2)
                }

                @TestAllUndirectedGraphs
                fun `adding already existing edge shouldn't change outgoing edges map`(graph: UndirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v1 = defaultVerticesList[1]
                    val v4 = defaultVerticesList[4]

                    val expectedEdges1 = graph.getOutgoingEdges(v1).toSet()
                    val expectedEdges2 = graph.getOutgoingEdges(v4).toSet()

                    graph.addEdge(v4, v1)

                    val actualEdges1 = graph.getOutgoingEdges(v1).toSet()
                    val actualEdges2 = graph.getOutgoingEdges(v4).toSet()

                    assertEquals(expectedEdges1, actualEdges1)
                    assertEquals(expectedEdges2, actualEdges2)
                }

                @TestAllUndirectedGraphs
                fun `adding edge with reversed arguments shouldn't change graph`(graph: UndirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v1 = defaultVerticesList[1]
                    val v4 = defaultVerticesList[4]

                    graph.addEdge(v1, v4)

                    val expectedGraph = graphStructure
                    val actualGraph = graph.getVertices() to graph.getEdges().toSet()

                    assertEquals(expectedGraph, actualGraph)
                }

                @TestAllUndirectedGraphs
                fun `adding edge with reversed arguments shouldn't change adjacency map`(graph: UndirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v1 = defaultVerticesList[1]
                    val v4 = defaultVerticesList[4]

                    val expectedNeighbours1 = graph.getNeighbours(v1).toSet()
                    val expectedNeighbours2 = graph.getNeighbours(v4).toSet()

                    graph.addEdge(v1, v4)

                    val actualNeighbours1 = graph.getNeighbours(v1).toSet()
                    val actualNeighbours2 = graph.getNeighbours(v4).toSet()

                    assertEquals(expectedNeighbours1, actualNeighbours1)
                    assertEquals(expectedNeighbours2, actualNeighbours2)
                }

                @TestAllUndirectedGraphs
                fun `adding edge with reversed arguments shouldn't change outgoing edges map`(graph: UndirectedGraph<Int>) {
                    val graphStructure = setup(graph)
                    val defaultVerticesList = graphStructure.first

                    val v1 = defaultVerticesList[1]
                    val v4 = defaultVerticesList[4]

                    val expectedEdges1 = graph.getOutgoingEdges(v1).toSet()
                    val expectedEdges2 = graph.getOutgoingEdges(v4).toSet()

                    graph.addEdge(v1, v4)

                    val actualEdges1 = graph.getOutgoingEdges(v1).toSet()
                    val actualEdges2 = graph.getOutgoingEdges(v4).toSet()

                    assertEquals(expectedEdges1, actualEdges1)
                    assertEquals(expectedEdges2, actualEdges2)
                }
            }

            @Nested
            inner class `Vertices are the same` {
                @TestAllUndirectedGraphs
                fun `exception should be thrown`(graph: UndirectedGraph<Int>) {
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
            @TestAllUndirectedGraphs
            fun `first vertex isn't in the graph`(graph: UndirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]

                assertThrows(IllegalArgumentException::class.java) {
                    graph.addEdge(Vertex(2210, 2005), v0)
                }
            }

            @TestAllUndirectedGraphs
            fun `second vertex isn't in the graph`(graph: UndirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]

                assertThrows(IllegalArgumentException::class.java) {
                    graph.addEdge(v0, Vertex(2510, 1917))
                }
            }

            @TestAllUndirectedGraphs
            fun `both vertices aren't in the graph`(graph: UndirectedGraph<Int>) {
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
            @TestAllUndirectedGraphs
            fun `removed edge should be returned`(graph: UndirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v1 = defaultVerticesList[1]
                val v3 = defaultVerticesList[3]

                val edgeToRemove = graph.getEdge(v3, v1)

                val actualValue = graph.removeEdge(edgeToRemove)
                val expectedValue = edgeToRemove

                assertEquals(expectedValue, actualValue)
            }

            @TestAllUndirectedGraphs
            fun `order of the arguments shouldn't matter`(graph: UndirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v1 = defaultVerticesList[1]
                val v3 = defaultVerticesList[3]

                val edgeToRemove = graph.getEdge(v1, v3)

                val actualValue = graph.removeEdge(edgeToRemove)
                val expectedValue = edgeToRemove

                assertEquals(expectedValue, actualValue)
            }

            @TestAllUndirectedGraphs
            fun `edge should be removed from graph`(graph: UndirectedGraph<Int>) {
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

            @TestAllUndirectedGraphs
            fun `vertices should be removed from each other's adjacency map values`(graph: UndirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v1 = defaultVerticesList[1]
                val v2 = defaultVerticesList[2]
                val v3 = defaultVerticesList[3]
                val v4 = defaultVerticesList[4]

                val edgeToRemove = graph.getEdge(v1, v2)
                graph.removeEdge(edgeToRemove)

                val actualVertices1 = graph.getNeighbours(v1).toSet()
                val expectedVertices1 = setOf(v0, v3, v4)

                val actualVertices2 = graph.getNeighbours(v2).toSet()
                val expectedVertices2 = setOf(v3)

                assertEquals(expectedVertices1, actualVertices1)
                assertEquals(expectedVertices2, actualVertices2)
            }

            @TestAllUndirectedGraphs
            fun `edge should be removed from vertices' outgoing edges map values`(graph: UndirectedGraph<Int>) {
                val graphStructure = setup(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]
                val v1 = defaultVerticesList[1]
                val v2 = defaultVerticesList[2]
                val v3 = defaultVerticesList[3]
                val v4 = defaultVerticesList[4]

                val edgeToRemove = graph.getEdge(v1, v2)
                graph.removeEdge(edgeToRemove)

                val actualEdges1 = graph.getOutgoingEdges(v1).toSet()
                val expectedEdges1 = setOf(
                    graph.getEdge(v1, v0),
                    graph.getEdge(v1, v3),
                    graph.getEdge(v1, v4),
                )

                val actualEdges2 = graph.getOutgoingEdges(v2).toSet()
                val expectedEdges2 = setOf(graph.getEdge(v2, v3))

                assertEquals(expectedEdges1, actualEdges1)
                assertEquals(expectedEdges2, actualEdges2)
            }
        }

        @Nested
        inner class `Edge isn't in the graph` {
            @TestAllUndirectedGraphs
            fun `non-existing edge should throw an exception`(graph: UndirectedGraph<Int>) {
                assertThrows(NoSuchElementException::class.java) {
                    graph.removeEdge(Edge(Vertex(0,0), Vertex(1, 1)))
                }
            }
        }
    }

    @Nested
    inner class FindBridgesTest {
        @Nested
        inner class `All bridges should be found`() {
            @TestAllUndirectedGraphs
            fun `if graph has one edge`(graph: UndirectedGraph<Int>) {
                val vertex0 = graph.addVertex(0)
                val vertex1 = graph.addVertex(1)

                val expectedBridges = listOf(graph.addEdge(vertex0, vertex1))
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }

            @TestAllUndirectedGraphs
            fun `if two components are connected via one edge`(graph: UndirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)

                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)

                graph.apply {
                    addEdge(v0, v1)
                    addEdge(v0, v2)
                    addEdge(v1, v2)

                    addEdge(v3, v4)
                    addEdge(v3, v5)
                    addEdge(v4, v5)
                }

                val expectedBridges = listOf(graph.addEdge(v0, v3))
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }

            @TestAllUndirectedGraphs
            fun `if graph is chain-like`(graph: UndirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                val e01 = graph.addEdge(v0, v1)
                val e12 = graph.addEdge(v1, v2)
                val e23 = graph.addEdge(v2, v3)

                val expectedBridges = setOf(e01, e12, e23)
                val actualBridges = graph.findBridges().toSet()

                assertEquals(expectedBridges, actualBridges)
            }

            @TestAllUndirectedGraphs
            fun `if graph is star-like`(graph: UndirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                val e01 = graph.addEdge(v0, v1)
                val e02 = graph.addEdge(v0, v2)
                val e03 = graph.addEdge(v0, v3)

                val expectedBridges = setOf(e01, e02, e03)
                val actualBridges = graph.findBridges().toSet()

                assertEquals(expectedBridges, actualBridges)
            }
        }

        @Nested
        inner class `No bridge should be found`() {
            @TestAllUndirectedGraphs
            fun `if graph has no vertices`(graph: UndirectedGraph<Int>) {
                val expectedBridges = listOf<Edge<Int>>()
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }

            @TestAllUndirectedGraphs
            fun `if graph has no edges`(graph: UndirectedGraph<Int>) {
                graph.apply {
                    addVertex(0)
                    addVertex(1)
                    addVertex(2)
                }

                val expectedBridges = listOf<Edge<Int>>()
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }

            @TestAllUndirectedGraphs
            fun `if graph is circle-like`(graph: UndirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)

                graph.apply {
                    addEdge(v0, v1)
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v4)
                    addEdge(v4, v0)
                }

                val expectedBridges = listOf<Edge<Int>>()
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }

            @TestAllUndirectedGraphs
            fun `if two components are connected via more than one edge`(graph: UndirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)

                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)

                graph.apply {
                    addEdge(v0, v1)
                    addEdge(v0, v2)
                    addEdge(v1, v2)

                    addEdge(v3, v4)
                    addEdge(v3, v5)
                    addEdge(v4, v5)
                }

                graph.addEdge(v0, v3)
                graph.addEdge(v1, v4)

                val expectedBridges = listOf<Edge<Int>>()
                val actualBridges = graph.findBridges()

                assertEquals(expectedBridges, actualBridges)
            }
        }
    }
}
