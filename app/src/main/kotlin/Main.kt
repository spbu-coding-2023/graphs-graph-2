import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import model.UndirectedGraph
import model.abstractGraph.Graph
import view.MainScreen
import viewmodel.MainScreenViewModel
import java.awt.Dimension

val testGraph: Graph<Int> = UndirectedGraph<Int>().apply {
    val v1 = addVertex(1)
    val v2 = addVertex(2)
    val v3 = addVertex(3)
    val v4 = addVertex(4)
    val v5 = addVertex(5)

    addEdge(v1, v5)
    addEdge(v1, v4)
    addEdge(v1, v3)
    addEdge(v1, v2)
    addEdge(v2, v4)
}


@Composable
@Preview
fun App() {
    MaterialTheme { MainScreen(MainScreenViewModel(testGraph)) }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Graphs-2",
        state = rememberWindowState(
            position = WindowPosition(alignment = Alignment.Center),
        ),) {
        window.minimumSize = Dimension(1200, 700)
        App()
    }
}
