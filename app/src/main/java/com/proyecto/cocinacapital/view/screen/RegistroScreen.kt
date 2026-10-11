package com.proyecto.cocinacapital.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.proyecto.cocinacapital.navigation.Rutas

@Composable
fun PantallaRegistro(navController: NavController) {
    Column {
        Text("Estoy en registro")
        Button(onClick = { navController.navigate(Rutas.INICIO) }) {
            Text("Ir a inicio")
        }
    }
}