package cl.duoc.mastermartini.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartini.model.UsuarioErrores
import cl.duoc.mastermartini.model.UsuarioUiState
import cl.duoc.mastermartini.repository.FotoRepository
import cl.duoc.mastermartini.repository.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Guías 11 y 13: ViewModel compartido entre Registro y Perfil (datos + foto).
class UsuarioViewModel(app: Application) : AndroidViewModel(app) {
    private val usuarios = UsuarioRepository(app)
    private val fotos = FotoRepository(app)

    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado

    // Guía 13: Uri de la imagen elegida o capturada (null = sin foto).
    // shortcut: la foto vive solo mientras la app está abierta, guardarla en DataStore si se pide que persista.
    private val _fotoUri = MutableStateFlow<Uri?>(null)
    val fotoUri: StateFlow<Uri?> = _fotoUri

    init {
        // Si ya se registró antes, el Perfil muestra sus datos guardados.
        viewModelScope.launch { usuarios.usuario.first()?.let { _estado.value = it } }
    }

    suspend fun estaRegistrado(): Boolean = usuarios.usuario.first() != null

    fun onNombre(v: String) = editar { it.copy(nombre = v) }
    fun onCorreo(v: String) = editar { it.copy(correo = v) }
    fun onTelefono(v: String) = editar { it.copy(telefono = v) }
    fun onRut(v: String) = editar { it.copy(rutEmpresa = v) }
    fun onTipoNegocio(v: String) = editar { it.copy(tipoNegocio = v) }
    fun onClave(v: String) = editar { it.copy(clave = v) }
    fun onTerminos(v: Boolean) = editar { it.copy(aceptaTerminos = v) }

    // Tras el primer intento de envío, los errores se recalculan en tiempo real mientras se corrige.
    private fun editar(cambio: (UsuarioUiState) -> UsuarioUiState) = _estado.update {
        val nuevo = cambio(it)
        if (it.errores != UsuarioErrores()) nuevo.copy(errores = validar(nuevo)) else nuevo
    }

    fun validarFormulario(): Boolean {
        val errores = validar(_estado.value)
        _estado.update { it.copy(errores = errores) }
        return errores == UsuarioErrores()
    }

    // true = datos válidos y guardándose; la pantalla decide a dónde navegar.
    fun registrar(): Boolean {
        if (!validarFormulario()) return false
        val datos = _estado.value
        viewModelScope.launch { usuarios.guardar(datos) }
        return true
    }

    fun nuevaUriParaCamara(): Uri = fotos.nuevaUriParaCamara()

    // Cancelar la cámara o la galería no cambia la foto actual.
    fun fotoTomada(uri: Uri, exito: Boolean) {
        if (exito) _fotoUri.value = uri
    }

    fun fotoDeGaleria(uri: Uri?) {
        if (uri != null) _fotoUri.value = uri
    }
}

val tiposDeNegocio = listOf("Pastelería", "Panadería", "Chocolatería", "Heladería", "Restaurante o casino", "Otro")

// Reglas de validación, separadas de la UI y sin dependencias de Android para poder probarlas.
fun validar(u: UsuarioUiState) = UsuarioErrores(
    nombre = if (u.nombre.trim().length < 3) "Ingresa tu nombre completo" else null,
    correo = if (!Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matches(u.correo.trim())) "Ingresa un correo válido, ej: nombre@correo.cl" else null,
    telefono = if (u.telefono.count(Char::isDigit) != 9) "Ingresa 9 dígitos, ej: 9 1234 5678" else null,
    rutEmpresa = if (!rutValido(u.rutEmpresa)) "RUT inválido, ej: 76.123.456-0" else null,
    tipoNegocio = if (u.tipoNegocio !in tiposDeNegocio) "Elige el tipo de negocio" else null,
    clave = if (u.clave.length < 8) "Mínimo 8 caracteres" else null,
    terminos = if (!u.aceptaTerminos) "Debes aceptar los términos" else null,
)

// Dígito verificador chileno (módulo 11).
fun rutValido(rut: String): Boolean {
    val limpio = rut.trim().replace(".", "").replace("-", "").uppercase()
    if (limpio.length < 2) return false
    val cuerpo = limpio.dropLast(1)
    if (!cuerpo.all(Char::isDigit)) return false
    var suma = 0
    var factor = 2
    for (c in cuerpo.reversed()) {
        suma += (c - '0') * factor
        factor = if (factor == 7) 2 else factor + 1
    }
    val esperado = when (val r = 11 - suma % 11) {
        11 -> '0'
        10 -> 'K'
        else -> '0' + r
    }
    return limpio.last() == esperado
}
