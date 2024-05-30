package view.utils

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
import model.io.neo4j.Neo4jRepositoryHandler
import model.io.sql.SQLDatabaseModule

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Neo4jImportGraphDialogWindow(onDismiss: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var importFromDBRequired by remember { mutableStateOf(false) }
    var selectedGraphName by remember { mutableStateOf("") }
    val graphNames = Neo4jRepositoryHandler.getNames()

    Dialog(
        onDismissRequest = {
            onDismiss()
        },
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
                        println(graphNames)
                        graphNames?.forEach { name ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedGraphName = name
                                    expanded = false
                                }
                            ) {
                                Text(text = name)
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
                        onDismiss()
                    }
                ) {
                    Text("Import", color = Color.White)
                }
            }
        }
    }

    if (importFromDBRequired) {
//        return SQLDatabaseModule.importGraph<Any>(selectedGraphID.value)
        Neo4jRepositoryHandler.loadGraph(selectedGraphName)
    }
}