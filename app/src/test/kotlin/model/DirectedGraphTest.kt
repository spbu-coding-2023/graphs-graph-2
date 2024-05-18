package model

import org.junit.jupiter.api.Nested
import util.annotations.TestAllDirectedGraphs
import util.setup

class DirectedGraphTest {
    @Nested
    inner class GetEdgeTest {}

    @Nested
    inner class GetNeighboursTest {}

    @Nested
    inner class GetOutgoingEdgesTest {}

    @Nested
    inner class AddEdgeTest {}

    @Nested
    inner class RemoveEdgeTest {}

    @Nested
    inner class FindSCCTest {}
}
