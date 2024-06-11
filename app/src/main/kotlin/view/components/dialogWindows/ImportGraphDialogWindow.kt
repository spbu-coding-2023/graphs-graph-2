package view.components.dialogWindows

import JSON
import MyAppTheme
import NEO4J
import SQLITE
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ImportGraphDialogWindow() {
    var selectedDatabase by remember { mutableStateOf("") }
    var importGraphClicked by remember { mutableStateOf(false) }

    val fontSize = 16.sp
    val buttonColor = MaterialTheme.colors.secondary

    MyAppTheme {
        if (!importGraphClicked) {
            Dialog(
                onDismissRequest = {}
            ) {
                Column(
                    modifier =
                    Modifier.background(Color.White).padding(16.dp).width(300.dp).height(290.dp)
                ) {
                    Text(
                        "Import from...",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier.padding(10.dp).fillMaxWidth().height(50.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier =
                            Modifier.height(60.dp).width(250.dp),
                            colors = ButtonDefaults.buttonColors(buttonColor),
                            onClick = {
                                selectedDatabase = SQLITE
                                importGraphClicked = true
                            }
                        ) {
                            Text(SQLITE, color = Color.White, fontSize = fontSize)
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
                            colors = ButtonDefaults.buttonColors(buttonColor),
                            onClick = {
                                selectedDatabase = NEO4J
                                importGraphClicked = true
                            }
                        ) {
                            Text(NEO4J, color = Color.White, fontSize = fontSize)
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
                            colors = ButtonDefaults.buttonColors(buttonColor),
                            onClick = {
                                selectedDatabase = JSON
                                importGraphClicked = true
                            }
                        ) {
                            Text(JSON, color = Color.White, fontSize = fontSize)
                        }
                    }
                }
            }
        }
        if (importGraphClicked) {
            when (selectedDatabase) {
                SQLITE -> SQLiteImportGraphDialogWindow()
                NEO4J -> Neo4jImportGraphDialogWindow { importGraphClicked = false }
            }
        }
    }
}
