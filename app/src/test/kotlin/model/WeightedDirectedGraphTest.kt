package model

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested

class WeightedDirectedGraphTest {
    private lateinit var graph: WeightedDirectedGraph<Int>

    @BeforeEach
    fun init() {
        graph = WeightedDirectedGraph()
    }

    @Nested
    inner class GetWeightTest {}

    @Nested
    inner class AddEdgeTest {}

    @Nested
    inner class RemoveEdgeTest {}

    @Nested
    inner class FindShortestPathDijkstraTest {}
}
