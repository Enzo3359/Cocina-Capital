package com.proyecto.cocinacapital.view.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import com.proyecto.cocinacapital.navigation.Rutas
import com.proyecto.cocinacapital.viewmodel.RegistroViewModel

@Composable
fun PantallaRegistro(
    navController: NavController,
    viewModel: RegistroViewModel = viewModel()
 ) {
    val estado by viewModel.estado.collectAsState()
    Column {
        Text("Estoy en registro")
        Button(onClick = { navController.navigate(Rutas.INICIO) }) {
            Text("Ir a inicio")
        }
    }
    OutlinedTextField(
        value = estado.nombre,
        onValueChange = {viewModel.onNombreChange(it)},
        label = {Text("Nombre")},
        isError = estado.errores.nombre != null,
        supportingText = {estado.errores.nombre?.let{Text(it)}}
    )
}