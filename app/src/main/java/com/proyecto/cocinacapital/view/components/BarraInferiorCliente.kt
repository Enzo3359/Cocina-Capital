package com.proyecto.cocinacapital.view.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.proyecto.cocinacapital.navigation.Rutas

@Composable
fun BarraInferiorCliente(navController: NavController, rutaActual: String) {
    NavigationBar {
        NavigationBarItem(
            selected = rutaActual == Rutas.INICIO,
            onClick = {
                if (rutaActual != Rutas.INICIO) {
                    navController.navigate(Rutas.INICIO) {
                        popUpTo(Rutas.INICIO) { inclusive = true }
                    }
                }
            },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") }
        )
        NavigationBarItem(
            selected = rutaActual == Rutas.MIS_TICKETS,
            onClick = {
                if (rutaActual != Rutas.MIS_TICKETS) {
                    navController.navigate(Rutas.MIS_TICKETS)
                }
            },
            icon = { Icon(Icons.Filled.ConfirmationNumber, contentDescription = "Mis tickets") },
            label = { Text("Mis tickets") }
        )
    }
}
