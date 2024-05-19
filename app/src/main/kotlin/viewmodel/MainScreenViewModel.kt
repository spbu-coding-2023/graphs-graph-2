package viewmodel

import androidx.compose.runtime.mutableStateOf
import model.abstractGraph.Graph
import viewmodel.graph.GraphViewModel
import viewmodel.graph.TFDPLayout

class MainScreenViewModel<D>(graph: Graph<D>, currentGraphType: String) {
    val showVerticesData = mutableStateOf(false)
    val showVerticesIds = mutableStateOf(false)
    val isGraphDirected = mutableStateOf(false)
    val graphType = mutableStateOf(currentGraphType)

    val graphViewModel = GraphViewModel(graph, showVerticesIds, showVerticesData, graphType, isGraphDirected)

    init { // here will be a placement-function call
        TFDPLayout().place(740.0, 650.0, graphViewModel.verticesVM)
    }

    //    fun setEdgeColor
    //
    //    fun setVerticesColor()

}
