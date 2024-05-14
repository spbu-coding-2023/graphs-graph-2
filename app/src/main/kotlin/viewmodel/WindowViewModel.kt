package viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class WindowViewModel(
    private var height: MutableState<Dp> = mutableStateOf(0.dp),
    private var width: MutableState<Dp> = mutableStateOf(0.dp),
) {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun SetCurrentDimensions() {
        val configuration = LocalWindowInfo.current.containerSize
        println(configuration)
        height.value = configuration.height.dp
        width.value = configuration.width.dp
    }

    val getHeight
        get() = height.value

    val getWidth
        get() = width.value
}
