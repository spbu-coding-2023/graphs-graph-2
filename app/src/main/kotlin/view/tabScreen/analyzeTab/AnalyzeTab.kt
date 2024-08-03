package view.tabScreen.analyzeTab

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import view.tabScreen.analyzeTab.algorithmsUI.*
import viewmodel.graph.Algorithm
import viewmodel.graph.GraphViewModel
import viewmodel.graph.getAlgorithmDisplayName

val rowHeight = 75.dp
val borderPadding = 10.dp
val horizontalGap = 20.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <D> AnalyzeTab(graphVM: GraphViewModel<D>) {
    val algorithms = graphVM.getAvailableAlgorithms()
    var selectedAlgorithm by remember { mutableStateOf(algorithms.first()) }

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
                        value = getAlgorithmDisplayName(selectedAlgorithm),
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
                                Text(text = getAlgorithmDisplayName(algorithm))
                            }
                        }
                    }
                }
            }
        }

        when (selectedAlgorithm) {
            Algorithm.LAYOUT -> { LayoutUI(graphVM) }
            Algorithm.FIND_COMMUNITIES -> { CommunitiesUI(graphVM) }
            Algorithm.FIND_KEY_VERTICES -> { KeyVerticesUI(graphVM) }
            Algorithm.FIND_SHORTEST_PATH -> { ShortestPathUI(graphVM) }
            Algorithm.FIND_CYCLES -> { CyclesUI(graphVM) }
            Algorithm.FIND_BRIDGES -> { BridgesUI(graphVM) }
            Algorithm.FIND_SCCS -> { SCCUI(graphVM) }
            Algorithm.MIN_SPANNING_TREE -> { MinSpanningTreeUI(graphVM) }
        }
    }
}

