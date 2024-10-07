package cl.bootcamp.shoestapapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color.Black, // Cambia esto según sea necesario
    secondary = Color(0xFF03DAC5),
    background = Color.White,
    surface = Color.White

)

@Composable
fun ShoesTapAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
