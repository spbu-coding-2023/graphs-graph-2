package view.tabScreen.analyzeTab.algorithmsUI

import androidx.compose.runtime.Composable
import view.utils.RunAlgoButton
import viewmodel.graph.GraphViewModel

@Composable
fun <D> BridgesUI(graphVM: GraphViewModel<D>) {
    RunAlgoButton("No bridges were found") { graphVM.findBridges() }
}
