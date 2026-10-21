package cl.duoc.mastermartini.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cl.duoc.mastermartini.model.UsuarioUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Persistencia local del usuario registrado. La contraseña NO se guarda.
class UsuarioRepository(private val context: Context) {
    private object Claves {
        val NOMBRE = stringPreferencesKey("usuario_nombre")
        val CORREO = stringPreferencesKey("usuario_correo")
        val TELEFONO = stringPreferencesKey("usuario_telefono")
        val RUT = stringPreferencesKey("usuario_rut")
        val TIPO = stringPreferencesKey("usuario_tipo_negocio")
    }

    // null = todavía no hay nadie registrado en este teléfono.
    val usuario: Flow<UsuarioUiState?> = context.dataStore.data.map { p ->
        p[Claves.NOMBRE]?.let { nombre ->
            UsuarioUiState(
                nombre = nombre,
                correo = p[Claves.CORREO].orEmpty(),
                telefono = p[Claves.TELEFONO].orEmpty(),
                rutEmpresa = p[Claves.RUT].orEmpty(),
                tipoNegocio = p[Claves.TIPO].orEmpty(),
                aceptaTerminos = true,
            )
        }
    }

    suspend fun guardar(u: UsuarioUiState) {
        context.dataStore.edit { p ->
            p[Claves.NOMBRE] = u.nombre.trim()
            p[Claves.CORREO] = u.correo.trim()
            p[Claves.TELEFONO] = u.telefono.trim()
            p[Claves.RUT] = u.rutEmpresa.trim()
            p[Claves.TIPO] = u.tipoNegocio
        }
    }
}
