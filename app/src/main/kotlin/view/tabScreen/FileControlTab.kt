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

@Composable
fun <D> FileControlTab(graphVM: GraphViewModel<D>) {
    var showSaveDialog by remember { mutableStateOf(false) }
    var showLoadDialog by remember { mutableStateOf(false) }
    var graphName by remember { mutableStateOf("") }

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
                    onValueChange = { graphName = it },
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
            Column(modifier = Modifier.width(fieldWidth).fillMaxHeight(), Arrangement.Center) {
                Button(
                    modifier = Modifier.fillMaxSize().height(fieldHeight),
                    onClick = { showSaveDialog = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text("Save")
                }
            }
            Column(modifier = Modifier.width(fieldWidth).fillMaxHeight(), Arrangement.Center) {
                Button(
                    modifier = Modifier.fillMaxSize().height(fieldHeight),
                    onClick = { showLoadDialog = true },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
                ) {
                    Text("Load")
                }
            }
        }
    }


    val dialogueHeight = 300.dp
    val dialogueWidth = 400.dp
    val padding = 14.dp

    if (showSaveDialog) {
        Dialog(
            onDismissRequest = {
                showSaveDialog = false
            }
        ) {
            Column(
                modifier =
                Modifier
                    .background(Color.White)
                    .padding(padding)
                    .width(dialogueWidth)
                    .height(dialogueHeight)
            ) {}
        }
    }

    if (showLoadDialog) {
        Dialog(
            onDismissRequest = {
                showLoadDialog = false
            }
        ) {
            Column(
                modifier =
                Modifier
                    .background(Color.White)
                    .padding(padding)
                    .width(dialogueWidth)
                    .height(dialogueHeight)
            ) {}
        }
    }
}
