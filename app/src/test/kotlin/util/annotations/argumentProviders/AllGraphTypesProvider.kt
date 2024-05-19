package util.annotations.argumentProviders

import model.DirectedGraph
import model.UndirectedGraph
import model.WeightedDirectedGraph
import model.WeightedUndirectedGraph
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

class AllGraphTypesProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<Arguments> = Stream.of(
        Arguments.of(UndirectedGraph<Int>()),
        Arguments.of(DirectedGraph<Int>()),
        Arguments.of(WeightedUndirectedGraph<Int>()),
        Arguments.of(WeightedDirectedGraph<Int>())
    )
}