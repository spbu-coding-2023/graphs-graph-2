import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.awt.Dimension
import view.utils.SelectInitDialogWindow

@Composable
@Preview
fun App() {
    MaterialTheme { SelectInitDialogWindow(true).GraphInitDialogWindow(true) }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "WUDU",
        state = rememberWindowState(position = WindowPosition(alignment = Alignment.Center)),
    ) {
        window.minimumSize = Dimension(1200, 700)
        App()
    }
}
