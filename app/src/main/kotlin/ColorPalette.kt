import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class ColorPalette(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color,
)

@Composable
fun MyAppTheme(content: @Composable () -> Unit) {
    val mycolors = ColorPalette(
        primary = Color(0xFFFF5094),
        secondary = Color(0xFFFFB5BB),
        background = Color(0xFFFEEEEC),
        surface = Color.White,
    )

    fun ColorPalette.toMaterialColors() = lightColors(
        primary = primary,
        secondary = secondary,
        background = background,
        surface = surface,
    )

    MaterialTheme(
        colors = mycolors.toMaterialColors()
    ) {
        content()
    }
}
