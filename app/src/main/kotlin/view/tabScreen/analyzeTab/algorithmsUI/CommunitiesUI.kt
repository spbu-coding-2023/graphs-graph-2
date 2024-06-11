package view.tabScreen.analyzeTab.algorithmsUI

import androidx.compose.runtime.Composable
import view.utils.RunAlgoButton
import viewmodel.graph.GraphViewModel

@Composable
fun <D> CommunitiesUI(graphVM: GraphViewModel<D>) {
    RunAlgoButton("No communities were found") { graphVM.findCommunities() }
}
