package com.proyecto.cocinacapital.view.screen

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.proyecto.cocinacapital.navigation.Rutas
import com.proyecto.cocinacapital.util.obtenerUbicacion
import com.proyecto.cocinacapital.viewmodel.InicioViewModel
import com.proyecto.cocinacapital.viewmodel.PromocionConDistancia
import kotlinx.coroutines.delay

@Composable
fun PantallaInicio(
    navController: NavController,
    viewModel: InicioViewModel
) {
    val promociones by viewModel.promociones.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Promociones para ti", style = MaterialTheme.typography.headlineMedium)

        mensaje?.let {
            Text(it, color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.bodyMedium)
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(promociones) { promocionConDistancia ->
                TarjetaPromocion(
                    promocionConDistancia,
                    onComprar = { viewModel.comprarTicket(promocionConDistancia.promocion) }
                )
            }
        }

        OutlinedButton(
            onClick = { navController.navigate(Rutas.MIS_TICKETS) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mis tickets")
        }

        OutlinedButton(
            onClick = {
                viewModel.cerrarSesion()
                navController.navigate(Rutas.LOGIN) {
                    popUpTo(Rutas.INICIO) { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun TarjetaPromocion(promocionConDistancia: PromocionConDistancia, onComprar: () -> Unit) {
    val promocion = promocionConDistancia.promocion

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(promocion.comercio, style = MaterialTheme.typography.titleMedium)
            promocionConDistancia.distanciaKm?.let {
                Text(
                    "A %.1f km".format(it),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(promocion.beneficio, style = MaterialTheme.typography.bodyLarge)
            Text(
                "Ticket: $${promocion.precioTicket}",
                style = MaterialTheme.typography.bodyMedium
            )
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
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comprar ticket")
            }
        }
    }
}
