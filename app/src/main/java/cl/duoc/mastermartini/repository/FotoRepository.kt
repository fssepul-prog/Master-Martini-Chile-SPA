package cl.duoc.mastermartini.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// Guía 13: almacenamiento temporal para la cámara, compartido mediante FileProvider.
class FotoRepository(private val context: Context) {
    // La cámara (TakePicture) escribe la foto en esta Uri.
    fun nuevaUriParaCamara(): Uri {
        val archivo = File(context.cacheDir, "foto_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
    }
}
