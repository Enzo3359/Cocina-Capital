package com.proyecto.cocinacapital.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.proyecto.cocinacapital.model.Promocion
import com.proyecto.cocinacapital.navigation.Rutas
import com.proyecto.cocinacapital.viewmodel.AdminViewModel

@Composable
fun PantallaAdmin(
    navController: NavController,
    viewModel: AdminViewModel
) {
    val promociones by viewModel.promociones.collectAsState()
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Administración de promociones", style = MaterialTheme.typography.headlineMedium)

        Button(
            onClick = { viewModel.abrirFormularioNuevo() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Nueva promoción")
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(promociones) { promocion ->
                TarjetaPromocionAdmin(
                    promocion = promocion,
                    onEditar = { viewModel.abrirFormularioEditar(promocion) },
                    onEliminar = { viewModel.eliminar(promocion) }
                )
            }
        }

        OutlinedButton(
            onClick = {
                viewModel.cerrarSesion()
                navController.navigate(Rutas.LOGIN) {
                    popUpTo(Rutas.ADMIN) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }

    if (estado.mostrandoFormulario) {
        AlertDialog(
            onDismissRequest = { viewModel.cerrarFormulario() },
            title = { Text(if (estado.editandoId == null) "Nueva promoción" else "Editar promoción") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = estado.comercio,
                        onValueChange = viewModel::onComercioChange,
                        label = { Text("Comercio") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = estado.beneficio,
                        onValueChange = viewModel::onBeneficioChange,
                        label = { Text("Beneficio") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = estado.precioTicket,
                        onValueChange = viewModel::onPrecioChange,
                        label = { Text("Precio del ticket") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = estado.condiciones,
                        onValueChange = viewModel::onCondicionesChange,
                        label = { Text("Condiciones") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = estado.periodo,
                        onValueChange = viewModel::onPeriodoChange,
                        label = { Text("Periodo de vigencia") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.guardar() }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cerrarFormulario() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun TarjetaPromocionAdmin(
    promocion: Promocion,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(promocion.comercio, style = MaterialTheme.typography.titleMedium)
            Text(promocion.beneficio, style = MaterialTheme.typography.bodyMedium)
            Text(
                "Ticket: $${promocion.precioTicket}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onEditar) { Text("Editar") }
                TextButton(onClick = onEliminar) { Text("Eliminar") }
            }
        }
    }
}
