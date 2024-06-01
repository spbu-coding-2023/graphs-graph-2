package util.annotations

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ArgumentsSource
import util.annotations.argumentProviders.AllGraphTypesProvider

@ParameterizedTest(name = "{0}")
@ArgumentsSource(AllGraphTypesProvider::class)
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TestAllGraphTypes
