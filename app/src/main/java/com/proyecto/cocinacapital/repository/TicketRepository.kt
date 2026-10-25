package com.proyecto.cocinacapital.repository

import com.proyecto.cocinacapital.model.EstadoTicket
import com.proyecto.cocinacapital.model.Ticket
import com.proyecto.cocinacapital.model.TicketDao
import kotlinx.coroutines.flow.Flow

class TicketRepository(private val dao: TicketDao) {

    suspend fun comprarTicket(idUsuario: Int, idPromocion: Int) {
        dao.insertar(Ticket(idUsuario = idUsuario, idPromocion = idPromocion))
    }

    fun obtenerTicketsDeUsuario(idUsuario: Int): Flow<List<Ticket>> =
        dao.obtenerPorUsuario(idUsuario)

    suspend fun buscarPorId(id: Int): Ticket? = dao.obtenerPorId(id)

    suspend fun marcarUtilizado(ticket: Ticket) {
        dao.actualizar(ticket.copy(estado = EstadoTicket.UTILIZADO))
    }
}
