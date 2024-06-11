package viewmodel.graph

import model.io.sql.SQLDatabaseModule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import model.graphs.DirectedGraph
import model.graphs.UndirectedGraph
import model.graphs.WeightedDirectedGraph
import model.graphs.WeightedUndirectedGraph
import model.graphs.abstractGraph.Graph
import view.MainScreen
import viewmodel.MainScreenViewModel

class SetupGraphViewModel {
    sealed class GraphType {
        data object Integer : GraphType()
        data object UInteger : GraphType()
        data object String : GraphType()
    }

    sealed class GraphStructure {
        data object Directed : GraphStructure()
        data object Undirected : GraphStructure()
    }

    sealed class Weight {
        data object Weighted : Weight()
        data object Unweighted : Weight()
    }

    // Simplified function to create a GraphViewModel based on the graph type
    fun createGraphViewModel(
        storedData: GraphType,
        graphStructure: GraphStructure,
        weight: Weight
    ): Graph<out Comparable<*>> {
        return when {
            weight is Weight.Weighted && graphStructure is GraphStructure.Directed && storedData is GraphType.Integer -> WeightedDirectedGraph<Int>()
            weight is Weight.Weighted && graphStructure is GraphStructure.Directed && storedData is GraphType.UInteger -> WeightedDirectedGraph<UInt>()
            weight is Weight.Weighted && graphStructure is GraphStructure.Directed && storedData is GraphType.String -> WeightedDirectedGraph<String>()
            weight is Weight.Weighted && graphStructure is GraphStructure.Undirected && storedData is GraphType.Integer -> WeightedUndirectedGraph<Int>()
            weight is Weight.Weighted && graphStructure is GraphStructure.Undirected && storedData is GraphType.UInteger -> WeightedUndirectedGraph<UInt>()
            weight is Weight.Weighted && graphStructure is GraphStructure.Undirected && storedData is GraphType.String -> WeightedUndirectedGraph<String>()
            weight is Weight.Unweighted && graphStructure is GraphStructure.Directed && storedData is GraphType.Integer -> DirectedGraph<Int>()
            weight is Weight.Unweighted && graphStructure is GraphStructure.Directed && storedData is GraphType.UInteger -> DirectedGraph<UInt>()
            weight is Weight.Unweighted && graphStructure is GraphStructure.Directed && storedData is GraphType.String -> DirectedGraph<String>()
            weight is Weight.Unweighted && graphStructure is GraphStructure.Undirected && storedData is GraphType.Integer -> UndirectedGraph<Int>()
            weight is Weight.Unweighted && graphStructure is GraphStructure.Undirected && storedData is GraphType.UInteger -> UndirectedGraph<UInt>()
            weight is Weight.Unweighted && graphStructure is GraphStructure.Undirected && storedData is GraphType.String -> UndirectedGraph<String>()
            else -> throw IllegalArgumentException("Invalid combination of parameters")
        }
    }

    @Composable
    fun createGraphAndApplyScreen(
        storedData: GraphType,
        graphStructure: GraphStructure,
        weight: Weight
    ) {
        val graph = createGraphViewModel(storedData, graphStructure, weight)
        MainScreen(MainScreenViewModel(graph, storedData.toString()))
    }

    @Suppress("UNCHECKED_CAST")
    fun createGraphObject(
        storedData: GraphType,
        graphStructure: GraphStructure,
        weight: Weight,
        graphId: Int,
        graphVMState: MutableState<GraphViewModel<out Comparable<*>>?>
    ) {
        val graph = createGraphViewModel(storedData, graphStructure, weight) as Graph<Comparable<Any>>
        graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(graph, graphId, graphVMState as MutableState<GraphViewModel<Comparable<Any>>?>)
    }
}

// Utility function to get the graph parameters
fun getGraphVMParameter(
    storedDataType: Int,
    structureType: Int,
    weightType: Int
): Triple<SetupGraphViewModel.GraphType, SetupGraphViewModel.GraphStructure, SetupGraphViewModel.Weight> {
    val storedData = when (storedDataType) {
        0 -> SetupGraphViewModel.GraphType.Integer
        1 -> SetupGraphViewModel.GraphType.UInteger
        2 -> SetupGraphViewModel.GraphType.String
        else -> SetupGraphViewModel.GraphType.Integer // default to integer
    }

    val graphStructure = when (structureType) {
        0 -> SetupGraphViewModel.GraphStructure.Undirected
        1 -> SetupGraphViewModel.GraphStructure.Directed
        else -> SetupGraphViewModel.GraphStructure.Undirected // default to directed
    }

    val weight = when (weightType) {
        0 -> SetupGraphViewModel.Weight.Unweighted
        1 -> SetupGraphViewModel.Weight.Weighted
        else -> SetupGraphViewModel.Weight.Unweighted // default to weighted
    }

    return Triple(storedData, graphStructure, weight)
}

@Composable
fun createGraphFromTypesIndices(
    viewModel: SetupGraphViewModel,
    storedDataIndex: Int,
    orientationIndex: Int,
    weightnessIndex: Int
) {
    val (storedData, graphStructure, weight) = getGraphVMParameter(storedDataIndex, orientationIndex, weightnessIndex)
    viewModel.createGraphAndApplyScreen(storedData, graphStructure, weight)
}
