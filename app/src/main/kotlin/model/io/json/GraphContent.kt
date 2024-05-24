package model.io.json

import kotlinx.serialization.Serializable

@Serializable
data class GraphContent<D>(
    val isDirected: Boolean,
    val isWeighted: Boolean,
    val verticesData: List<D>,
    val edgesEndsIds: Map<Pair<Int, Int>, Int?> // values - edge weights, null if graph is unweighted
)
