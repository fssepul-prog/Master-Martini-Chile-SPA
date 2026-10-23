package cl.duoc.mastermartini.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.navigation.Rutas
import cl.duoc.mastermartini.ui.components.BarraInferior
import cl.duoc.mastermartini.ui.components.BarraSuperior
import cl.duoc.mastermartini.viewmodel.PreferenciasViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreferenciasScreen(viewModel: PreferenciasViewModel, onIr: (Rutas) -> Unit) {
    val estado by viewModel.estado.collectAsState()
    val pref = estado.preferencias

    // Guía 12: texto derivado del estado y color animado según el interruptor.
    val resumen by remember { derivedStateOf { "${estado.preferencias.intereses.size} de ${viewModel.temas.size} temas elegidos" } }
    val fondoTarjeta by animateColorAsState(
        if (pref.letraGrande) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        label = "fondoTarjeta",
    )

    // El mensaje de éxito se oculta solo.
    LaunchedEffect(estado.guardado) {
        if (estado.guardado) {
            delay(2500)
            viewModel.ocultarMensaje()
        }
    }

    Scaffold(
        topBar = { BarraSuperior("Preferencias") },
        bottomBar = { BarraInferior(Rutas.Preferencias, onIr) },
    ) { padding ->
        if (estado.cargando) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            return@Scaffold
        }
        Column(
            modifier = Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Column(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(fondoTarjeta)
                    .padding(horizontal = 16.dp),
            ) {
                Interruptor("Letra grande", "Textos más fáciles de leer", pref.letraGrande, viewModel::cambiarLetraGrande)
                HorizontalDivider()
                Interruptor("Avisos de nuevos cursos", "Te avisamos de eventos y cursos", pref.avisos, viewModel::cambiarAvisos)
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("¿Qué te interesa aprender?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(resumen, color = MaterialTheme.colorScheme.onSurfaceVariant)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    viewModel.temas.forEach { tema ->
                        FilterChip(
                            selected = tema in pref.intereses,
                            onClick = { viewModel.alternarInteres(tema) },
                            label = { Text(tema) },
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = estado.guardado,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 },
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text("Preferencias guardadas")
                }
            }

            Button(onClick = viewModel::guardar, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Text("Guardar preferencias")
            }
        }
    }
}

@Composable
private fun Interruptor(titulo: String, ayuda: String, activo: Boolean, onCambio: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .toggleable(value = activo, role = Role.Switch, onValueChange = onCambio)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(titulo, fontWeight = FontWeight.Bold)
            Text(ayuda, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = activo, onCheckedChange = null)
    }
}
