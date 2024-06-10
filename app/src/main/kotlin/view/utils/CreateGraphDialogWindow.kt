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
    var selectedStoredDataIndex by remember { mutableStateOf(0) }
    var selectedOrientationIndex by remember { mutableStateOf(0) }
    var selectedWeightinessIndex by remember { mutableStateOf(0) }
    var createGraphClicked by remember { mutableStateOf(false) }

    MyAppTheme {
        if (!closeDialog) {
            Dialog(
                onDismissRequest = {},
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Column(
                    modifier =
                    Modifier.background(Color.White).padding(16.dp).width(700.dp).height(290.dp)
                ) {
                    Text(
                        "Create Graph",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Row(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                        Column(modifier = Modifier.width(250.dp).fillMaxHeight()) {
                            Text("Select stored data:")

                            Row(modifier = Modifier.height(20.dp).fillMaxWidth()) {}

                            val radioOptions = listOf("Integer", "UInteger", "String")

                            Column(modifier = Modifier.width(220.dp)) {
                                radioOptions.forEachIndexed { index, option ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier =
                                        Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                                            selectedStoredDataIndex = index
                                        }
                                    ) {
                                        RadioButton(
                                            selected = selectedStoredDataIndex == index,
                                            onClick = { selectedStoredDataIndex = index },
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
                                            if (selectedStoredDataIndex == index) Color.Black
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
                                            selectedOrientationIndex = index
                                        }
                                    ) {
                                        RadioButton(
                                            selected = selectedOrientationIndex == index,
                                            onClick = { selectedOrientationIndex = index },
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
                                            if (selectedOrientationIndex == index) Color.Black
                                            else Color.Gray
                                        )
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.width(250.dp).height(200.dp)) {
                            Text("Select the weightiness:")

                            Row(modifier = Modifier.height(20.dp).fillMaxWidth()) {}

                            Column(modifier = Modifier.width(220.dp)) {
                                val radioOptions = listOf("Unweighted", "Weighted")

                                radioOptions.forEachIndexed { index, option ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier =
                                        Modifier.padding(vertical = 4.dp).fillMaxWidth().clickable {
                                            selectedWeightinessIndex = index
                                        }
                                    ) {
                                        RadioButton(
                                            selected = selectedWeightinessIndex == index,
                                            onClick = { selectedWeightinessIndex = index },
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
                                            if (selectedWeightinessIndex == index) Color.Black
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
                selectedStoredDataIndex,
                selectedOrientationIndex,
                selectedWeightinessIndex
            )
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

fun getGraphVMParameter(storedDataType: Int, structureType: Int, weightType: Int): Triple<SetupGraphViewModel.GraphType, SetupGraphViewModel.GraphStructure, SetupGraphViewModel.Weight> {
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
