package view.utils

import model.io.sql.SQLDatabaseModule
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
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SQLiteImportGraphDialogWindow() {
    var selectedGraphID by remember { mutableStateOf(0) }
    var closeDialog by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var importFromDBRequired by remember { mutableStateOf(false) }
    var selectedGraphName by remember { mutableStateOf("") }
    val graphs = remember { mutableStateOf(arrayListOf<Pair<Int, String>>()) }

    SQLDatabaseModule.getGraphNames(graphs)

    if (!closeDialog) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier.background(Color.White)
                    .padding(top = 16.dp, end = 16.dp, start = 16.dp, bottom = 6.dp).width(350.dp).height(180.dp)
            ) {
                Text(
                    "Select the graph:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                Row(
                    modifier = Modifier.padding(10.dp).fillMaxWidth().height(50.dp),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        },
                        modifier = Modifier.fillMaxWidth().fillMaxHeight()
                    ) {
                        TextField(
                            value = selectedGraphName,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            graphs.value.forEach { graphName ->
                                // TODO: fix its layout
                                DropdownMenuItem(
                                    onClick = {
                                        selectedGraphName = graphName.second
                                        selectedGraphID = graphName.first
                                        expanded = false
                                    }
                                ) {
                                    Text(text = graphName.second)
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
                            importFromDBRequired = true
                            expanded = false
                            closeDialog = true
                        }
                    ) {
                        Text("Import", color = Color.White)
                    }
                }
            }
        }
    }
    if (importFromDBRequired) {
        return SQLDatabaseModule.importGraph<Any>(selectedGraphID)
    }
}
