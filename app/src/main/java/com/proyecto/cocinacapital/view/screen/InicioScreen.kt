package com.proyecto.cocinacapital.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController


@Composable
fun PantallaInicio(navController: NavController) {
    Column {
        Text("Estoy en la pantalla inicio")
        Button(onClick = { navController.popBackStack() }) {
            Text("Volver")
        }
    }
}