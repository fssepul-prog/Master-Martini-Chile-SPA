package cl.duoc.mastermartini.ui.utils

import android.app.Activity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

// Guía 9: punto único para saber si la pantalla es Compact, Medium o Expanded.
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun Activity.anchoVentana(): WindowWidthSizeClass = calculateWindowSizeClass(this).widthSizeClass
