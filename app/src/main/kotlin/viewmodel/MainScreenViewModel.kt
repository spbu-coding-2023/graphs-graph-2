package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import model.graphs.abstractGraph.Graph
import viewmodel.graph.GraphViewModel

class MainScreenViewModel<D>(
    graph: Graph<D>,
    dataType: String,
    existingGraphViewModel: GraphViewModel<D>? = null,
) {
    private val showVerticesData = mutableStateOf(false)
    private val showVerticesIds = mutableStateOf(false)
    val graphType = mutableStateOf(simplifyGraphString(graph.toString()) + "\nData type: " + dataType)

    private fun simplifyGraphString(graphString: String): String {
        return graphString.substringAfterLast('.').substringBefore('@')
    }

    private fun setDirectionState(currentGraphType: String): MutableState<Boolean> {
        return mutableStateOf(currentGraphType.contains("Directed"))
    }

    private fun setWeightinessState(currentGraphType: String): MutableState<Boolean> {
        return mutableStateOf(currentGraphType.contains("Weighted"))
    }

    var graphViewModel: GraphViewModel<D> = existingGraphViewModel
        ?: GraphViewModel(
            graph,
            showVerticesIds,
            showVerticesData,
            graphType,
            setDirectionState(graph.toString()),
            setWeightinessState(graph.toString())
        )
}
