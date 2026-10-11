package com.proyecto.cocinacapital.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.proyecto.cocinacapital.navigation.Rutas

@Composable
fun PantallaLogin(navController: NavController) {
    Column {
        Text("Estoy en el login")
        Button(onClick = { navController.navigate(Rutas.INICIO) }) {
            Text("Ir a inicio")
        }
        Button(onClick = {navController.navigate(Rutas.REGISTRO) } ) {
            Text("yendo a registro")
        }
    }
}