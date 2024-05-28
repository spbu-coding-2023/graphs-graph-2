package model.algorithms.clustering

import model.graphs.abstractGraph.Graph
import model.graphs.abstractGraph.Vertex
import org.junit.jupiter.api.Assertions.assertEquals
import util.annotations.TestAllGraphTypes
import util.setupAbstractGraph

class CommunitiesFinderTest {
    val louvain = CommunitiesFinder()

    @TestAllGraphTypes
    fun `graph of 1 vertex should have one community`(graph: Graph<Int>) {
        val v0 = graph.addVertex(0)

        val actualValue = louvain.findCommunity(graph)
        val expectedValue = setOf(setOf(v0))

        assertEquals(expectedValue, actualValue)
    }

    @TestAllGraphTypes
    fun `empty graph should have no communities`(graph: Graph<Int>) {
        val actualValue = louvain.findCommunity(graph)
        val expectedValue = emptySet<Set<Vertex<Int>>>()

        assertEquals(expectedValue, actualValue)
    }

    @TestAllGraphTypes
    fun `graph doesn't change`(graph: Graph<Int>) {
        val graphStructure = setupAbstractGraph(graph)

        louvain.findCommunity(graph)

        val actualGraph = graph.getVertices() to graph.getEdges().toSet()
        val expectedGraph = graphStructure

        assertEquals(expectedGraph, actualGraph)
    }
}
