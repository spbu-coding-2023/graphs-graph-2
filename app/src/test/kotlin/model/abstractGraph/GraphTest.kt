package model.abstractGraph

import model.abstractGraph.Graph
import model.DirectedGraph
import model.UndirectedGraph
import model.WeightedDirectedGraph
import model.WeightedUndirectedGraph
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
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

class GraphTest {
    @Nested
    inner class GetVerticesTest {}

    @Nested
    inner class GetEdgesTest {}

    @Nested
    inner class AddVertexTest {}

    @Nested
    inner class RemoveVertexTest {}
}
