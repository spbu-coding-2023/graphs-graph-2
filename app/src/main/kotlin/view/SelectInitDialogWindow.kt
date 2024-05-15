package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun GraphInitDialogWindow(
    showDialog: Boolean,
) {
    var showGraphDialog by remember { mutableStateOf(false) }
    var showCreateGraphDialog by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        if (showDialog) {
            showGraphDialog = true
        }

        onDispose { showGraphDialog = false }
    }

    if (showGraphDialog) {
        Dialog(onDismissRequest = {}, properties = DialogProperties(dismissOnBackPress = false)) {
            Column(
                modifier =
                    Modifier.background(Color.White).padding(16.dp).width(350.dp).height(150.dp)
            ) {
                Text(
                    "Welcome to WUDU!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text("Please select how to initialize the graph")

                Row(modifier = Modifier.height(20.dp).fillMaxWidth()) {}

                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    Button(
                        modifier = Modifier.width(145.dp).height(50.dp),
                        colors = ButtonDefaults.buttonColors(Color.Red),
                        onClick = {
                            showGraphDialog = false
                            showCreateGraphDialog = true
                        }
                    ) {
                        Text("Create", color = Color.White)
                    }

                    Button(
                        modifier = Modifier.width(145.dp).height(50.dp),
                        colors = ButtonDefaults.buttonColors(Color.Blue),
                        onClick = { showGraphDialog = false }
                    ) {
                        Text("Import", color = Color.White)
                    }
                }
            }
        }
    }

    if (showCreateGraphDialog) {
        CreateGraphDialogWindow()
    }
}
