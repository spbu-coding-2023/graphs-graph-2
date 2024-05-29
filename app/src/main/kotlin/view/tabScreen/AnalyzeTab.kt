package view.tabScreen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import viewmodel.graph.GraphViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <D> AnalyzeTab(graphVM: GraphViewModel<D>) {


    val algorithms = arrayOf(
        "Layout",
        "Clustering",
        "Key vertices",
        "Shortest path",
        "Cycles",
        "Bridges",
        "SCC",
        "Min spanning tree"
    )
    var selectedAlgorithm by remember { mutableStateOf(algorithms[0]) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Row(modifier = Modifier.height(0.dp)) {}

        val rowHeight = 75.dp
        val fieldHeight = 70.dp

        val borderPadding = 10.dp
        val horizontalGap = 20.dp

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

    }
}
