package cl.duoc.mastermartini.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import cl.duoc.mastermartini.model.Preferencias
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Guía 12: preferencias persistentes con DataStore Preferences.
class PreferenciasRepository(private val context: Context) {
    private object Claves {
        val LETRA_GRANDE = booleanPreferencesKey("letra_grande")
        val AVISOS = booleanPreferencesKey("avisos")
        val INTERESES = stringSetPreferencesKey("intereses")
    }

    val preferencias: Flow<Preferencias> = context.dataStore.data.map { p ->
        Preferencias(
            letraGrande = p[Claves.LETRA_GRANDE] ?: false,
            avisos = p[Claves.AVISOS] ?: false,
            intereses = p[Claves.INTERESES] ?: emptySet(),
        )
    }

    suspend fun guardar(pref: Preferencias) {
        context.dataStore.edit { p ->
            p[Claves.LETRA_GRANDE] = pref.letraGrande
            p[Claves.AVISOS] = pref.avisos
            p[Claves.INTERESES] = pref.intereses
        }
    }

    companion object {
        // Temas del caso Master Martini (encuesta de intereses).
        val temasDeInteres = listOf(
            "Chocolatería", "Pastelería de vitrina", "Heladería", "Bollería",
            "Masas de hoja", "Galletas", "Empanadas", "Productos de horneo",
        )
    }
}
