package view.tabScreen.analyzeTab.algorithmsUI

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import view.tabScreen.analyzeTab.borderPadding
import view.tabScreen.analyzeTab.horizontalGap
import view.tabScreen.analyzeTab.rowHeight
import viewmodel.graph.GraphViewModel

@Composable
fun <D> CyclesUI(graphVM: GraphViewModel<D>) {
    var vertexId by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.height(rowHeight).padding(borderPadding),
        horizontalArrangement = Arrangement.spacedBy(horizontalGap)
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), Arrangement.Center) {
            TextField(
                value = vertexId,
                onValueChange = { vertexId = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                textStyle = TextStyle(fontSize = 14.sp),
                label = {
                    Text(
                        "Vertex ID",
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
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), Arrangement.Center) {
            Button(
                modifier = Modifier.fillMaxSize(),
                onClick = {},
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
            ) {
                Text("Run algorithm")
            }
        }
    }
}
