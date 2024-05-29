package view.tabScreen.analyzeTab.algorithmsUI

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.tabScreen.analyzeTab.borderPadding
import view.tabScreen.analyzeTab.horizontalGap
import view.tabScreen.analyzeTab.rowHeight
import view.utils.ErrorWindow
import viewmodel.graph.GraphViewModel

@Composable
fun <D> CyclesUI(graphVM: GraphViewModel<D>) {
    var vertexId by remember { mutableStateOf("") }
    val showErrorWindow = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }

    Row(
        modifier = Modifier.height(rowHeight).padding(borderPadding),
        horizontalArrangement = Arrangement.spacedBy(horizontalGap)
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), Arrangement.Center) {
            TextField(
                value = vertexId,
                onValueChange = { vertexId = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                textStyle = TextStyle(fontSize = 14.sp),
                label = {
                    Text(
                        "Vertex ID",
                        style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                        color = Color.Gray
                    )
                },
                colors =
                TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.secondaryVariant
                )
            )
        }
    }
    Row(
        modifier = Modifier.height(rowHeight).padding(borderPadding),
        horizontalArrangement = Arrangement.spacedBy(horizontalGap)
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), Arrangement.Center) {
            Button(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    if (vertexId.isEmpty()) {
                        errorMessage.value = "Enter vertex's ID"
                        showErrorWindow.value = true
                    }
                    else if (!vertexId.all { char -> char.isDigit() }) {
                        errorMessage.value = "ID should be a number"
                        showErrorWindow.value = true
                    }
                    else if (vertexId.toInt() > graphVM.graph.getVertices().size - 1) {
                        errorMessage.value = "No vertex with ID $vertexId"
                        showErrorWindow.value = true
                    }
                    else if (!graphVM.findCycles(vertexId.toInt())) {
                        errorMessage.value = "No cycles were found"
                        showErrorWindow.value = true
                    }
                    else {
                        graphVM.highlighNextCycle()
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
            ) {
                Text("Run algorithm")
            }
        }
    }
    Row(
        modifier = Modifier.height(rowHeight).padding(borderPadding),
        horizontalArrangement = Arrangement.spacedBy(horizontalGap)
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), Arrangement.Center) {
            Button(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    if (!graphVM.highlighNextCycle()) {
                        errorMessage.value = "Please run algorithm first"
                        showErrorWindow.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
            ) {
                Text("Highlight next cycle")
            }
        }
    }

    if (showErrorWindow.value) {
        ErrorWindow(errorMessage.value, { showErrorWindow.value = false })
    }
}
