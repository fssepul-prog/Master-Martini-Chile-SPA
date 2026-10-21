package cl.duoc.mastermartini.navigation

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.duoc.mastermartini.model.Contenido
import cl.duoc.mastermartini.ui.screens.ajustes.AjustesScreen
import cl.duoc.mastermartini.ui.screens.carga.CargaScreen
import cl.duoc.mastermartini.ui.screens.contenidos.ContenidosScreen
import cl.duoc.mastermartini.ui.screens.contenidos.DetalleScreen
import cl.duoc.mastermartini.ui.screens.inicio.InicioScreen
import cl.duoc.mastermartini.ui.screens.perfil.PerfilScreen
import cl.duoc.mastermartini.ui.screens.registro.RegistroScreen
import cl.duoc.mastermartini.viewmodel.ContenidoViewModel
import cl.duoc.mastermartini.viewmodel.PreferenciasViewModel
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation(anchoVentana: WindowWidthSizeClass, preferenciasViewModel: PreferenciasViewModel) {
    val navController = rememberNavController()
    // Creados una sola vez para que las pantallas compartan la misma instancia (Guía 11).
    val contenidoViewModel: ContenidoViewModel = viewModel()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    // Pestañas de la barra inferior: sin copias repetidas en la pila (Guía 10).
    val irA: (Rutas) -> Unit = { destino ->
        navController.navigate(destino.ruta) {
            popUpTo(Rutas.Inicio.ruta)
            launchSingleTop = true
        }
    }

    // En tablet el detalle se ve junto a la lista; en teléfono es otra pantalla (Guía 9).
    val abrirDetalle: (Contenido) -> Unit = { contenido ->
        contenidoViewModel.seleccionar(contenido)
        if (anchoVentana == WindowWidthSizeClass.Expanded) irA(Rutas.Contenidos) else navController.navigate(Rutas.Detalle.ruta)
    }

    NavHost(navController = navController, startDestination = Rutas.Carga.ruta) {
        composable(Rutas.Carga.ruta) {
            CargaScreen(estaRegistrado = usuarioViewModel::estaRegistrado, onListo = { registrado ->
                val destino = if (registrado) Rutas.Inicio else Rutas.Registro
                navController.navigate(destino.ruta) { popUpTo(Rutas.Carga.ruta) { inclusive = true } }
            })
        }
        composable(Rutas.Registro.ruta) {
            RegistroScreen(usuarioViewModel, onRegistrado = {
                navController.navigate(Rutas.Inicio.ruta) { popUpTo(Rutas.Registro.ruta) { inclusive = true } }
            })
        }
        composable(Rutas.Inicio.ruta) { InicioScreen(contenidoViewModel, onIr = irA, onAbrir = abrirDetalle) }
        composable(Rutas.Contenidos.ruta) { ContenidosScreen(contenidoViewModel, anchoVentana, onIr = irA, onAbrir = abrirDetalle) }
        composable(Rutas.Detalle.ruta) { DetalleScreen(contenidoViewModel, onVolver = { navController.popBackStack() }) }
        composable(Rutas.Perfil.ruta) {
            PerfilScreen(usuarioViewModel, preferenciasViewModel, onIr = irA)
        }
        composable(Rutas.Ajustes.ruta) { AjustesScreen(preferenciasViewModel, onIr = irA) }
    }
}
