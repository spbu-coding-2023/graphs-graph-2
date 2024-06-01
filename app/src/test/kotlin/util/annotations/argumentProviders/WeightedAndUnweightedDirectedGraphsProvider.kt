package util.annotations.argumentProviders

import java.util.stream.Stream
import model.graphs.DirectedGraph
import model.graphs.WeightedDirectedGraph
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider

class WeightedAndUnweightedDirectedGraphsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<Arguments> =
        Stream.of(Arguments.of(DirectedGraph<Int>()), Arguments.of(WeightedDirectedGraph<Int>()))
}
