package com.proyecto.cocinacapital.view.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.proyecto.cocinacapital.navigation.Rutas
import com.proyecto.cocinacapital.util.obtenerUbicacion
import com.proyecto.cocinacapital.view.components.BarraInferiorCliente
import com.proyecto.cocinacapital.viewmodel.InicioViewModel
import com.proyecto.cocinacapital.viewmodel.PromocionConDistancia
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(
    navController: NavController,
    viewModel: InicioViewModel
) {
    val promociones by viewModel.promociones.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val error by viewModel.error.collectAsState()
    val comprandoIds by viewModel.comprandoIds.collectAsState()
    val nombreUsuario by viewModel.nombreUsuario.collectAsState()
    val context = LocalContext.current

    val lanzadorPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val concedido = permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
            permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true
        if (concedido) {
            obtenerUbicacion(context) { lat, lng -> viewModel.actualizarUbicacion(lat, lng) }
        }
    }

    LaunchedEffect(Unit) {
        lanzadorPermiso.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    LaunchedEffect(mensaje) {
        if (mensaje != null) {
            delay(2000)
            viewModel.mensajeMostrado()
        }
    }

    LaunchedEffect(error) {
        if (error != null) {
            delay(2000)
            viewModel.errorMostrado()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (nombreUsuario != null) "Hola, $nombreUsuario" else "Promociones para ti"
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.cerrarSesion()
                            navController.navigate(Rutas.LOGIN) {
                                popUpTo(Rutas.INICIO) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        bottomBar = { BarraInferiorCliente(navController, Rutas.INICIO) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (nombreUsuario != null) {
                Text(
                    "Promociones para ti",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            mensaje?.let {
                Text(it, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium)
            }
            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(promociones) { indice, promocionConDistancia ->
                    TarjetaPromocion(
                        promocionConDistancia,
                        esMasCercana = indice == 0 && promocionConDistancia.distanciaKm != null,
                        comprando = promocionConDistancia.promocion.id in comprandoIds,
                        onComprar = { viewModel.comprarTicket(promocionConDistancia.promocion) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaPromocion(
    promocionConDistancia: PromocionConDistancia,
    esMasCercana: Boolean,
    comprando: Boolean,
    onComprar: () -> Unit
) {
    val promocion = promocionConDistancia.promocion

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(promocion.comercio, style = MaterialTheme.typography.titleMedium)
                if (esMasCercana) {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            "Más cercana",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            promocionConDistancia.distanciaKm?.let {
                Text(
                    "A %.1f km".format(it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(promocion.beneficio, style = MaterialTheme.typography.bodyLarge)
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    "Ticket: $${promocion.precioTicket}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
            Text(
                promocion.condiciones,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                promocion.periodo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Button(
                onClick = onComprar,
                enabled = !comprando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (comprando) "Comprando..." else "Comprar ticket")
            }
        }
    }
}
