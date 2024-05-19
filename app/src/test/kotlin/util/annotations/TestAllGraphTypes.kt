package util.annotations

import util.annotations.argumentProviders.AllGraphTypesProvider
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource

@ParameterizedTest(name = "{0}")
@ArgumentsSource(AllGraphTypesProvider::class)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestAllGraphTypes

