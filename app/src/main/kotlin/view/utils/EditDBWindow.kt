package view.utils

import SQLITE
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import model.io.sql.SQLDatabaseModule

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditDBWindow(DBType: String, onDismiss: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    val graphNamesSQL = remember { mutableStateOf(arrayListOf<Pair<Int, String>>()) }
    var expanded by remember { mutableStateOf(false) }
    var selectedGraphName by remember { mutableStateOf("") }
    var selectedGraphID by remember { mutableStateOf(0)}
    var updateGraphNames by remember { mutableStateOf(false) }
    var graphNameToReplaceWith by remember { mutableStateOf("")}

    if (DBType == SQLITE) {
        SQLDatabaseModule.getGraphNames(graphNamesSQL)
        if (graphNamesSQL.value.isNotEmpty()) showDialog = true
        else ErrorWindow("Database doesn't have any Graphs", {})
    }


    if (showDialog) {
        Dialog(onDismissRequest = onDismiss, properties = DialogProperties(dismissOnBackPress = false, usePlatformDefaultWidth = false)) {
            Column(
                modifier = Modifier.background(Color.White).padding(top = 16.dp, end = 16.dp, start = 16.dp, bottom = 6.dp).width(450.dp).height(180.dp)
            ) {
                Text(
                    "Edit database",
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
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.secondaryVariant
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            graphNamesSQL.value.forEach { db ->
                                DropdownMenuItem(
                                    modifier = Modifier,
                                    onClick = {
                                        selectedGraphName = db.second
                                        selectedGraphID = db.first
                                        expanded = false
                                    }
                                ) {
                                    Text(text = db.second)
                                }
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 10.dp)
                        .fillMaxWidth()
                        .height(150.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextField(
                        value = graphNameToReplaceWith,
                        onValueChange = { newValue ->
                            graphNameToReplaceWith = newValue
                        },
                        modifier = Modifier
                            .width(200.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        textStyle = TextStyle(fontSize = 14.sp),
                        label = {
                            Text(
                                "Graph name",
                                style = MaterialTheme.typography.body1.copy(fontSize = 14.sp),
                                color = Color.Gray
                            )
                        },
                        colors =
                        TextFieldDefaults.textFieldColors(
                            backgroundColor = MaterialTheme.colors.secondaryVariant
                        )
                    )

                    Spacer(modifier = Modifier.width(15.dp))

                    Button(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
                        onClick = {
                            SQLDatabaseModule.deleteGraph(selectedGraphID)
                            updateGraphNames = true
                            selectedGraphName = ""
                        }
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                    }

                    Spacer(modifier = Modifier.width(15.dp))

                    Button(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
                        onClick = {
                            SQLDatabaseModule.renameGraph(selectedGraphID, graphNameToReplaceWith)
                            updateGraphNames = true
                            selectedGraphName = ""
                            graphNameToReplaceWith = ""
                        }
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Rename", tint = Color.White)
                    }
                }
            }
        }
    }
    if (updateGraphNames) {
        SQLDatabaseModule.getGraphNames(graphNamesSQL)
        updateGraphNames = false
    }
}
