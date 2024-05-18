package view

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
import viewmodel.graph.CreateGraphViewModel

@Composable
fun CreateGraphDialogWindow(viewModel: CreateGraphViewModel) {

    var closeDialog = remember { mutableStateOf(false) }
    val selectedStoredDataIndex = remember { mutableStateOf(0) }
    val selectedOrientationIndex = remember { mutableStateOf(0) }
    val selectedWeightinessIndex = remember { mutableStateOf(0) }
    val createGraphClicked = remember { mutableStateOf(false) }

    if (!closeDialog.value) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier =
                Modifier.background(Color.White).padding(16.dp).width(700.dp).height(390.dp)
            ) {
                Text(
                    "Create Graph",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    Column(modifier = Modifier.width(250.dp).fillMaxHeight()) {
                        Text("Select stored data:")

                        Row(modifier = Modifier.height(20.dp).fillMaxWidth()) {}

                        val radioOptions = listOf("Integer", "UInteger", "String", "Boolean")

                        Column(modifier = Modifier.width(220.dp)) {
                            radioOptions.forEachIndexed { index, option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier =
                                    Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                                        selectedStoredDataIndex.value = index
                                    }
                                ) {
                                    RadioButton(
                                        selected = selectedStoredDataIndex.value == index,
                                        onClick = { selectedStoredDataIndex.value = index },
                                        colors =
                                        RadioButtonDefaults.colors(
                                            selectedColor = MaterialTheme.colors.secondary
                                        )
                                    )
                                    Spacer(Modifier.width(1.dp))
                                    Text(
                                        text = option,
                                        style = TextStyle(fontSize = 16.sp),
                                        color =
                                        if (selectedStoredDataIndex.value == index) Color.Black
                                        else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.width(250.dp).fillMaxHeight()) {
                        Text("Select the orientation:")

                        Row(modifier = Modifier.height(20.dp).fillMaxWidth()) {}

                        val radioOptions = listOf("Undirected", "Directed")
                        Column(modifier = Modifier.width(220.dp)) {
                            radioOptions.forEachIndexed { index, option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier =
                                    Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                                        selectedOrientationIndex.value = index
                                    }
                                ) {
                                    RadioButton(
                                        selected = selectedOrientationIndex.value == index,
                                        onClick = { selectedOrientationIndex.value = index },
                                        colors =
                                        RadioButtonDefaults.colors(
                                            selectedColor = MaterialTheme.colors.secondary
                                        )
                                    )
                                    Spacer(Modifier.width(1.dp))
                                    Text(
                                        text = option,
                                        style = TextStyle(fontSize = 16.sp),
                                        color =
                                        if (selectedOrientationIndex.value == index) Color.Black
                                        else Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Column(modifier = Modifier.width(250.dp).height(200.dp)) {
                        Text("Select the weightnes*:")

                        Row(modifier = Modifier.height(20.dp).fillMaxWidth()) {}

                        Column(modifier = Modifier.width(220.dp)) {
                            val radioOptions = listOf("Unweighted", "Weighted")

                            radioOptions.forEachIndexed { index, option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier =
                                    Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                                        selectedWeightinessIndex.value = index
                                    }
                                ) {
                                    RadioButton(
                                        selected = selectedWeightinessIndex.value == index,
                                        onClick = { selectedWeightinessIndex.value = index },
                                        colors =
                                        RadioButtonDefaults.colors(
                                            selectedColor = MaterialTheme.colors.secondary
                                        )
                                    )
                                    Spacer(Modifier.width(1.dp))
                                    Text(
                                        text = option,
                                        style = TextStyle(fontSize = 16.sp),
                                        color =
                                        if (selectedWeightinessIndex.value == index) Color.Black
                                        else Color.Gray
                                    )
                                }
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth().height(50.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier.width(145.dp).height(50.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
                        onClick = {
                            closeDialog.value = true
                            createGraphClicked.value = true
                        }

                    ) {
                        Text("Apply", color = Color.White)
                    }
                }
            }
        }
    }
    if (createGraphClicked.value) {
        onCreateGraphClicked(
            viewModel,
            selectedStoredDataIndex.value,
            selectedOrientationIndex.value,
            selectedWeightinessIndex.value
        )
    }
}


@Composable
fun onCreateGraphClicked(
    viewModel: CreateGraphViewModel,
    storedDataIndex: Int,
    orientationIndex: Int,
    weightnessIndex: Int
) {
    val storedData = when (storedDataIndex) {
        0 -> CreateGraphViewModel.GraphType.Integer
        1 -> CreateGraphViewModel.GraphType.UInteger
        2 -> CreateGraphViewModel.GraphType.String
        else -> CreateGraphViewModel.GraphType.Integer // default to integer
    }

    val graphStructure = when (orientationIndex) {
        0 -> CreateGraphViewModel.GraphStructure.Directed
        1 -> CreateGraphViewModel.GraphStructure.Undirected
        else -> CreateGraphViewModel.GraphStructure.Directed // default to directed
    }

    val weight = when (weightnessIndex) {
        0 -> CreateGraphViewModel.Weight.Weighted
        1 -> CreateGraphViewModel.Weight.Unweighted
        else -> CreateGraphViewModel.Weight.Weighted // default to weighted
    }

    return viewModel.createGraph(storedData, graphStructure, weight)
}