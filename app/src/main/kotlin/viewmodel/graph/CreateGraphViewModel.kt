package viewmodel.graph

import androidx.compose.runtime.Composable
import model.DirectedGraph
import model.UndirectedGraph
import model.WeightedDirectedGraph
import model.WeightedUndirectedGraph
import view.MainScreen
import viewmodel.MainScreenViewModel

class CreateGraphViewModel {
    sealed class GraphType {
        object Integer : GraphType()
        object UInteger : GraphType()
        object String : GraphType()
    }

    sealed class GraphStructure {
        object Directed : GraphStructure()
        object Undirected : GraphStructure()
    }

    sealed class Weight {
        object Weighted : Weight()
        object Unweighted : Weight()
    }

    @Composable
    fun createGraph(
        storedData: GraphType,
        graphStructure: GraphStructure,
        weight: Weight
    ) { // TODO looks too shitty (((((((((( + mb any could be changed
        return when (weight) {
            is Weight.Weighted -> {
                when (graphStructure) {
                    is GraphStructure.Directed -> {
                        when (storedData) {
                            is GraphType.Integer -> MainScreen(MainScreenViewModel(WeightedDirectedGraph<Int>(),
                                "WeightedDirectedGraph Int"))
                            is GraphType.UInteger -> MainScreen(MainScreenViewModel(WeightedDirectedGraph<UInt>(),
                                "WeightedDirectedGraph UInt"))
                            is GraphType.String -> MainScreen(MainScreenViewModel(WeightedDirectedGraph<String>(),
                                "WeightedDirectedGraph String"))

                        }
                    }

                    is GraphStructure.Undirected -> {
                        when (storedData) {
                            is GraphType.Integer -> MainScreen(MainScreenViewModel(WeightedUndirectedGraph<Int>(),
                                "WeightedUndirectedGraph Int"))
                            is GraphType.UInteger -> MainScreen(MainScreenViewModel(WeightedUndirectedGraph<UInt>(),
                                "WeightedUndirectedGraph UInt"))
                            is GraphType.String -> MainScreen(MainScreenViewModel(WeightedUndirectedGraph<String>(),
                                "WeightedUndirectedGraph String")
                            )
                        }
                    }
                }
            }

            is Weight.Unweighted -> {
                when (graphStructure) {
                    is GraphStructure.Directed -> {
                        when (storedData) {
                            is GraphType.Integer -> MainScreen(MainScreenViewModel(DirectedGraph<Int>(),
                                "DirectedGraph Int"))
                            is GraphType.UInteger -> MainScreen(MainScreenViewModel(DirectedGraph<UInt>(),
                                "DirectedGraph UInt"))
                            is GraphType.String -> MainScreen(MainScreenViewModel(DirectedGraph<String>(),
                                "DirectedGraph String"))
                        }
                    }

                    is GraphStructure.Undirected -> {
                        when (storedData) {
                            is GraphType.Integer -> MainScreen(MainScreenViewModel(UndirectedGraph<Int>(),
                                "UndirectedGraph Int"))
                            is GraphType.UInteger -> MainScreen(MainScreenViewModel(UndirectedGraph<UInt>(),
                                "UndirectedGraph UInt"))
                            is GraphType.String -> MainScreen(MainScreenViewModel(UndirectedGraph<String>(),
                                "UndirectedGraph String"))
                        }
                    }
                }
            }
        }
    }
}
