package cl.duoc.mastermartini.viewmodel

import cl.duoc.mastermartini.model.UsuarioErrores
import cl.duoc.mastermartini.model.UsuarioUiState
import cl.duoc.mastermartini.repository.UsuarioRepository

// Reglas de validación, separadas de la UI y sin dependencias de Android para poder probarlas.
fun validar(u: UsuarioUiState) = UsuarioErrores(
    nombre = if (u.nombre.trim().length < 3) "Ingresa tu nombre completo" else null,
    correo = if (!Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matches(u.correo.trim())) "Ingresa un correo válido, ej: nombre@correo.cl" else null,
    telefono = if (u.telefono.count(Char::isDigit) != 9) "Ingresa 9 dígitos, ej: 9 1234 5678" else null,
    rutEmpresa = if (!rutValido(u.rutEmpresa)) "RUT inválido, ej: 76.123.456-0" else null,
    tipoNegocio = if (u.tipoNegocio !in UsuarioRepository.tiposDeNegocio) "Elige el tipo de negocio" else null,
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
