package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import model.graphs.abstractGraph.Graph
import model.io.neo4j.Neo4jRepository
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import viewmodel.graph.GraphViewModel

class MainScreenViewModel<D>(
    graph: Graph<D>,
    currentGraphType: String,
    existingGraphViewModel: GraphViewModel<D>? = null
) {
    val showVerticesData = mutableStateOf(false)
    val showVerticesIds = mutableStateOf(false)
    val graphType = mutableStateOf(currentGraphType)

    fun setDirectionState(currentGraphType: String): MutableState<Boolean> {
        return mutableStateOf(currentGraphType.contains("Directed"))
    }

    fun setWeightinessState(currentGraphType: String): MutableState<Boolean> {
        return mutableStateOf(currentGraphType.contains("Weighted"))
    }

    var graphViewModel: GraphViewModel<D> = existingGraphViewModel
        ?: GraphViewModel(
            graph,
            showVerticesIds,
            showVerticesData,
            graphType,
            setDirectionState(currentGraphType),
            setWeightinessState(currentGraphType)
        )
}
