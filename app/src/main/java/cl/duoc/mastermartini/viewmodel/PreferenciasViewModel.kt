package cl.duoc.mastermartini.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.mastermartini.model.Preferencias
import cl.duoc.mastermartini.model.PreferenciasUiState
import cl.duoc.mastermartini.repository.PreferenciasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Guía 12: cambios en memoria hasta presionar Guardar; "guardado" activa el AnimatedVisibility.
class PreferenciasViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = PreferenciasRepository(app)

    private val _estado = MutableStateFlow(PreferenciasUiState())
    val estado: StateFlow<PreferenciasUiState> = _estado

    // "Letra grande" ya guardada: es la que usa el tema de toda la app (no cambia hasta presionar Guardar).
    private val _letraGrande = MutableStateFlow(false)
    val letraGrande: StateFlow<Boolean> = _letraGrande

    val temas = PreferenciasRepository.temasDeInteres

    init {
        viewModelScope.launch {
            val guardadas = repo.preferencias.first()
            _estado.value = PreferenciasUiState(cargando = false, preferencias = guardadas)
            _letraGrande.value = guardadas.letraGrande
        }
    }

    fun cambiarLetraGrande(activo: Boolean) = editar { it.copy(letraGrande = activo) }
    fun cambiarAvisos(activo: Boolean) = editar { it.copy(avisos = activo) }
    fun alternarInteres(tema: String) = editar {
        it.copy(intereses = if (tema in it.intereses) it.intereses - tema else it.intereses + tema)
    }

    fun guardar() {
        val actuales = _estado.value.preferencias
        viewModelScope.launch {
            repo.guardar(actuales)
            _letraGrande.value = actuales.letraGrande
            _estado.update { it.copy(guardado = true) }
        }
    }

    fun ocultarMensaje() = _estado.update { it.copy(guardado = false) }

    private fun editar(cambio: (Preferencias) -> Preferencias) {
        _estado.update { it.copy(preferencias = cambio(it.preferencias), guardado = false) }
    }
}
