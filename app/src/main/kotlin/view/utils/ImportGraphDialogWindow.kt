package view.utils

import MyAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
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

@Composable
fun ImportGraphDialogWindow() {
    var selectedDatabase by remember { mutableStateOf("") }
    var importGraphClicked by remember { mutableStateOf(false) }

    MyAppTheme {
        Dialog(
            onDismissRequest = {}
        ) {
            Column(
                modifier =
                Modifier.background(Color.White).padding(16.dp).width(300.dp).height(290.dp)
            ) {
                Text(
                    "Where do you want to import from?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth().height(50.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier =
                        Modifier.height(60.dp).width(250.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
                        onClick = {
                            selectedDatabase = "SQLite"
                            importGraphClicked = true
                        }
                    ) {
                        Text("SQLite", color = Color.White)
                    }
                }

                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth().height(50.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier =
                        Modifier.height(60.dp).width(250.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
                        onClick = {
                            selectedDatabase = "Neo4j"
                            importGraphClicked = true
                        }
                    ) {
                        Text("Neo4j", color = Color.White)
                    }
                }
                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth().height(50.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier =
                        Modifier.height(60.dp).width(250.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.secondary),
                        onClick = {
                            selectedDatabase = "JSON"
                            importGraphClicked = true
                        }
                    ) {
                        Text("JSON", color = Color.White)
                    }
                }
            }
        }
    }
}