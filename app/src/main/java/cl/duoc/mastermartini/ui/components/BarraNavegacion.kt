package cl.duoc.mastermartini.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import cl.duoc.mastermartini.navigation.Rutas

data class Pestana(val ruta: Rutas, val titulo: String, val icono: ImageVector)

val pestanas = listOf(
    Pestana(Rutas.Inicio, "Inicio", Icons.Default.Home),
    Pestana(Rutas.Contenidos, "Contenidos", Icons.Default.PlayArrow),
    Pestana(Rutas.Perfil, "Perfil", Icons.Default.Person),
    Pestana(Rutas.Preferencias, "Preferencias", Icons.Default.Settings),
)

// Guía 10: cada pantalla pone esta barra en el bottomBar de su Scaffold e indica cuál es ella.
@Composable
fun BarraInferior(actual: Rutas, onIr: (Rutas) -> Unit) {
    NavigationBar {
        pestanas.forEach { p ->
            NavigationBarItem(
                selected = p.ruta == actual,
                onClick = { onIr(p.ruta) },
                icon = { Icon(p.icono, contentDescription = null) },
                label = { Text(p.titulo) },
                // Verde en vez del dorado por defecto: el dorado no se lee bien sobre fondo claro.
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        }
    }
}
