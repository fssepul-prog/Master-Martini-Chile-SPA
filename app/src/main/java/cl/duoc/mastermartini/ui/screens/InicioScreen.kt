package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import cl.duoc.mastermartini.ui.components.BarraSuperior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.navigation.Rutas
import cl.duoc.mastermartini.model.Contenido
import cl.duoc.mastermartini.ui.components.BarraInferior
import cl.duoc.mastermartini.ui.components.ImagenContenido
import cl.duoc.mastermartini.ui.components.pestanas
import cl.duoc.mastermartini.viewmodel.ContenidoViewModel
import kotlinx.coroutines.launch

// Guía 10: TopAppBar + NavigationDrawer. Los datos vienen del ContenidoViewModel.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(viewModel: ContenidoViewModel, nombre: String, onIr: (Rutas) -> Unit, onAbrir: (Contenido) -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Master Martini", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(24.dp))
                pestanas.forEach { p ->
                    NavigationDrawerItem(
                        label = { Text(p.titulo) },
                        icon = { Icon(p.icono, contentDescription = null) },
                        selected = p.ruta == Rutas.Inicio,
                        onClick = {
                            scope.launch { drawerState.close() }
                            onIr(p.ruta)
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                BarraSuperior("Master Martini") {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Abrir menú")
                    }
                }
            },
            bottomBar = { BarraInferior(Rutas.Inicio, onIr) },
        ) { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    Text("Hola, ${nombre.trim().substringBefore(" ").ifBlank { "bienvenido" }}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("¿Qué quieres aprender hoy?", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                item { CursoDestacado(viewModel.destacado, onVer = onAbrir) }
                item {
                    Titulo("Líneas de negocio")
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        viewModel.lineasDeNegocio.chunked(2).forEach { fila ->
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                fila.forEach { linea ->
                                    OutlinedCard(
                                        onClick = {
                                            viewModel.seleccionar(null)
                                            onIr(Rutas.Contenidos)
                                        },
                                        modifier = Modifier.weight(1f).heightIn(min = 72.dp),
                                    ) {
                                        Text(linea, fontWeight = FontWeight.Bold, modifier = Modifier.padding(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Titulo("Recetas recientes")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(viewModel.recetasRecientes, key = { it.id }) { receta ->
                            OutlinedCard(
                                onClick = { onAbrir(receta) },
                                modifier = Modifier.width(180.dp),
                            ) {
                                ImagenContenido(receta.tipo, Modifier.fillMaxWidth().heightIn(min = 90.dp))
                                Column(Modifier.padding(12.dp)) {
                                    Text(receta.titulo, fontWeight = FontWeight.Bold, maxLines = 2)
                                    Text(receta.detalle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CursoDestacado(curso: Contenido, onVer: (Contenido) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("CURSO DESTACADO", style = MaterialTheme.typography.labelLarge)
            Text(curso.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(curso.detalle)
            Spacer(Modifier.size(4.dp))
            Button(
                onClick = { onVer(curso) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                ),
            ) { Text("Ver curso") }
        }
    }
}

@Composable
private fun Titulo(texto: String) {
    Text(texto, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
}
