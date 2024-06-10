package view.tabScreen.analyzeTab.algorithmsUI

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import view.tabScreen.analyzeTab.borderPadding
import view.tabScreen.analyzeTab.horizontalGap
import view.tabScreen.analyzeTab.rowHeight
import view.utils.dialogWindows.ErrorWindow
import viewmodel.graph.GraphViewModel

@Composable
fun <D> CommunitiesUI(graphVM: GraphViewModel<D>) {
    var showErrorWindow by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.height(rowHeight).padding(borderPadding),
        horizontalArrangement = Arrangement.spacedBy(horizontalGap)
    ) {
        Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(), Arrangement.Center) {
            Button(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    if (!graphVM.findCommunities()) {
                        showErrorWindow = true
                    }
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary)
            ) {
                Text("Run algorithm")
            }
        }
    }

    if (showErrorWindow) {
        ErrorWindow("No communities were found") { showErrorWindow = false }
    }
}
