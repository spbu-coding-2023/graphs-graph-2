package util.annotations.argumentProviders

import java.util.stream.Stream
import model.graphs.UndirectedGraph
import model.graphs.WeightedUndirectedGraph
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider

class WeightedAndUnweightedUndirectedGraphsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<Arguments> =
        Stream.of(Arguments.of(UndirectedGraph<Int>()), Arguments.of(WeightedUndirectedGraph<Int>()))
}
