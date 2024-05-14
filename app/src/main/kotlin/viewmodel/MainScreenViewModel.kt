package viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import model.abstractGraph.Graph
import viewmodel.graph.GraphViewModel
import viewmodel.graph.TestRepresentation

class MainScreenViewModel<D>(graph: Graph<D>) {
    val showVerticesData = mutableStateOf(false)
    val showVerticesIds = mutableStateOf(false)
    val graphViewModel =
        GraphViewModel(
            graph,
            showVerticesIds,
            showVerticesData,
            WindowViewModel(mutableStateOf(1200.dp), mutableStateOf(700.dp))
        )

    init { // here will be a placement-function call
        TestRepresentation().place(740.0, 650.0, graphViewModel.verticesVM)
    }

    //    fun setEdgeColor
    //
    //    fun setVerticesColor()

}