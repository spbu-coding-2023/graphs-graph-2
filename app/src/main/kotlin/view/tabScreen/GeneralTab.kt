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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import view.utils.ErrorWindow
import viewmodel.WindowViewModel
import viewmodel.graph.GraphViewModel

@Composable
fun <D> GeneralTab(graphVM: GraphViewModel<D>) {
    var showVertexAddDialog by remember { mutableStateOf(false) }
    var vertexData by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var connectVertexId by remember { mutableStateOf("") }
    var firstVertexId by remember { mutableStateOf("") }
    var secondVertexId by remember { mutableStateOf("") }
    var secondVertexData by remember { mutableStateOf("") }
    var changesWereMade by remember { mutableStateOf(false) }
    val showErrorWindow = remember { mutableStateOf(false) }

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
                    onClick = { if (vertexData.isNotEmpty()) showVertexAddDialog = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text(
                        "add\nvertex",
                        textAlign = TextAlign.Center
                    )
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
                            "1 vertex ID",
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
                    textStyle = TextStyle(fontSize = 12.sp),
                    label = {
                        Text(
                            "2 vertex ID",
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
                    onClick = {
                        if (graphVM.graph.getVertices()
                                .any { it.id == firstVertexId.toInt() } && graphVM.graph.getVertices()
                                .any { it.id == secondVertexId.toInt() }
                        ) {
                            graphVM.addEdge(firstVertexId.toInt(), secondVertexId.toInt())

                            graphVM.updateIsRequired.value = true
                            secondVertexId = ""
                            firstVertexId = ""
                        } else {
                            showErrorWindow.value = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text(
                        "add\nedge",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp).clickable {
                graphVM.showVerticesID.value = !graphVM.showVerticesID.value
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = graphVM.showVerticesID.value,
                onCheckedChange = { graphVM.showVerticesID.value = it },
                colors =
                CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = MaterialTheme.colors.secondary
                )
            )
            Text(
                text = "Show ID",
                modifier = Modifier.padding(start = 10.dp, bottom = 3.dp).align(Alignment.CenterVertically)
            )

        }
    }

    if (showVertexAddDialog) {
        Dialog(onDismissRequest = {}) {
            Column(
                modifier =
                Modifier.background(Color.White).padding(16.dp).width(350.dp).height(200.dp)
            ) {
                if (graphVM.verticesVM.isEmpty()) {
                    Text("Input data of second vertex to create and connect with")

                    TextField(
                        value = secondVertexData,
                        onValueChange = { enteredValue ->
                            errorMessage = ""
                            secondVertexData = enteredValue
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
                            secondVertexData = secondVertexData.replace("\n", "")

                            if (secondVertexData.isBlank()) {
                                errorMessage = "Please enter data to store"
                            } else {
                                val firstId = graphVM.addVertex(vertexData)
                                val secondId = graphVM.addVertex(secondVertexData)
                                graphVM.addEdge(firstId, secondId)

                                graphVM.updateIsRequired.value = true

                                showVertexAddDialog = false
                                errorMessage = ""
                                secondVertexData = ""
                                vertexData = ""
                            }

                        }
                    ) {
                        Text("Connect")
                    }

                } else {
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
                                val firstId = graphVM.addVertex(vertexData)
                                graphVM.addEdge(firstId, connectVertexId.toInt())

                                graphVM.updateIsRequired.value = true

                                showVertexAddDialog = false
                                errorMessage = ""
                                connectVertexId = ""
                                vertexData = ""
                            }
                        }
                    ) {
                        Text("Connect")
                    }
                }
            }
        }
    }
    if (showErrorWindow.value) {
        ErrorWindow("No such Vertex", { showErrorWindow.value = false })
    }
}