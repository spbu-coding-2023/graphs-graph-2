package viewmodel.graph

import model.io.sql.SQLDatabaseModule
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import model.graphs.DirectedGraph
import model.graphs.UndirectedGraph
import model.graphs.WeightedDirectedGraph
import model.graphs.WeightedUndirectedGraph
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

    @Composable
    fun createGraphAndApplyScreen(
        storedData: GraphType,
        graphStructure: GraphStructure,
        weight: Weight
    ) { // TODO looks too shitty (((((((((( + mb any could be changed
        return when (weight) {
            is Weight.Weighted -> {
                when (graphStructure) {
                    is GraphStructure.Directed -> {
                        when (storedData) {
                            is GraphType.Integer -> MainScreen(MainScreenViewModel(
                                WeightedDirectedGraph<Int>(),
                                "WeightedDirectedGraph Int"))
                            is GraphType.UInteger -> MainScreen(MainScreenViewModel(
                                WeightedDirectedGraph<UInt>(),
                                "WeightedDirectedGraph UInt"))
                            is GraphType.String -> MainScreen(MainScreenViewModel(
                                WeightedDirectedGraph<String>(),
                                "WeightedDirectedGraph String"))
                        }
                    }

                    is GraphStructure.Undirected -> {
                        when (storedData) {
                            is GraphType.Integer -> MainScreen(MainScreenViewModel(
                                WeightedUndirectedGraph<Int>(),
                                "WeightedUndirectedGraph Int"))
                            is GraphType.UInteger -> MainScreen(MainScreenViewModel(
                                WeightedUndirectedGraph<UInt>(),
                                "WeightedUndirectedGraph UInt"))
                            is GraphType.String -> MainScreen(MainScreenViewModel(
                                WeightedUndirectedGraph<String>(),
                                "WeightedUndirectedGraph String"))
                        }
                    }
                }
            }

            is Weight.Unweighted -> {
                when (graphStructure) {
                    is GraphStructure.Directed -> {
                        when (storedData) {
                            is GraphType.Integer -> MainScreen(MainScreenViewModel(
                                DirectedGraph<Int>(),
                                "DirectedGraph Int"))
                            is GraphType.UInteger -> MainScreen(MainScreenViewModel(
                                DirectedGraph<UInt>(),
                                "DirectedGraph UInt"))
                            is GraphType.String -> MainScreen(MainScreenViewModel(
                                DirectedGraph<String>(),
                                "DirectedGraph String"))
                        }
                    }

                    is GraphStructure.Undirected -> {
                        when (storedData) {
                            is GraphType.Integer -> MainScreen(MainScreenViewModel(
                                UndirectedGraph<Int>(),
                                "UndirectedGraph Int"))
                            is GraphType.UInteger -> MainScreen(MainScreenViewModel(
                                UndirectedGraph<UInt>(),
                                "UndirectedGraph UInt"))
                            is GraphType.String ->
                                MainScreen(MainScreenViewModel(
                                UndirectedGraph<String>(),
                                "UndirectedGraph String"))
                        }
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <D>createGraphObject(
        storedData: GraphType,
        graphStructure: GraphStructure,
        weight: Weight,
        graphId: Int,
        graphVMState: MutableState<GraphViewModel<D>?>
    ) = when (weight) {
        is Weight.Weighted -> {
            when (graphStructure) {
                is GraphStructure.Directed -> {
                    when (storedData) {
                        is GraphType.Integer -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(WeightedDirectedGraph(),
                                graphId, graphVMState as MutableState<GraphViewModel<Int>?>) as GraphViewModel<D>?
                        }
                        is GraphType.UInteger -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(WeightedDirectedGraph(),
                                graphId, graphVMState as MutableState<GraphViewModel<UInt>?>)  as GraphViewModel<D>?
                        }
                        is GraphType.String -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(WeightedDirectedGraph(),
                                graphId, graphVMState as MutableState<GraphViewModel<String>?>)  as GraphViewModel<D>?
                        }
                    }
                }
                is GraphStructure.Undirected -> {
                    when (storedData) {
                        is GraphType.Integer -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(WeightedUndirectedGraph(),
                                graphId, graphVMState as MutableState<GraphViewModel<Int>?>) as GraphViewModel<D>?
                        }
                        is GraphType.UInteger -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(WeightedUndirectedGraph(),
                                graphId, graphVMState as MutableState<GraphViewModel<UInt>?>) as GraphViewModel<D>?
                        }
                        is GraphType.String -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(WeightedUndirectedGraph(),
                                graphId, graphVMState as MutableState<GraphViewModel<String>?>) as GraphViewModel<D>?
                        }
                    }
                }
            }
        }
        is Weight.Unweighted -> {
            when (graphStructure) {
                is GraphStructure.Directed -> {
                    when (storedData) {
                        is GraphType.Integer -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(DirectedGraph(), graphId,
                                graphVMState as MutableState<GraphViewModel<Int>?>) as GraphViewModel<D>?
                        }
                        is GraphType.UInteger -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(DirectedGraph(), graphId,
                                graphVMState as MutableState<GraphViewModel<UInt>?>) as GraphViewModel<D>?
                        }
                        is GraphType.String -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(DirectedGraph(), graphId,
                                graphVMState as MutableState<GraphViewModel<String>?>) as GraphViewModel<D>?
                        }
                    }
                }
                is GraphStructure.Undirected -> {
                    when (storedData) {
                        is GraphType.Integer -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(UndirectedGraph(), graphId,
                                graphVMState as MutableState<GraphViewModel<Int>?>) as GraphViewModel<D>?
                        }
                        is GraphType.UInteger -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(UndirectedGraph(), graphId,
                                graphVMState as MutableState<GraphViewModel<UInt>?>) as GraphViewModel<D>?
                        }
                        is GraphType.String -> {
                            graphVMState.value = SQLDatabaseModule.updateImportedGraphVM(UndirectedGraph(), graphId,
                                graphVMState as MutableState<GraphViewModel<String>?>) as GraphViewModel<D>?
                        }
                    }
                }
            }
        }
    }
}
