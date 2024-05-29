package view.tabScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewmodel.graph.GraphViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import model.io.neo4j.Neo4jRepository
import model.io.sql.SQLDatabaseModule
import view.utils.EditDBWindow
import view.utils.ErrorWindow
import view.utils.ImportGraphDialogWindow
import view.utils.Neo4jLoginDialog
import viewmodel.MainScreenViewModel
import java.awt.FileDialog
import java.awt.Frame

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <D> FileControlTab(graphVM: GraphViewModel<D>, mainScreenVM: MainScreenViewModel<D>) {
    var showSaveDialog by remember { mutableStateOf(false) }
    var showLoadDialog by remember { mutableStateOf(false) }
    var graphName by remember { mutableStateOf("") }
    var showErrorWindow by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showEditDialog by remember { mutableStateOf(false) }

    var showNeo4jDialog by remember { mutableStateOf(false) }
    val isAuthorizedToNeo4j = remember { mutableStateOf(false) }
    val neo4jUri = remember { mutableStateOf("") }
    val neo4jUser = remember { mutableStateOf("") }
    val neo4jPassword = remember { mutableStateOf("") }

    val databases = arrayOf("SQLite", "Neo4j", "JSON")
    var selectedDatabase by remember { mutableStateOf(databases[0]) }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Row(modifier = Modifier.height(0.dp)) {}

        val rowHeight = 75.dp
        val fieldHeight = 70.dp

        val borderPadding = 10.dp
        val horizontalGap = 20.dp

        val tabWidth = 360.dp
        val fieldWidth = (tabWidth / 2) - horizontalGap

        Row(
            modifier = Modifier.height(rowHeight).padding(borderPadding),
            horizontalArrangement = Arrangement.spacedBy(borderPadding)
        ) {
            Column(modifier = Modifier.width(tabWidth).fillMaxHeight(), Arrangement.Center) {
                TextField(
                    value = graphName,
                    onValueChange = { newValue ->
                        graphName = newValue
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(fieldHeight)
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
            }
        }

        Row(
            modifier = Modifier.height(rowHeight).padding(borderPadding),
            horizontalArrangement = Arrangement.spacedBy(horizontalGap)
        ) {

            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier.width(fieldWidth).fillMaxHeight()
            ) {
                TextField(
                    value = selectedDatabase,
                    onValueChange = { graphName = it },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier,
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
                    databases.forEach { db ->
                        DropdownMenuItem(
                            modifier = Modifier,
                            onClick = {
                                selectedDatabase = db
                                expanded = false
                            }
                        ) {
                            Text(text = db)
                        }
                    }
                }
            }

            if (selectedDatabase == "JSON") {
                val fileDialog = FileDialog(null as Frame?, "Select File to Open")
                fileDialog.mode = FileDialog.LOAD
                Button(
                    modifier = Modifier.fillMaxSize().height(fieldHeight),
                    onClick = {
                        fileDialog.isVisible = true
                    },
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text("Select File")
                }
            }
        }

        Row(
            modifier = Modifier.height(rowHeight).padding(borderPadding),
            horizontalArrangement = Arrangement.spacedBy(horizontalGap)
        ) {
            Column(modifier = Modifier.width(fieldWidth).fillMaxHeight(), Arrangement.Center) {
                Button(
                    modifier = Modifier.fillMaxSize().height(fieldHeight),
                    onClick = {
                        showSaveDialog = true
                        graphName = ""
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text("Save")
                }
            }
            Column(modifier = Modifier.width(fieldWidth).fillMaxHeight(), Arrangement.Center) {
                Button(
                    modifier = Modifier.fillMaxSize().height(fieldHeight),
                    onClick = {
                        showLoadDialog = true
                        graphName = ""
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text("Load")
                }
            }
        }
        Row(
            modifier = Modifier.height(rowHeight).padding(borderPadding),
            horizontalArrangement = Arrangement.spacedBy(horizontalGap)
        ) {
            Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), Arrangement.Center) {
                Button(
                    modifier = Modifier.fillMaxSize().height(fieldHeight),
                    onClick = {
                        showEditDialog = true
                        graphName = ""
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text("Edit DB")
                }
            }
        }
    }

    if (showSaveDialog) {
        if (selectedDatabase == "SQLite") {
            val existingGraphNamesSQL = remember { mutableStateOf(arrayListOf<Pair<Int, String>>()) }
            SQLDatabaseModule.getGraphNames(existingGraphNamesSQL)

            if (existingGraphNamesSQL.value.any { it.second == graphName }) {
                showErrorWindow = true
                errorMessage = "Graph with name: ${graphName} already exists"
                graphName = ""
                showSaveDialog = false
            } else {
                SQLDatabaseModule.insertGraph(graphVM, graphName, graphVM.graphType.value)
                Dialog(
                    onDismissRequest = {
                        showSaveDialog = false
                        graphName = ""
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(16.dp)
                            .width(300.dp)
                            .height(50.dp)
                    ) {
                        Text("Graph '$graphName' saved successfully!")
                    }
                }

                // Automatically dismiss the dialog after 3 seconds
                LaunchedEffect(Unit) {
                    delay(3000)
                    showSaveDialog = false
                }
            }
        } else if (selectedDatabase == "Neo4j") {
            val repo = mainScreenVM.neo4jRepo
            if (repo == null) {
                showSaveDialog = false
                showNeo4jDialog = true
            } else {
                repo.saveOrReplaceGraph(graphVM.graph, graphName, graphVM.isDirected.value, graphVM.isWeighted.value)
            }
        }
    }

    if (showLoadDialog) {
        ImportGraphDialogWindow() // TODO
    }

    if (showEditDialog) {
        EditDBWindow(selectedDatabase) { showEditDialog = false }
    }

    if (showNeo4jDialog)  {
        Neo4jLoginDialog(mainScreenVM) { showNeo4jDialog = false }
    }

    if (showErrorWindow) {
        ErrorWindow(
            errorMessage
        ) {
            showErrorWindow = false
            errorMessage = ""
        }
    }
}
