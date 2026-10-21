package cl.duoc.mastermartini.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import cl.duoc.mastermartini.navigation.Rutas

data class Pestana(val ruta: Rutas, val titulo: String, val icono: ImageVector)

val pestanas = listOf(
    Pestana(Rutas.Inicio, "Inicio", Icons.Outlined.Home),
    Pestana(Rutas.Contenidos, "Contenidos", Icons.Outlined.PlayCircle),
    Pestana(Rutas.Perfil, "Perfil", Icons.Outlined.Person),
    Pestana(Rutas.Ajustes, "Ajustes", Icons.Outlined.Tune),
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
            )
        }
    }
}
