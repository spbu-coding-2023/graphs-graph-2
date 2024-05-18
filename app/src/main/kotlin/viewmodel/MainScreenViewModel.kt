package viewmodel

import androidx.compose.runtime.mutableStateOf
import model.abstractGraph.Graph
import viewmodel.graph.GraphViewModel
import viewmodel.graph.TestRepresentation

class MainScreenViewModel<D>(graph: Graph<D>, currentGraphType: String) {
    val showVerticesData = mutableStateOf(false)
    val showVerticesIds = mutableStateOf(false)
    val graphType = mutableStateOf(currentGraphType)

    val graphViewModel = GraphViewModel(graph, showVerticesIds, showVerticesData, graphType)

    init { // here will be a placement-function call
        TestRepresentation().place(740.0, 650.0, graphViewModel.verticesVM)
    }

    //    fun setEdgeColor
    //
    //    fun setVerticesColor()

}
