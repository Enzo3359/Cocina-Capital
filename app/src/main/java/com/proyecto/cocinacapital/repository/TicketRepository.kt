package com.proyecto.cocinacapital.repository

import com.proyecto.cocinacapital.model.EstadoTicket
import com.proyecto.cocinacapital.model.Ticket
import com.proyecto.cocinacapital.model.TicketDao
import com.proyecto.cocinacapital.util.generarCodigoTicket
import kotlinx.coroutines.flow.Flow

class TicketRepository(private val dao: TicketDao) {

    suspend fun comprarTicket(idUsuario: Int, idPromocion: Int) {
        dao.insertar(
            Ticket(idUsuario = idUsuario, idPromocion = idPromocion, codigo = generarCodigoUnico())
        )
    }

    private suspend fun generarCodigoUnico(): String {
        var codigo: String
        do {
            codigo = generarCodigoTicket()
        } while (dao.obtenerPorCodigo(codigo) != null)
        return codigo
    }

    fun obtenerTicketsDeUsuario(idUsuario: Int): Flow<List<Ticket>> =
        dao.obtenerPorUsuario(idUsuario)

    suspend fun buscarPorId(id: Int): Ticket? = dao.obtenerPorId(id)

    suspend fun buscarPorCodigo(codigo: String): Ticket? = dao.obtenerPorCodigo(codigo)

    suspend fun yaTieneTicket(idUsuario: Int, idPromocion: Int): Boolean =
        dao.obtenerPorUsuarioYPromocion(idUsuario, idPromocion) != null

    suspend fun marcarUtilizado(ticket: Ticket) {
        dao.actualizar(ticket.copy(estado = EstadoTicket.UTILIZADO))
    }
}
