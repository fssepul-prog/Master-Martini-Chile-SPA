package cl.duoc.mastermartini.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import cl.duoc.mastermartini.R
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

// Guía 12: simulación de carga inicial con CircularProgressIndicator.
@Composable
fun CargaScreen(estaRegistrado: suspend () -> Boolean, onListo: (registrado: Boolean) -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onListo(estaRegistrado())
    }
    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //logotipo
        Image(
            painter = painterResource(R.drawable.logo_master_martini),
            contentDescription = "Master Martini",
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
                .width(200.dp),
        )
        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
        Text("Cargando contenidos…", color = MaterialTheme.colorScheme.onPrimary)
    }
}
