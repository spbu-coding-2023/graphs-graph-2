package view

import MyAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import view.graph.GraphView
import view.tabScreen.TabHandler
import view.utils.FAQBox
import view.utils.ZoomBox
import viewmodel.MainScreenViewModel

@Composable
fun <D> MainScreen(viewmodel: MainScreenViewModel<D>) {
    MyAppTheme {
        // state for hover effect
        val interactionSource = remember { MutableInteractionSource() }
        val scale = remember { mutableStateOf(1f) }

        Column {
            Row {
                TabHandler(viewmodel)
                Surface(modifier = Modifier.fillMaxSize()) {
                    GraphView(viewmodel.graphViewModel, scale)
                }
            }
            // Hoverable box over the existing Surface
            FAQBox(interactionSource, viewmodel)
            ZoomBox(scale)
        }
    }
}
