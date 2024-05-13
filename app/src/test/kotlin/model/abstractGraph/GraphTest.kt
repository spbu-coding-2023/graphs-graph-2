package model.abstractGraph

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
