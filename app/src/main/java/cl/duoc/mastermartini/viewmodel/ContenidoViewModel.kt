package cl.duoc.mastermartini.viewmodel

import androidx.lifecycle.ViewModel
import cl.duoc.mastermartini.model.Contenido
import cl.duoc.mastermartini.model.ContenidoUiState
import cl.duoc.mastermartini.model.TipoContenido
import cl.duoc.mastermartini.repository.ContenidoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// Compartido por Inicio y Contenidos: la UI solo observa el estado y llama funciones.
class ContenidoViewModel : ViewModel() {
    private val todos = ContenidoRepository.obtenerContenidos()

    private val _estado = MutableStateFlow(ContenidoUiState(lista = todos))
    val estado: StateFlow<ContenidoUiState> = _estado

    val lineasDeNegocio = ContenidoRepository.lineasDeNegocio
    val destacado: Contenido = todos.first { it.tipo == TipoContenido.Curso }
    val recetasRecientes: List<Contenido> = todos.filter { it.tipo == TipoContenido.Receta }

    fun filtrar(tipo: TipoContenido?) {
        _estado.update { it.copy(filtro = tipo, lista = if (tipo == null) todos else todos.filter { c -> c.tipo == tipo }) }
    }

    fun seleccionar(contenido: Contenido?) {
        _estado.update { it.copy(seleccionado = contenido) }
    }
}
