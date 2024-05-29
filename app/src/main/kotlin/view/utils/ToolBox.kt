package view.utils

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import scaleFactor
import view.tabScreen.analyzeTab.horizontalGap
import viewmodel.graph.GraphViewModel

@Composable
fun <D> ToolBox(graphVM: GraphViewModel<D>, currentScale: MutableState<Float>) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom
        ) {
            FloatingActionButton(
                onClick = {
                currentScale.value = (scaleFactor * currentScale.value).coerceIn(0.7f, 1.9f)
            },
                modifier = Modifier.padding(horizontal = 11.dp)
            ) {
                Text("+")
            }
            Spacer(modifier = Modifier.height(8.dp))
            FloatingActionButton(onClick = {
                currentScale.value = (currentScale.value / scaleFactor).coerceIn(0.7f, 1.9f)
            },
                modifier = Modifier.padding(horizontal = 11.dp)
            ) {
                Text("-")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier
                    .width(80.dp)
                    .height(50.dp)
                    .clip(shape = RoundedCornerShape(25.dp)),
                onClick = {
                    graphVM.clearGraph()
                }
            ) {
                Text("Clear")
            }
        }
    }
}
