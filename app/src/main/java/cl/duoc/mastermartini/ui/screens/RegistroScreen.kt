package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.selection.toggleable
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.ui.components.BarraSuperior
import cl.duoc.mastermartini.viewmodel.UsuarioViewModel
import cl.duoc.mastermartini.repository.UsuarioRepository

// Guía 11: formulario con OutlinedTextField, isError y supportingText. Toda la validación vive en el ViewModel.
@Composable
fun RegistroScreen(viewModel: UsuarioViewModel, onRegistrado: () -> Unit) {
    val estado by viewModel.estado.collectAsState()
    val e = estado.errores
    var verClave by rememberSaveable { mutableStateOf(false) }

    Scaffold(topBar = { BarraSuperior("Crear cuenta") }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Completa tus datos para recibir contenidos de tu rubro.", color = MaterialTheme.colorScheme.onSurfaceVariant)

            Campo("Nombre completo", estado.nombre, viewModel::onNombre, e.nombre, Icons.Default.Person)
            Campo("Correo electrónico", estado.correo, viewModel::onCorreo, e.correo, Icons.Default.Email, KeyboardType.Email)
            Campo("Teléfono (+56)", estado.telefono, viewModel::onTelefono, e.telefono, Icons.Default.Phone, KeyboardType.Phone)
            Campo("RUT empresa", estado.rutEmpresa, viewModel::onRut, e.rutEmpresa, Icons.Default.AccountBox, ayuda = "Ej: 76.123.456-0")
            SelectorTipoNegocio(estado.tipoNegocio, viewModel::onTipoNegocio, e.tipoNegocio)
            Campo(
                "Contraseña", estado.clave, viewModel::onClave, e.clave, Icons.Default.Lock, KeyboardType.Password,
                ayuda = "Mínimo 8 caracteres",
                transformacion = if (verClave) VisualTransformation.None else PasswordVisualTransformation(),
                accion = {
                    TextButton(onClick = { verClave = !verClave }) {
                        Text(if (verClave) "Ocultar" else "Ver")
                    }
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(value = estado.aceptaTerminos, role = Role.Checkbox, onValueChange = viewModel::onTerminos),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = estado.aceptaTerminos, onCheckedChange = null)
                Text("Acepto los términos y el uso de mis datos")
            }
            e.terminos?.let { Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium) }

            Button(
                onClick = { viewModel.registrar(onRegistrado) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
            ) { Text("Registrarme") }
        }
    }
}

@Composable
private fun Campo(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    error: String?,
    icono: ImageVector,
    teclado: KeyboardType = KeyboardType.Text,
    ayuda: String? = null,
    transformacion: VisualTransformation = VisualTransformation.None,
    accion: (@Composable () -> Unit)? = null,
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        // El ícono de error acompaña al color rojo: no depender solo del color.
        trailingIcon = accion ?: if (error != null) {
            { Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error) }
        } else null,
        isError = error != null,
        supportingText = (error ?: ayuda)?.let { { Text(it) } },
        singleLine = true,
        visualTransformation = transformacion,
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = Modifier.fillMaxWidth(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorTipoNegocio(valor: String, onCambio: (String) -> Unit, error: String?) {
    var abierto by rememberSaveable { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = abierto, onExpandedChange = { abierto = it }) {
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo de negocio") },
            leadingIcon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = abierto) },
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
        )
        ExposedDropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            UsuarioRepository.tiposDeNegocio.forEach { tipo ->
                DropdownMenuItem(text = { Text(tipo) }, onClick = {
                    onCambio(tipo)
                    abierto = false
                })
            }
        }
    }
}
