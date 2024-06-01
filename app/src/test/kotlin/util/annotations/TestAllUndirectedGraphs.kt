package util.annotations

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import util.annotations.argumentProviders.WeightedAndUnweightedUndirectedGraphsProvider

@ParameterizedTest(name = "{0}")
@ArgumentsSource(WeightedAndUnweightedUndirectedGraphsProvider::class)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestAllUndirectedGraphs
