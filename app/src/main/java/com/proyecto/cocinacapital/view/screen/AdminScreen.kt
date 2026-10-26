package com.proyecto.cocinacapital.view.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.proyecto.cocinacapital.model.Promocion
import com.proyecto.cocinacapital.navigation.Rutas
import com.proyecto.cocinacapital.viewmodel.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAdmin(
    navController: NavController,
    viewModel: AdminViewModel
) {
    val promociones by viewModel.promociones.collectAsState()
    val estado by viewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Administración de promociones") },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.cerrarSesion()
                            navController.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.ADMIN) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.abrirFormularioNuevo() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Nueva promoción")
            }

            if (!estado.mostrandoFormulario) {
                estado.error?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(promociones, key = { it.id }) { promocion ->
                    TarjetaPromocionAdmin(
                        promocion = promocion,
                        onEditar = { viewModel.abrirFormularioEditar(promocion) },
                        onEliminar = { viewModel.eliminar(promocion) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
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
                        isError = estado.errores.comercio != null,
                        supportingText = {
                            estado.errores.comercio?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = estado.beneficio,
                        onValueChange = viewModel::onBeneficioChange,
                        label = { Text("Beneficio") },
                        isError = estado.errores.beneficio != null,
                        supportingText = {
                            estado.errores.beneficio?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                        },
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
                    estado.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.guardar() }, enabled = !estado.cargando) {
                    Crossfade(targetState = estado.cargando, label = "textoGuardar") { cargando ->
                        Text(if (cargando) "Guardando..." else "Guardar")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.cerrarFormulario() }, enabled = !estado.cargando) {
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
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(promocion.comercio, style = MaterialTheme.typography.titleMedium)
            Text(promocion.beneficio, style = MaterialTheme.typography.bodyMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onEditar) { Text("Editar") }
                TextButton(onClick = onEliminar) { Text("Eliminar") }
            }
        }
    }
}
