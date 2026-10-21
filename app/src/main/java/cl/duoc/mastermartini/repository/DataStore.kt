package cl.duoc.mastermartini.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Una sola instancia por archivo: DataStore falla si se crean dos para el mismo nombre.
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mastermartini")
