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
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog

@Composable
fun <D> FileControlTab(graphVM: GraphViewModel<D>) {
    var showSaveDialog by remember { mutableStateOf(false) }
    var showLoadDialog by remember { mutableStateOf(false) }
    var graphName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        val padding = 10.dp
        Row(modifier = Modifier.height(0.dp)) {}

        Row(
            modifier = Modifier.height(75.dp).padding(padding),
            horizontalArrangement = Arrangement.spacedBy(padding)
        ) {
            Column(modifier = Modifier.width(360.dp).fillMaxHeight(), Arrangement.Center) {
                TextField(
                    value = graphName,
                    onValueChange = { graphName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
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
            modifier = Modifier.height(75.dp).padding(padding),
            horizontalArrangement = Arrangement.spacedBy(padding)
        ) {
            Column(modifier = Modifier.width(170.dp).fillMaxHeight(), Arrangement.Center) {
                Button(
                    modifier = Modifier.fillMaxSize().height(70.dp),
                    onClick = { showSaveDialog = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text("Save")
                }
            }
            Column(modifier = Modifier.width(170.dp).fillMaxHeight(), Arrangement.Center) {
                Button(
                    modifier = Modifier.fillMaxSize().height(70.dp),
                    onClick = { showLoadDialog = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text("Load")
                }
            }
        }
    }
}
