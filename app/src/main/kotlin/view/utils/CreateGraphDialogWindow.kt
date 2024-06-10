package view.utils

import MyAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import viewmodel.graph.SetupGraphViewModel

@Composable
fun CreateGraphDialogWindow(viewModel: SetupGraphViewModel) {
    var closeDialog by remember { mutableStateOf(false) }
    val selectedStoredDataIndex = remember { mutableStateOf(0) }
    val selectedOrientationIndex = remember { mutableStateOf(0) }
    val selectedWeightinessIndex = remember { mutableStateOf(0) }
    var createGraphClicked by remember { mutableStateOf(false) }

    MyAppTheme {
        if (!closeDialog) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(16.dp)
                        .widthIn(min = 200.dp, max = 700.dp)
                        .wrapContentHeight()
                ) {
                    Text(
                        "Create Graph",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Row(modifier = Modifier.wrapContentHeight().fillMaxWidth()) {
                        RadioColumn(
                            "Select stored data:",
                            selectedStoredDataIndex,
                            listOf("Integer", "UInteger", "String"),
                            Modifier.weight(1f).padding(horizontal = 8.dp)
                        )
                        RadioColumn(
                            "Select the orientation:",
                            selectedOrientationIndex,
                            listOf("Undirected", "Directed"),
                            Modifier.weight(1f).padding(horizontal = 8.dp)
                        )
                        RadioColumn(
                            "Select the weightiness:",
                            selectedWeightinessIndex,
                            listOf("Unweighted", "Weighted"),
                            Modifier.weight(1f).padding(horizontal = 8.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            modifier = Modifier.wrapContentSize(),
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
                            onClick = {
                                closeDialog = true
                                createGraphClicked = true
                            }
                        ) {
                            Text("Apply", color = Color.White)
                        }
                    }
                }
            }
        }
        if (createGraphClicked) {
            createGraphFromTypesIndices(
                viewModel,
                selectedStoredDataIndex.value,
                selectedOrientationIndex.value,
                selectedWeightinessIndex.value
            )
        }
    }
}

@Composable
fun RadioColumn(
    selectText: String,
    currentDataIndex: MutableState<Int>,
    radioOptions: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(selectText)
        Spacer(modifier = Modifier.height(8.dp))
        Column {
            radioOptions.forEachIndexed { index, option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                        .clickable { currentDataIndex.value = index }
                ) {
                    RadioButton(
                        selected = currentDataIndex.value == index,
                        onClick = { currentDataIndex.value = index },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colors.secondary
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = option,
                        style = TextStyle(fontSize = 16.sp),
                        color = if (currentDataIndex.value == index) Color.Black else Color.Gray
                    )
                }
            }
        }
    }
}


@Composable
fun createGraphFromTypesIndices(
    viewModel: SetupGraphViewModel,
    storedDataIndex: Int,
    orientationIndex: Int,
    weightnessIndex: Int
) {
    val storedData = when (storedDataIndex) {
        0 -> SetupGraphViewModel.GraphType.Integer
        1 -> SetupGraphViewModel.GraphType.UInteger
        2 -> SetupGraphViewModel.GraphType.String
        else -> SetupGraphViewModel.GraphType.Integer // default to integer
    }

    val graphStructure = when (orientationIndex) {
        0 -> SetupGraphViewModel.GraphStructure.Undirected
        1 -> SetupGraphViewModel.GraphStructure.Directed
        else -> SetupGraphViewModel.GraphStructure.Undirected // default to undirected
    }

    val weight = when (weightnessIndex) {
        0 -> SetupGraphViewModel.Weight.Unweighted
        1 -> SetupGraphViewModel.Weight.Weighted
        else -> SetupGraphViewModel.Weight.Unweighted // default to unweighted
    }

    return viewModel.createGraphAndApplyScreen(storedData, graphStructure, weight)
}

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
