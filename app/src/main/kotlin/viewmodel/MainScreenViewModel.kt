package viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import model.graphs.abstractGraph.Graph
import viewmodel.graph.GraphViewModel

class MainScreenViewModel<D>(graph: Graph<D>, currentGraphType: String) {
    val showVerticesData = mutableStateOf(false)
    val showVerticesIds = mutableStateOf(false)
    val graphType = mutableStateOf(currentGraphType)

    fun setDirectionState(currentGraphType: String): MutableState<Boolean> {
        if (currentGraphType.contains("Directed")) return mutableStateOf(true)
        return mutableStateOf(false)
    }

    val graphViewModel =
        GraphViewModel(graph, showVerticesIds, showVerticesData, graphType, setDirectionState(currentGraphType))
}
