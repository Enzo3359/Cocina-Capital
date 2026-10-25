package com.proyecto.cocinacapital.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.Image
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.proyecto.cocinacapital.model.EstadoTicket
import com.proyecto.cocinacapital.util.generarQr
import com.proyecto.cocinacapital.viewmodel.TicketConPromocion
import com.proyecto.cocinacapital.viewmodel.TicketViewModel

@Composable
fun PantallaMisTickets(
    navController: NavController,
    viewModel: TicketViewModel
) {
    val tickets by viewModel.tickets.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Mis tickets", style = MaterialTheme.typography.headlineMedium)

        if (tickets.isEmpty()) {
            Text(
                "Todavía no compraste ningún ticket.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}

@Composable
private fun TarjetaTicket(ticketConPromocion: TicketConPromocion) {
    val promocion = ticketConPromocion.promocion
    val ticket = ticketConPromocion.ticket
    val qr = remember(ticket.id) { generarQr("TICKET:${ticket.id}") }

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
            }
        }
    }
}
