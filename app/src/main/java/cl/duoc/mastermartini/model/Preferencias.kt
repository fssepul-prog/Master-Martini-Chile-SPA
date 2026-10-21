package cl.duoc.mastermartini.model

// Guía 12: lo que se guarda en DataStore.
data class Preferencias(
    val letraGrande: Boolean = false,
    val avisos: Boolean = false,
    val intereses: Set<String> = emptySet(),
)

data class PreferenciasUiState(
    val cargando: Boolean = true,
    val preferencias: Preferencias = Preferencias(),
    val guardado: Boolean = false,
)
