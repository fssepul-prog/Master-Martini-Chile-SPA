package cl.duoc.mastermartini.ui.components


import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cl.duoc.mastermartini.model.TipoContenido


@Composable
fun ImagenContenido(tipo: TipoContenido, imagen: Int?, modifier: Modifier = Modifier) {
    if (imagen != null) {
        Image(
            painter = painterResource(id = imagen),
            contentDescription = null, // decorativa: el título ya está al lado
            contentScale = ContentScale.Crop,
            modifier = modifier.clip(RoundedCornerShape(12.dp)),
        )
        return
    }
    val icono = when (tipo) {
        TipoContenido.Receta -> Icons.AutoMirrored.Filled.List
        TipoContenido.Video -> Icons.Default.PlayArrow
        TipoContenido.Curso -> Icons.Default.Star
    }
    Box(
        modifier = modifier.clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.secondaryContainer),
        contentAlignment = Alignment.Center,
    ) {
        Icon(icono, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
    }
}
