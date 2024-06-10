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
import viewmodel.graph.createGraphFromTypesIndices

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
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
