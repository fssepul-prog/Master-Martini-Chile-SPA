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

    // Si los datos son válidos, los guarda y recién entonces avisa a la pantalla para navegar.
    fun registrar(onRegistrado: () -> Unit) {
        if (!validarFormulario()) return
        val datos = _estado.value
        viewModelScope.launch {
            usuarios.guardar(datos)
            onRegistrado()
        }
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
