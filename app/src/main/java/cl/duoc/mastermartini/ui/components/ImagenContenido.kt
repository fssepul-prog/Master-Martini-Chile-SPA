package cl.duoc.mastermartini.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.model.TipoContenido

// shortcut: ícono según el tipo en vez de foto, cambiar por imágenes cuando el socio entregue material autorizado.
@Composable
fun ImagenContenido(tipo: TipoContenido, modifier: Modifier = Modifier) {
    val icono = when (tipo) {
        TipoContenido.Receta -> Icons.AutoMirrored.Outlined.MenuBook
        TipoContenido.Video -> Icons.Outlined.PlayCircle
        TipoContenido.Curso -> Icons.Outlined.School
    }
    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
    }
}
