package cl.duoc.mastermartini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.mastermartini.navigation.AppNavigation
import cl.duoc.mastermartini.ui.theme.MasterMartiniTheme
import cl.duoc.mastermartini.ui.utils.anchoVentana
import cl.duoc.mastermartini.viewmodel.PreferenciasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Se crea aquí porque "letra grande" afecta al tema de toda la app.
            val preferenciasViewModel: PreferenciasViewModel = viewModel()
            val letraGrande by preferenciasViewModel.letraGrande.collectAsState()
            MasterMartiniTheme(letraGrande = letraGrande) {
                AppNavigation(anchoVentana = anchoVentana(), preferenciasViewModel = preferenciasViewModel)
            }
        }
    }
}
