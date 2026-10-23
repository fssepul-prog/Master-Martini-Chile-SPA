package cl.duoc.mastermartini.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import cl.duoc.mastermartini.navigation.Rutas
import cl.duoc.mastermartini.ui.components.BarraInferior
import cl.duoc.mastermartini.ui.components.BarraSuperior
import cl.duoc.mastermartini.ui.components.ImagenInteligente
import cl.duoc.mastermartini.viewmodel.PreferenciasViewModel
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel

// Guía 13: TakePicture() y GetContent() con rememberLauncherForActivityResult; el resultado va al ViewModel.
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PerfilScreen(usuarioViewModel: UsuarioViewModel, preferenciasViewModel: PreferenciasViewModel, onIr: (Rutas) -> Unit) {
    val usuario by usuarioViewModel.estado.collectAsState()
    val foto by usuarioViewModel.fotoUri.collectAsState()
    val preferencias by preferenciasViewModel.estado.collectAsState()
    val context = LocalContext.current

    // Uri donde la cámara escribe; se guarda para sobrevivir a la rotación mientras la cámara está abierta.
    var uriPendiente by rememberSaveable { mutableStateOf<Uri?>(null) }
    var aviso by rememberSaveable { mutableStateOf<String?>(null) }

    val galeria = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        usuarioViewModel.fotoDeGaleria(uri)
    }
    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        uriPendiente?.let { usuarioViewModel.fotoTomada(it, exito) }
        uriPendiente = null
    }
    val abrirCamara = {
        val uri = usuarioViewModel.nuevaUriParaCamara()
        uriPendiente = uri
        camara.launch(uri)
    }
    val permisoCamara = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) abrirCamara() else aviso = "Sin permiso de cámara no podemos tomar la foto. Puedes elegir una de la galería."
    }

    Scaffold(
        topBar = { BarraSuperior("Mi perfil") },
        bottomBar = { BarraInferior(Rutas.Perfil, onIr) },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ImagenInteligente(foto)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedButton(onClick = { galeria.launch("image/*") }, modifier = Modifier.weight(1f).height(52.dp)) {
                    Text("Galería")
                }
                Button(
                    onClick = {
                        aviso = null
                        val concedido = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        if (concedido) abrirCamara() else permisoCamara.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                ) {
                    Text("Cámara")
                }
            }
            aviso?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            // Guía 11: resumen de los datos del mismo ViewModel usado en el registro.
            OutlinedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Mis datos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Dato("Nombre", usuario.nombre)
                    Dato("Correo", usuario.correo)
                    Dato("Teléfono", if (usuario.telefono.isBlank()) "" else "+56 ${usuario.telefono}")
                    Dato("RUT empresa", usuario.rutEmpresa)
                    Dato("Negocio", usuario.tipoNegocio)
                }
            }

            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Mis intereses", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    preferencias.preferencias.intereses.forEach { AssistChip(onClick = { onIr(Rutas.Preferencias) }, label = { Text(it) }) }
                }
                TextButton(onClick = { onIr(Rutas.Preferencias) }) {
                    Text(if (preferencias.preferencias.intereses.isEmpty()) "Elegir intereses" else "Editar intereses")
                }
            }
        }
    }
}

@Composable
private fun Dato(etiqueta: String, valor: String) {
    Row {
        Text(etiqueta, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.width(120.dp))
        Text(valor.ifBlank { "—" }, fontWeight = FontWeight.Bold)
    }
}
