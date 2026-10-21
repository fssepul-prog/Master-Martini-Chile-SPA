package cl.duoc.mastermartini.ui.components

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

// Guía 13: foto de perfil circular; sin foto muestra un ícono por defecto.
@Composable
fun ImagenInteligente(uri: Uri?, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    // Lee la imagen de la Uri; si no se puede leer, se muestra el ícono.
    // shortcut: no corrige la rotación EXIF de algunas cámaras, usar una librería de imágenes si se ve girada.
    val imagen = remember(uri) {
        uri?.let {
            runCatching {
                context.contentResolver.openInputStream(it)?.use { entrada ->
                    BitmapFactory.decodeStream(entrada, null, BitmapFactory.Options().apply { inSampleSize = 2 })
                }
            }.getOrNull()?.asImageBitmap()
        }
    }
    val forma = modifier
        .size(140.dp)
        .clip(CircleShape)
        .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)

    if (imagen != null) {
        Image(bitmap = imagen, contentDescription = "Foto de perfil", contentScale = ContentScale.Crop, modifier = forma)
    } else {
        Box(forma.background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Person, contentDescription = "Sin foto de perfil", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(72.dp))
        }
    }
}
