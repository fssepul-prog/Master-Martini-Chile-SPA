package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import cl.duoc.mastermartini.ui.components.BarraSuperior
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.model.Contenido
import cl.duoc.mastermartini.model.TipoContenido
import cl.duoc.mastermartini.navigation.Rutas
import cl.duoc.mastermartini.ui.components.BarraInferior
import cl.duoc.mastermartini.ui.components.ImagenContenido
import cl.duoc.mastermartini.viewmodel.ContenidoViewModel

// Guía 10 (TopAppBar, NavigationBar, lista) + Guía 9 (Expanded = lista y detalle lado a lado).
@Composable
fun ContenidosScreen(
    viewModel: ContenidoViewModel,
    anchoVentana: WindowWidthSizeClass,
    onIr: (Rutas) -> Unit,
    onAbrir: (Contenido) -> Unit,
) {
    val estado by viewModel.estado.collectAsState()
    val expandida = anchoVentana == WindowWidthSizeClass.Expanded
    val seleccionado = estado.seleccionado

    Scaffold(
        topBar = { BarraSuperior("Contenidos") },
        bottomBar = { BarraInferior(Rutas.Contenidos, onIr) },
    ) { padding ->
        Row(Modifier.padding(padding).fillMaxSize()) {
            Column(if (expandida) Modifier.width(400.dp) else Modifier.fillMaxWidth()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item { FilterChip(selected = estado.filtro == null, onClick = { viewModel.filtrar(null) }, label = { Text("Todos") }) }
                    items(TipoContenido.entries) { tipo ->
                        FilterChip(selected = estado.filtro == tipo, onClick = { viewModel.filtrar(tipo) }, label = { Text(tipo.plural) })
                    }
                }
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(estado.lista, key = { it.id }) { contenido ->
                        FilaContenido(contenido, marcado = expandida && contenido == seleccionado) { onAbrir(contenido) }
                    }
                }
            }
            if (expandida) {
                VerticalDivider()
                if (seleccionado != null) {
                    DetalleContenido(seleccionado, Modifier.weight(1f))
                } else {
                    Box(Modifier.weight(1f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text("Elige un contenido de la lista", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaContenido(contenido: Contenido, marcado: Boolean, onClick: () -> Unit) {
    OutlinedCard(
        onClick = onClick,
        colors = if (marcado) CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer) else CardDefaults.outlinedCardColors(),
    ) {
        Row(Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            ImagenContenido(contenido.tipo, Modifier.size(72.dp))
            Column(Modifier.weight(1f)) {
                Text(contenido.tipo.name.uppercase(), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                Text(contenido.titulo, fontWeight = FontWeight.Bold)
                Text(contenido.detalle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
