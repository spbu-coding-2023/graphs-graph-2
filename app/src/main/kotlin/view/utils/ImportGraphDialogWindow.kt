package view.utils

import model.io.sql.SQLDatabaseModule
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import viewmodel.graph.GraphViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImportGraphDialogWindow() {
    val selectedGraphID = remember { mutableStateOf(0) }
    val closeDialog = remember { mutableStateOf(false) }
    val expanded = remember { mutableStateOf(false) }
    val importFromDBRequired = remember { mutableStateOf(false) }
    val selectedGraphName = remember { mutableStateOf("") }
    val graphs = remember { mutableStateOf(arrayListOf<Pair<Int, String>>()) }

    SQLDatabaseModule.getGraphNames(graphs)

    if (!closeDialog.value) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Column(
                modifier = Modifier.background(Color.White).padding(16.dp).width(350.dp).height(200.dp)
            ) {
                Row(modifier = Modifier.width(350.dp).height(150.dp)) {
                    Text(
                        "Select the database",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Box(modifier = Modifier.width(300.dp).padding(16.dp)) {
                        ExposedDropdownMenuBox(
                            expanded = expanded.value,
                            onExpandedChange = {
                                expanded.value = !expanded.value
                            }
                        ) {
                            TextField(
                                value = selectedGraphName.value,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
                                modifier = Modifier
                            )

                            ExposedDropdownMenu(
                                expanded = expanded.value,
                                onDismissRequest = { expanded.value = false }
                            ) {
                                graphs.value.forEach { graphName ->
                                    // TODO: fix its layout
                                    DropdownMenuItem(
                                        onClick = {
                                            selectedGraphName.value = graphName.second
                                            selectedGraphID.value = graphName.first
                                            expanded.value = false
                                        }
                                    ) {
                                        Text(text = graphName.second)
                                    }
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
                            importFromDBRequired.value = true
                            expanded.value = false
                            closeDialog.value = true
                        }
                    ) {
                        Text("Import", color = Color.White)
                    }
                }
            }
        }
    }
    if (importFromDBRequired.value) {
        importFromDBRequired.value = false
        return SQLDatabaseModule.importGraph<Any>(selectedGraphID.value)
    }
}
