package util.annotations

import util.annotations.argumentProviders.WeightedAndUnweightedDirectedGraphsProvider
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

@ParameterizedTest(name = "{0}")
@ArgumentsSource(WeightedAndUnweightedDirectedGraphsProvider::class)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestAllDirectedGraphs
