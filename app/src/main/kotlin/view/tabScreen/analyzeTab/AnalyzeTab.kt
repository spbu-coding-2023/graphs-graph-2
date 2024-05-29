package view.tabScreen.analyzeTab

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import model.graphs.DirectedGraph
import model.graphs.UndirectedGraph
import model.graphs.WeightedDirectedGraph
import model.graphs.WeightedUndirectedGraph
import view.tabScreen.analyzeTab.algorithmsUI.*
import viewmodel.graph.GraphViewModel

val rowHeight = 75.dp
val borderPadding = 10.dp
val horizontalGap = 20.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <D> AnalyzeTab(graphVM: GraphViewModel<D>) {
    val algorithms = mutableListOf(
        "Layout",
        "Communities",
        "Key vertices"
    )

    if (graphVM.graph is DirectedGraph) {
        algorithms += "SCCs"
        algorithms += "Cycles"
    }

    if (graphVM.graph is UndirectedGraph) {
        algorithms += "Bridges"
    }

    if (graphVM.graph is WeightedUndirectedGraph) {
        algorithms += "Min spanning tree"
        algorithms += "Shortest path"
    }

    if (graphVM.graph is WeightedDirectedGraph) {
        algorithms += "Shortest path"
    }

    var selectedAlgorithm by remember { mutableStateOf(algorithms[0]) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Row(modifier = Modifier.height(0.dp)) {}
        Row(
            modifier = Modifier.height(rowHeight).padding(borderPadding),
            horizontalArrangement = Arrangement.spacedBy(horizontalGap)
        ) {
            Column(
                modifier = Modifier.width(95.dp).fillMaxHeight(),
                Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Algorithm:",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 7.dp)
                )
            }
            Column(modifier = Modifier.width(225.dp).fillMaxHeight(), Arrangement.Center) {
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    modifier = Modifier.width(225.dp).fillMaxHeight()
                ) {
                    TextField(
                        value = selectedAlgorithm,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.secondaryVariant
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        algorithms.forEach { algorithm ->
                            DropdownMenuItem(
                                modifier = Modifier,
                                onClick = {
                                    selectedAlgorithm = algorithm
                                    expanded = false
                                }
                            ) {
                                Text(text = algorithm)
                            }
                        }
                    }
                }
            }
        }

        when (selectedAlgorithm) {
            "Layout" -> { LayoutUI(graphVM) }
            "Communities" -> { CommunitiesUI(graphVM) }
            "Key vertices" -> { KeyVerticesUI(graphVM) }
            "Shortest path" -> { ShortestPathUI(graphVM) }
            "Cycles" -> { CyclesUI(graphVM) }
            "Bridges" -> { BridgesUI(graphVM) }
            "SCCs" -> { SCCUI(graphVM) }
            "Min spanning tree" -> { MinSpanningTreeUI(graphVM) }
        }
    }
}
