package cl.duoc.mastermartini.model

enum class TipoContenido(val plural: String) { Receta("Recetas"), Video("Videos"), Curso("Cursos") }

data class Contenido(
    val id: Int,
    val tipo: TipoContenido,
    val titulo: String,
    val detalle: String,
    val descripcion: String,
)

// Estado de la pantalla de contenidos: filtro activo, lista filtrada y elemento abierto.
data class ContenidoUiState(
    val filtro: TipoContenido? = null,
    val lista: List<Contenido> = emptyList(),
    val seleccionado: Contenido? = null,
)
