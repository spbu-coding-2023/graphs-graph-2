package view.tabScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import viewmodel.graph.GraphViewModel

@Composable
fun <D> FileControlTab(graphVM: GraphViewModel<D>) {
    Column(modifier = Modifier.fillMaxSize()) { Text("hahahhahh 3rd tab") }
}
