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
import view.components.dialogWindows.ErrorWindow
import viewmodel.graph.GraphViewModel

@Composable
fun <D> ShortestPathUI(graphVM: GraphViewModel<D>) {
    var sourceVertexId by remember { mutableStateOf("") }
    var destVertexId by remember { mutableStateOf("") }
    var showErrorWindow by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.height(rowHeight).padding(borderPadding),
        horizontalArrangement = Arrangement.spacedBy(horizontalGap)
    ) {
        Column(modifier = Modifier.width(160.dp).fillMaxHeight(), Arrangement.Center) {
            TextField(
                value = sourceVertexId,
                onValueChange = { sourceVertexId = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                textStyle = TextStyle(fontSize = 14.sp),
                label = {
                    Text(
                        "Source ID",
                        style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                        color = Color.Gray,
                    )
                },
                colors =
                TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.secondaryVariant
                )
            )
        }
        Column(modifier = Modifier.width(160.dp).fillMaxHeight(), Arrangement.Center) {
            TextField(
                value = destVertexId,
                onValueChange = { destVertexId = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                textStyle = TextStyle(fontSize = 14.sp),
                label = {
                    Text(
                        "Destination ID",
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
                    if (sourceVertexId.isEmpty() || destVertexId.isEmpty()) {
                        errorMessage = "Enter vertices' IDs"
                        showErrorWindow = true
                    }
                    else if (
                        !sourceVertexId.all { char -> char.isDigit() } ||
                        !destVertexId.all { char -> char.isDigit() }
                        ) {
                        errorMessage = "ID should be a number"
                        showErrorWindow = true
                    }
                    else if (sourceVertexId == destVertexId) {
                        errorMessage = "Vertices' IDs should be different"
                        showErrorWindow = true
                    }
                    else if (
                        sourceVertexId.toInt() > graphVM.graph.getVertices().size - 1 ||
                        destVertexId.toInt() > graphVM.graph.getVertices().size - 1
                        ) {
                        errorMessage = "No vertex with such ID"
                        showErrorWindow = true
                    }
                    else if (!graphVM.findShortestPath(sourceVertexId.toInt(), destVertexId.toInt())) {
                        errorMessage = "Shortest path doesn't exist"
                        showErrorWindow = true
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
            ) {
                Text("Run algorithm")
            }
        }
    }

    if (showErrorWindow) {
        ErrorWindow(errorMessage) { showErrorWindow = false }
    }
}
