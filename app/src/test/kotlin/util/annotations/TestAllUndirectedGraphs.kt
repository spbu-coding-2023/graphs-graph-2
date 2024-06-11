package util.annotations

import util.annotations.argumentProviders.WeightedAndUnweightedUndirectedGraphsProvider
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

@ParameterizedTest(name = "{0}")
@ArgumentsSource(WeightedAndUnweightedUndirectedGraphsProvider::class)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestAllUndirectedGraphs
