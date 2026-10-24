package com.proyecto.cocinacapital.repository

import com.proyecto.cocinacapital.model.Ticket
import com.proyecto.cocinacapital.model.TicketDao
import kotlinx.coroutines.flow.Flow

class TicketRepository(private val dao: TicketDao) {

    suspend fun comprarTicket(idUsuario: Int, idPromocion: Int) {
        dao.insertar(Ticket(idUsuario = idUsuario, idPromocion = idPromocion))
    }

    fun obtenerTicketsDeUsuario(idUsuario: Int): Flow<List<Ticket>> =
        dao.obtenerPorUsuario(idUsuario)
}
