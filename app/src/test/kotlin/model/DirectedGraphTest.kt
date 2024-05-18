package model

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import java.util.stream.Stream

@ParameterizedTest(name = "{0}")
@ArgumentsSource(WeightedAndUnweightedDirectedGraphsProvider::class)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
private annotation class TestBothDirectedGraphs

class WeightedAndUnweightedDirectedGraphsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<Arguments> = Stream.of(
        Arguments.of(DirectedGraph<Int>()),
        Arguments.of(WeightedDirectedGraph<Int>())
    )
}

class DirectedGraphTest {
    @Nested
    inner class GetEdgeTest {}

    @Nested
    inner class GetNeighboursTest {}

    @Nested
    inner class GetOutgoingEdgesTest {}

    @Nested
    inner class AddEdgeTest {}

    @Nested
    inner class RemoveEdgeTest {}

    @Nested
    inner class FindSCCTest {}
}
