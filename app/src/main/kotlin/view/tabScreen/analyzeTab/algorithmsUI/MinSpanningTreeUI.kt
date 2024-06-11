package view.tabScreen.analyzeTab.algorithmsUI

import androidx.compose.runtime.Composable
import view.utils.RunAlgoButton
import viewmodel.graph.GraphViewModel

@Composable
fun <D> MinSpanningTreeUI(graphVM: GraphViewModel<D>) {
    RunAlgoButton("No min spanning tree was found") { graphVM.findMinSpanningTree() }
}
