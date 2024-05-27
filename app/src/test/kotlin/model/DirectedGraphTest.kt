package model

import model.graphs.abstractGraph.Edge
import model.graphs.abstractGraph.Vertex
import model.graphs.DirectedGraph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import util.annotations.TestAllDirectedGraphs
import util.emptyEdgesSet
import util.emptyVerticesList
import util.setupAbstractGraph

class DirectedGraphTest {
    @Nested
    inner class GetEdgeTest {
        @Nested
        inner class `Edge is in the graph` {
            @TestAllDirectedGraphs
            fun `edge should be returned`(graph: DirectedGraph<Int>) {
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                    val graphStructure = setupAbstractGraph(graph)
                    val defaultVerticesList = graphStructure.first

                    val v0 = defaultVerticesList[0]
                    val v4 = defaultVerticesList[4]

                    val actualValue = graph.addEdge(v0, v4)
                    val expectedValue = graph.getEdge(v0, v4)

                    assertEquals(expectedValue, actualValue)
                }

                @TestAllDirectedGraphs
                fun `Edge should be added to graph`(graph: DirectedGraph<Int>) {
                    val graphStructure = setupAbstractGraph(graph)
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
                    val graphStructure = setupAbstractGraph(graph)
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
                    val graphStructure = setupAbstractGraph(graph)
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
                    val graphStructure = setupAbstractGraph(graph)
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
                    val graphStructure = setupAbstractGraph(graph)
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
                    val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
                val defaultVerticesList = graphStructure.first

                val v0 = defaultVerticesList[0]

                assertThrows(IllegalArgumentException::class.java) {
                    graph.addEdge(Vertex(2210, 2005), v0)
                }
            }

            @TestAllDirectedGraphs
            fun `second vertex isn't in the graph`(graph: DirectedGraph<Int>) {
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                val graphStructure = setupAbstractGraph(graph)
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
                    graph.removeEdge(Edge(Vertex(0, 0), Vertex(1, 1)))
                }
            }
        }
    }

    @Nested
    inner class FindSCCTest {
        @Nested
        inner class `SCC should return not empty array` {
            @TestAllDirectedGraphs
            fun `graph has two connected vertices`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)

                graph.addEdge(v1, v2)
                graph.addEdge(v2, v1)

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v1, v2))

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `complex graph`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v1)
                    addEdge(v3, v4)
                }

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v1, v2, v3), mutableSetOf(v4))

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `graph has multiple SCCs`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v1)
                    addEdge(v3, v4)
                    addEdge(v4, v3)
                    addEdge(v5, v1)
                }

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v3, v4), mutableSetOf(v1, v2), mutableSetOf(v5))

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `graph with nested cycles`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)
                val v6 = graph.addVertex(6)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v1)
                    addEdge(v3, v4)
                    addEdge(v4, v5)
                    addEdge(v5, v6)
                    addEdge(v6, v4)
                }

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v1, v2, v3), mutableSetOf(v4, v5, v6))

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `graph with cross connections`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)
                val v6 = graph.addVertex(6)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v1)
                    addEdge(v3, v4)
                    addEdge(v4, v5)
                    addEdge(v5, v6)
                    addEdge(v6, v4)
                }

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v1, v2, v3), mutableSetOf(v4, v5, v6))

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `graph with disconnected subgraphs`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)
                val v6 = graph.addVertex(6)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v1)
                    addEdge(v3, v4)
                    addEdge(v4, v3)
                    addEdge(v5, v6)
                    addEdge(v6, v5)
                }

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v1, v2), mutableSetOf(v3, v4), mutableSetOf(v5, v6))
                assertEquals(expectedValue, actualValue)
            }

            @Disabled("Our model doesn't support edge from vertex to itself, check DirectedGraph.kt")
            @TestAllDirectedGraphs
            fun `graph with single vertex cycle`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v1)
                    addEdge(v3, v3)
                }

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v1, v2, v3))

                assertEquals(expectedValue, actualValue)
            }
        }

        @Nested
        inner class `SCC should return single-element SCCs` {
            @TestAllDirectedGraphs
            fun `graph has single vertex`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v1))

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `graph with multiple disconnected vertices`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)

                val actualValue = graph.findSCC()
                val expectedValue =
                    mutableSetOf(
                        mutableSetOf(v1),
                        mutableSetOf(v2),
                        mutableSetOf(v3),
                        mutableSetOf(v4)
                    )

                assertEquals(expectedValue, actualValue)
            }
        }

        @Nested
        inner class `Additional edge cases`() {
            @TestAllDirectedGraphs
            fun `empty graph`(graph: DirectedGraph<Int>) {

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf<MutableSet<Vertex<Int>>>()

                assertEquals(expectedValue, actualValue)
            }

            @Disabled("Our model doesn't support edge from vertex to itself, check DirectedGraph.kt")
            @TestAllDirectedGraphs
            fun `graph with self-loops`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)

                graph.addEdge(v1, v1)
                graph.addEdge(v2, v2)

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v1), mutableSetOf(v2))

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `linear graph`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.addEdge(v1, v2)
                graph.addEdge(v2, v3)

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v3), mutableSetOf(v2), mutableSetOf(v1))

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `graph with cycles and tail`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v1)
                    addEdge(v4, v3)
                    addEdge(v4, v5)
                }

                val actualValue = graph.findSCC()
                val expectedValue = mutableSetOf(mutableSetOf(v1, v2, v3), mutableSetOf(v4), mutableSetOf(v5))

                assertEquals(expectedValue, actualValue)
            }
        }

        @Nested
        inner class `Side-effects check` {
            @TestAllDirectedGraphs
            fun `check vertices in complex graph`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v1)
                    addEdge(v3, v4)
                }

                val expectedValue = graph.getVertices()
                graph.findSCC()
                val actualValue = graph.getVertices()

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `check edges in complex graph`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v1)
                    addEdge(v3, v4)
                }

                val expectedValue = graph.getEdges()
                graph.findSCC()
                val actualValue = graph.getEdges()

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `check edges in graph with cycles and tail`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v1)
                    addEdge(v4, v3)
                    addEdge(v4, v5)
                }

                val expectedValue = graph.getEdges()
                graph.findSCC()
                val actualValue = graph.getEdges()

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `check vertices graph with cycles and tail`(graph: DirectedGraph<Int>) {
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v3, v1)
                    addEdge(v4, v3)
                    addEdge(v4, v5)
                }

                val expectedValue = graph.getVertices()
                graph.findSCC()
                val actualValue = graph.getVertices()

                assertEquals(expectedValue, actualValue)
            }
        }
    }

    @Nested
    inner class FindKeyVerticesTest {
        @Nested
        inner class `One vertex is picked over another`() {
            @TestAllDirectedGraphs
            fun `if it can reach more vertices`(graph: DirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v0, v1)
                    addEdge(v0, v2)
                    addEdge(v0, v3)
                    addEdge(v1, v2)
                }

                val expectedResult = setOf(v0)
                val actualResult = graph.findKeyVertices()

                assertEquals(expectedResult, actualResult)
            }

            @TestAllDirectedGraphs
            fun `if it can reach other vertices with fewer edges`(graph: DirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                graph.apply {
                    addEdge(v1, v2)
                    addEdge(v2, v3)
                    addEdge(v0, v2)
                    addEdge(v0, v3)
                }

                val expectedResult = setOf(v0)
                val actualResult = graph.findKeyVertices()

                assertEquals(expectedResult, actualResult)
            }
        }
    }

    @Nested
    inner class FindCyclesTest {
        @Nested
        inner class `There are some cycles` {
            @TestAllDirectedGraphs
            fun `all cycles should be returned`(graph: DirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)
                val v4 = graph.addVertex(4)
                val v5 = graph.addVertex(5)
                val v6 = graph.addVertex(6)
                val v7 = graph.addVertex(7)
                val v8 = graph.addVertex(8)

                val e01 = graph.addEdge(v0, v1)
                val e07 = graph.addEdge(v0, v7)
                val e04 = graph.addEdge(v0, v4)
                val e18 = graph.addEdge(v1, v8)
                val e12 = graph.addEdge(v1, v2)
                val e20 = graph.addEdge(v2, v0)
                val e21 = graph.addEdge(v2, v1)
                val e25 = graph.addEdge(v2, v5)
                val e23 = graph.addEdge(v2, v3)
                val e53 = graph.addEdge(v5, v3)
                val e34 = graph.addEdge(v3, v4)
                val e41 = graph.addEdge(v4, v1)
                val e78 = graph.addEdge(v7, v8)
                val e87 = graph.addEdge(v8, v7)

                val expectedCycle1 = listOf(e12 to v2, e21 to v1)
                val expectedCycle2 = listOf(e12 to v2, e20 to v0, e01 to v1)
                val expectedCycle3 = listOf(e12 to v2, e20 to v0, e04 to v4, e41 to v1)
                val expectedCycle4 = listOf(e12 to v2, e23 to v3, e34 to v4, e41 to v1)
                val expectedCycle5 = listOf(e12 to v2, e25 to v5, e53 to v3, e34 to v4, e41 to v1)

                val actualValue = graph.findCycles(v1)
                val expectedValue =
                    setOf(
                        expectedCycle1,
                        expectedCycle2,
                        expectedCycle3,
                        expectedCycle4,
                        expectedCycle5
                    )

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `SCC of 2 vertices should have one cycle`(graph: DirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)
                val v2 = graph.addVertex(2)
                val v3 = graph.addVertex(3)

                val e01 = graph.addEdge(v0, v1)
                val e12 = graph.addEdge(v1, v2)
                val e21 = graph.addEdge(v2, v1)
                val e23 = graph.addEdge(v2, v3)

                val actualValue = graph.findCycles(v1)
                val expectedValue = setOf(listOf(e12 to v2, e21 to v1))

                assertEquals(expectedValue, actualValue)
            }
        }

        @Nested
        inner class `There are no cycles` {
            @TestAllDirectedGraphs
            fun `vertex without outgoing edges shouldn't have cycles`(graph: DirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)

                graph.addEdge(v0, v1)

                val actualValue = graph.findCycles(v1)
                val expectedValue = emptySet<List<Pair<Edge<Int>, Vertex<Int>>>>()

                assertEquals(expectedValue, actualValue)
            }

            @TestAllDirectedGraphs
            fun `SCC of 1 vertex shouldn't have cycles`(graph: DirectedGraph<Int>) {
                val v0 = graph.addVertex(0)
                val v1 = graph.addVertex(1)

                graph.addEdge(v0, v1)

                val actualValue = graph.findCycles(v0)
                val expectedValue = emptySet<List<Pair<Edge<Int>, Vertex<Int>>>>()

                assertEquals(expectedValue, actualValue)
            }
        }

        @TestAllDirectedGraphs
        fun `graph shouldn't change`(graph: DirectedGraph<Int>) {
            val v0 = graph.addVertex(0)
            val v1 = graph.addVertex(1)
            val v2 = graph.addVertex(2)
            val v3 = graph.addVertex(3)
            val v4 = graph.addVertex(4)
            val v5 = graph.addVertex(5)
            val v6 = graph.addVertex(6)
            val v7 = graph.addVertex(7)
            val v8 = graph.addVertex(8)

            val e01 = graph.addEdge(v0, v1)
            val e07 = graph.addEdge(v0, v7)
            val e04 = graph.addEdge(v0, v4)
            val e18 = graph.addEdge(v1, v8)
            val e12 = graph.addEdge(v1, v2)
            val e20 = graph.addEdge(v2, v0)
            val e21 = graph.addEdge(v2, v1)
            val e25 = graph.addEdge(v2, v5)
            val e23 = graph.addEdge(v2, v3)
            val e53 = graph.addEdge(v5, v3)
            val e34 = graph.addEdge(v3, v4)
            val e41 = graph.addEdge(v4, v1)
            val e78 = graph.addEdge(v7, v8)
            val e87 = graph.addEdge(v8, v7)

            val expectedGraph = graph.getVertices() to graph.getEdges().toSet()

            graph.findCycles(v1)

            val actualGraph = graph.getVertices() to graph.getEdges().toSet()

            assertEquals(expectedGraph, actualGraph)
        }
    }
}
