package cl.duoc.mastermartini.navigation

// Guía 10: rutas en una sealed class para no escribir strings sueltos.
sealed class Rutas(val ruta: String) {
    data object Carga : Rutas("carga")
    data object Registro : Rutas("registro")
    data object Inicio : Rutas("inicio")
    data object Contenidos : Rutas("contenidos")
    data object Detalle : Rutas("detalle")
    data object Perfil : Rutas("perfil")
    data object Preferencias : Rutas("preferencias")
}
