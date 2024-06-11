package view.tabScreen.analyzeTab.algorithmsUI

import androidx.compose.runtime.Composable
import view.utils.RunAlgoButton
import viewmodel.graph.GraphViewModel

@Composable
fun <D> SCCUI(graphVM: GraphViewModel<D>) {
    RunAlgoButton("No SCCs were found") { graphVM.findSCCs() }
}
