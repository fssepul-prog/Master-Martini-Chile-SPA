package cl.duoc.mastermartini.model

enum class TipoContenido(val plural: String) { Receta("Recetas"), Video("Videos"), Curso("Cursos") }

data class Contenido(
    val id: Int,
    val tipo: TipoContenido,
    val titulo: String,
    val detalle: String,
    val descripcion: String,
    val imagen: Int? = null, // asignacion para el casillero de imagenes, en caso de que un apartado no tenga una asignada, esta quedara con una alternativa (sin implementar)
)

// Estado de la pantalla de contenidos: filtro activo, lista filtrada y elemento abierto.
data class ContenidoUiState(
    val filtro: TipoContenido? = null,
    val lista: List<Contenido> = emptyList(),
    val seleccionado: Contenido? = null,
)
