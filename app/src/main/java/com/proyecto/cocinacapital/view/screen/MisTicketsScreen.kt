package com.proyecto.cocinacapital.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.proyecto.cocinacapital.model.EstadoTicket
import com.proyecto.cocinacapital.navigation.Rutas
import com.proyecto.cocinacapital.util.generarQr
import com.proyecto.cocinacapital.view.components.BarraInferiorCliente
import com.proyecto.cocinacapital.viewmodel.TicketConPromocion
import com.proyecto.cocinacapital.viewmodel.TicketViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMisTickets(
    navController: NavController,
    viewModel: TicketViewModel
) {
    val tickets by viewModel.tickets.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis tickets") },
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
        bottomBar = { BarraInferiorCliente(navController, Rutas.MIS_TICKETS) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (tickets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.ConfirmationNumber,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            "Todavía no compraste ningún ticket",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Las promociones que compres van a aparecer acá",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tickets) { ticketConPromocion ->
                        TarjetaTicket(ticketConPromocion)
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaTicket(ticketConPromocion: TicketConPromocion) {
    val promocion = ticketConPromocion.promocion
    val ticket = ticketConPromocion.ticket
    val qr = remember(ticket.codigo) { generarQr(ticket.codigo) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                promocion?.comercio ?: "Promoción no disponible",
                style = MaterialTheme.typography.titleMedium
            )
            promocion?.let {
                Text(it.beneficio, style = MaterialTheme.typography.bodyMedium)
            }
            Text(
                "Estado: ${ticket.estado}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (ticket.estado == EstadoTicket.DISPONIBLE) {
                Image(
                    bitmap = qr.asImageBitmap(),
                    contentDescription = "Código QR del ticket",
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                )
                Text(
                    "También podés usar este código:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                SelectionContainer {
                    Text(
                        ticket.codigo,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
