package view.tabScreen

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
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
import androidx.compose.ui.window.Dialog

@Composable
fun GeneralTab() {
    var showDialog by remember { mutableStateOf(false) }
    var vertexData by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var connectVertexId by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Row(modifier = Modifier.height(0.dp)) {}

        Row(
            modifier = Modifier.height(65.dp).padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Column(modifier = Modifier.width(200.dp).fillMaxHeight(), Arrangement.Center) {
                TextField(
                    value = vertexData,
                    onValueChange = { vertexData = it },
                    modifier = Modifier.fillMaxWidth().height(60.dp).clip(RoundedCornerShape(8.dp)),
                    textStyle = TextStyle(fontSize = 14.sp),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent)
                )
            }
            Column(modifier = Modifier.width(110.dp).fillMaxHeight(), Arrangement.Center) {
                Button(
                    modifier = Modifier.fillMaxSize().height(60.dp),
                    onClick = { showDialog = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text("add")
                }
            }
        }

        Row(
            modifier = Modifier.height(50.dp).padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Column(modifier = Modifier.width(200.dp).fillMaxHeight(), Arrangement.Center) {
                Button(modifier = Modifier.width(150.dp).height(45.dp), onClick = {}) {
                    Text("hahhaha")
                }
            }
            Column(modifier = Modifier.width(110.dp).fillMaxHeight(), Arrangement.Center) {
                Button(modifier = Modifier.fillMaxSize().height(45.dp), onClick = {}) {
                    Text("hahhaha")
                }
            }
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = {}) {
            Column(
                modifier =
                    Modifier.background(Color.White).padding(16.dp).width(300.dp).height(250.dp)
            ) {
                Text("Input id of vertex to connect with:")
                TextField(
                    value = connectVertexId,
                    onValueChange = { connectVertexId = it },
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
                        if (connectVertexId.isBlank()) {
                            errorMessage = "Please enter Id of a vertex to connect"
                        } else {
                            // Process the input here
                            showDialog = false
                            errorMessage = ""
                        }
                    }
                ) {
                    Text("Connect")
                }
            }
        }
    }
}

@Preview
@Composable
fun GeneralTabPreview() {
    GeneralTab()
}
