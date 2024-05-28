package view.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import scaleFactor

@Composable
fun ZoomBox(currentScale: MutableState<Float>) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom
        ) {
            FloatingActionButton(onClick = {
                currentScale.value = (scaleFactor * currentScale.value).coerceIn(0.7f, 1.9f)
            }) {
                Text("+")
            }
            Spacer(modifier = Modifier.height(8.dp))
            FloatingActionButton(onClick = {
                currentScale.value = (currentScale.value / scaleFactor).coerceIn(0.7f, 1.9f)
            }) {
                Text("-")
            }
        }
    }
}
