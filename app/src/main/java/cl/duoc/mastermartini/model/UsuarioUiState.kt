package cl.duoc.mastermartini.model

// Guía 11: estado del formulario de registro y sus errores por campo.
data class UsuarioUiState(
    val nombre: String = "",
    val correo: String = "",
    val telefono: String = "",
    val rutEmpresa: String = "",
    val tipoNegocio: String = "",
    val clave: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: UsuarioErrores = UsuarioErrores(),
)

data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val telefono: String? = null,
    val rutEmpresa: String? = null,
    val tipoNegocio: String? = null,
    val clave: String? = null,
    val terminos: String? = null,
)
