package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.model.Contenido
import cl.duoc.mastermartini.ui.components.BarraSuperior
import cl.duoc.mastermartini.ui.components.ImagenContenido
import cl.duoc.mastermartini.viewmodel.ContenidoViewModel

// Teléfono: el detalle es su propia ruta; la flecha y el botón atrás del sistema vuelven a la lista.
@Composable
fun DetalleScreen(viewModel: ContenidoViewModel, onVolver: () -> Unit) {
    val estado by viewModel.estado.collectAsState()
    val contenido = estado.seleccionado
    Scaffold(
        topBar = {
            BarraSuperior(contenido?.tipo?.name ?: "") {
                IconButton(onClick = onVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        },
    ) { padding ->
        if (contenido != null) DetalleContenido(contenido, Modifier.padding(padding))
    }
}

@Composable
fun DetalleContenido(contenido: Contenido, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ImagenContenido(contenido.tipo, contenido.imagen, Modifier.fillMaxWidth().height(200.dp))
        Text("${contenido.tipo.name.uppercase()} · ${contenido.detalle}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(contenido.titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(contenido.descripcion)
    }
}

