package cl.duoc.mastermartini.viewmodel

import cl.duoc.mastermartini.model.UsuarioErrores
import cl.duoc.mastermartini.model.UsuarioUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacionUsuarioTest {
    private val valido = UsuarioUiState(
        nombre = "Rosa Fuentes",
        correo = "rosa.fuentes@correo.cl",
        telefono = "9 1234 5678",
        rutEmpresa = "76.123.456-0",
        tipoNegocio = "Pastelería",
        clave = "masamadre24",
        aceptaTerminos = true,
    )

    @Test
    fun formularioCompletoNoTieneErrores() {
        assertEquals(UsuarioErrores(), validar(valido))
    }

    @Test
    fun cadaCampoInvalidoMarcaSuError() {
        val e = validar(UsuarioUiState())
        listOf(e.nombre, e.correo, e.telefono, e.rutEmpresa, e.tipoNegocio, e.clave, e.terminos).forEach { assertNotNull(it) }
        assertNotNull(validar(valido.copy(correo = "rosa@correo")).correo)
        assertNotNull(validar(valido.copy(telefono = "1234")).telefono)
    }

    @Test
    fun rutConDigitoVerificador() {
        assertTrue(rutValido("76.123.456-0"))
        assertTrue(rutValido("761234560"))
        assertTrue(rutValido("11.111.111-1"))
        assertTrue(rutValido("10.000.013-k")) // K en minúscula
        assertFalse(rutValido("76.123.456-7"))
        assertFalse(rutValido("abc"))
        assertFalse(rutValido(""))
    }
}
