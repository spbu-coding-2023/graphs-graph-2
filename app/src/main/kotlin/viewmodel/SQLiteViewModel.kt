package viewmodel

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import model.graphs.abstractGraph.Graph
import model.io.sql.SQLDatabaseModule
import view.MainScreen
import view.components.dialogWindows.ErrorWindow
import viewmodel.graph.GraphViewModel
import viewmodel.graph.GraphViewModelFactory
import java.sql.SQLException
import kotlin.system.exitProcess

@Suppress("UNCHECKED_CAST")
@Composable
fun <D> importGraphAndRender(graphId: Int) {
    val graphVMState = remember { mutableStateOf<GraphViewModel<D>?>(null) }
    var showErrorMessage by remember { mutableStateOf(false) }
    var updateIsRequired by remember { mutableStateOf(false) }
    var currentGraphSetup: Pair<Triple<GraphViewModelFactory.GraphType,
            GraphViewModelFactory.GraphStructure, GraphViewModelFactory.Weight>, String>? = null

    try {
        currentGraphSetup = SQLDatabaseModule.importGraph<D>(graphId, currentGraphSetup)
        if (currentGraphSetup == null) showErrorMessage = true

        // Execute side-effect to create graph object
        GraphViewModelFactory.createGraphObject(
            currentGraphSetup?.first?.first as GraphViewModelFactory.GraphType,
            currentGraphSetup?.first?.second as GraphViewModelFactory.GraphStructure,
            currentGraphSetup?.first?.third as GraphViewModelFactory.Weight,
            graphId,
            graphVMState as MutableState<GraphViewModel<out Comparable<*>>?>
        )
        updateIsRequired = true

    } catch (e: SQLException) {
        e.printStackTrace()
        showErrorMessage = true
    }

    if (updateIsRequired) return importGraphUI(showErrorMessage, graphVMState, graphId)
}

@Composable
fun <D> importGraphUI(
    showErrorMessage: Boolean,
    graphVMState: MutableState<GraphViewModel<D>?>,
    graphId: Int
) {
    if (showErrorMessage) {
        ErrorWindow("Graph with ID $graphId not found.") {}
    }
    if (graphVMState.value != null) {
        graphVMState.value?.updateIsRequired?.value = true

        MainScreen(
            MainScreenViewModel(
                graphVMState.value?.graph as Graph<D>,
                graphVMState.value?.graphType as String,
                graphVMState.value
            )
        )

    } else CircularProgressIndicator()
}