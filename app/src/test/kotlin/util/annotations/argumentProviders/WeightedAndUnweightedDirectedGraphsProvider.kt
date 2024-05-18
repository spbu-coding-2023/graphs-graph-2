package util.annotations.argumentProviders

import model.DirectedGraph
import model.WeightedDirectedGraph
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import java.util.stream.Stream

class WeightedAndUnweightedDirectedGraphsProvider : ArgumentsProvider {
    override fun provideArguments(context: ExtensionContext?): Stream<Arguments> = Stream.of(
        Arguments.of(DirectedGraph<Int>()),
        Arguments.of(WeightedDirectedGraph<Int>())
    )
}
