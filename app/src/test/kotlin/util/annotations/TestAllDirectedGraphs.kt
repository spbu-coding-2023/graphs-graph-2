package util.annotations

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import util.annotations.argumentProviders.WeightedAndUnweightedDirectedGraphsProvider

@ParameterizedTest(name = "{0}")
@ArgumentsSource(WeightedAndUnweightedDirectedGraphsProvider::class)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestAllDirectedGraphs
