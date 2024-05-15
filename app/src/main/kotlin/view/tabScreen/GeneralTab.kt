package view.tabScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import viewmodel.graph.GraphViewModel

@Composable
fun <D> GeneralTab(graphVM: GraphViewModel<D>) {
    var showDialog by remember { mutableStateOf(false) }
    var vertexData by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var connectVertexId by remember { mutableStateOf("") }
    var vertexExistenceStatus by remember { mutableStateOf(false) }
    var firstVertexId by remember { mutableStateOf("") }
    var secondVertexId by remember { mutableStateOf("") }
    val displayId = remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Row(modifier = Modifier.height(0.dp)) {}

        Row(
            modifier = Modifier.height(75.dp).padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(modifier = Modifier.width(200.dp).fillMaxHeight(), Arrangement.Center) {
                TextField(
                    value = vertexData,
                    onValueChange = { vertexData = it },
                    modifier = Modifier.fillMaxWidth().height(70.dp).clip(RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(fontSize = 14.sp),
                    label = {
                        Text(
                            "Vertex data",
                            style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                            color = Color.Gray
                        )
                    },
                    colors =
                        TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.secondaryVariant
                        ),
                )
            }
            Column(modifier = Modifier.width(120.dp).fillMaxHeight(), Arrangement.Center) {
                Button(
                    modifier = Modifier.fillMaxSize().height(70.dp),
                    onClick = { if (vertexData.isNotEmpty()) showDialog = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text("add")
                }
            }
        }

        Row(
            modifier = Modifier.height(75.dp).padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(modifier = Modifier.width(100.dp).fillMaxHeight(), Arrangement.Center) {
                TextField(
                    value = firstVertexId,
                    onValueChange = { firstVertexId = it },
                    modifier = Modifier.fillMaxWidth().height(70.dp).clip(RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(fontSize = 12.sp),
                    label = {
                        Text(
                            "1 edge ID",
                            style = MaterialTheme.typography.body1.copy(fontSize = 12.sp),
                            color = Color.Gray
                        )
                    },
                    colors =
                        TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.secondaryVariant
                        )
                )
            }

            Column(modifier = Modifier.width(100.dp).fillMaxHeight(), Arrangement.Center) {
                TextField(
                    value = secondVertexId,
                    onValueChange = { secondVertexId = it },
                    modifier = Modifier.fillMaxWidth().height(70.dp).clip(RoundedCornerShape(8.dp)),
                    label = {
                        Text(
                            "2 edge ID",
                            style = MaterialTheme.typography.body1.copy(fontSize = 12.sp),
                            color = Color.Gray
                        )
                    },
                    colors =
                        TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.secondaryVariant
                        )
                )
            }

            Column(modifier = Modifier.width(110.dp).fillMaxHeight(), Arrangement.Center) {
                Button(
                    modifier = Modifier.fillMaxSize().height(70.dp),
                    onClick = {},
                    // TODO add edge
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text("add")
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp).clickable {
                displayId.value = !displayId.value
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = displayId.value,
                onCheckedChange = { displayId.value = it },
                // TODO display ids
                colors =
                    CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary, uncheckedColor = MaterialTheme.colors.secondary)
            )
            Text(
                text = "Checkbox Text",
                modifier = Modifier.padding(start = 10.dp, bottom = 3.dp).align(Alignment.CenterVertically)
            )

        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = {}) {
            vertexData = ""

            Column(
                modifier =
                    Modifier.background(Color.White).padding(16.dp).width(350.dp).height(200.dp)
            ) {
                Text("Input ID of vertex to connect with:")

                TextField(
                    value = connectVertexId,
                    onValueChange = { newValue ->
                        errorMessage = ""
                        connectVertexId = newValue
                    },
                    modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(fontSize = 14.sp),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                )

                if (errorMessage.isNotBlank()) {
                    Text(errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }

                Button(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    onClick = {
                        connectVertexId = connectVertexId.replace("\n", "")

                        if (!connectVertexId.all { char -> char.isDigit() }) {
                            errorMessage = "ID should be a numeric"
                        } else if (!graphVM.checkVertexById(connectVertexId.toInt())) {
                            errorMessage = "There isn't a Vertex with such ID"
                        } else if (connectVertexId.isBlank()) {
                            errorMessage = "Please enter an ID"
                        } else if (
                            connectVertexId.isNotBlank() && connectVertexId.toIntOrNull() == null
                        ) {
                            errorMessage = "ID must be an integer"
                        } else {
                            // val firstId = graphVM.addVertex<D>(vertexData)
                            // graphVM.addEdge(firstId, connectVertexId.toInt())
                            // TODO
                            showDialog = false
                            errorMessage = ""
                            connectVertexId = ""
                        }
                    }
                ) {
                    Text("Connect")
                }
            }
        }
    }
}
