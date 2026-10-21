package cl.duoc.mastermartini.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

private val Colores = lightColorScheme(
    primary = VerdeMartini,
    onPrimary = Color.White,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = VerdeMartini,
    secondary = Dorado,
    secondaryContainer = Crema,
    background = Fondo,
    onBackground = Texto,
    surface = Fondo,
    onSurface = Texto,
    onSurfaceVariant = TextoSecundario,
    error = Error,
)

// Textos más grandes que el default: el caso pide accesibilidad para usuarios mayores de 50 años.
private val Tipografia = Typography().run {
    copy(
        bodyLarge = bodyLarge.copy(fontSize = 18.sp, lineHeight = 26.sp),
        bodyMedium = bodyMedium.copy(fontSize = 16.sp, lineHeight = 24.sp),
        labelLarge = labelLarge.copy(fontSize = 16.sp),
        titleLarge = TextStyle(fontSize = 24.sp, lineHeight = 32.sp),
    )
}

// Preferencia "letra grande" (Guía 12): la misma tipografía con tamaños mayores.
private val TipografiaGrande = Tipografia.copy(
    bodyLarge = Tipografia.bodyLarge.copy(fontSize = 22.sp, lineHeight = 30.sp),
    bodyMedium = Tipografia.bodyMedium.copy(fontSize = 20.sp, lineHeight = 28.sp),
    labelLarge = Tipografia.labelLarge.copy(fontSize = 19.sp),
    titleMedium = Tipografia.titleMedium.copy(fontSize = 22.sp),
    titleLarge = Tipografia.titleLarge.copy(fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall = Tipografia.headlineSmall.copy(fontSize = 30.sp),
)

@Composable
fun MasterMartiniTheme(letraGrande: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Colores,
        typography = if (letraGrande) TipografiaGrande else Tipografia,
        content = content,
    )
}
