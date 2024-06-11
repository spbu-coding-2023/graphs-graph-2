package view.tabScreen.analyzeTab.algorithmsUI

import androidx.compose.runtime.Composable
import view.components.RunAlgoButton
import viewmodel.graph.GraphViewModel

@Composable
fun <D> KeyVerticesUI(graphVM: GraphViewModel<D>) {
    RunAlgoButton("No key vertices were found") { graphVM.findKeyVertices() }
}
