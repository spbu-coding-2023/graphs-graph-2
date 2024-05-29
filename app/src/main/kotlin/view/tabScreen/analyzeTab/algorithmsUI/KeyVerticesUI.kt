package view.tabScreen.analyzeTab.algorithmsUI

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import view.tabScreen.analyzeTab.borderPadding
import view.tabScreen.analyzeTab.horizontalGap
import view.tabScreen.analyzeTab.rowHeight
import view.utils.ErrorWindow
import viewmodel.graph.GraphViewModel

@Composable
fun <D> KeyVerticesUI(graphVM: GraphViewModel<D>) {
    val showErrorWindow = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.height(rowHeight).padding(borderPadding),
        horizontalArrangement = Arrangement.spacedBy(horizontalGap)
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), Arrangement.Center) {
            Button(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    if (!graphVM.findKeyVertices()) {
                        showErrorWindow.value = true
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
            ) {
                Text("Run algorithm")
            }
        }
    }

    if (showErrorWindow.value) {
        ErrorWindow("No key vertices were found", { showErrorWindow.value = false })
    }
}
