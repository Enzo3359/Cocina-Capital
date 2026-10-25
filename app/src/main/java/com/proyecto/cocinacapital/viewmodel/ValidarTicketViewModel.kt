package com.proyecto.cocinacapital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.cocinacapital.model.EstadoTicket
import com.proyecto.cocinacapital.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResultadoValidacion(val mensaje: String, val valido: Boolean)

class ValidarTicketViewModel(
    private val ticketRepository: TicketRepository,
    private val sesionViewModel: SesionViewModel
) : ViewModel() {

    private val _resultado = MutableStateFlow<ResultadoValidacion?>(null)
    val resultado: StateFlow<ResultadoValidacion?> = _resultado.asStateFlow()

    fun validarCodigo(codigo: String) {
        val idTicket = codigo.removePrefix("TICKET:").toIntOrNull()
        if (idTicket == null) {
            _resultado.value = ResultadoValidacion("Código no reconocido", valido = false)
            return
        }

        viewModelScope.launch {
            val ticket = ticketRepository.buscarPorId(idTicket)
            _resultado.value = when {
                ticket == null -> ResultadoValidacion("Ticket no reconocido", valido = false)
                ticket.estado == EstadoTicket.UTILIZADO ->
                    ResultadoValidacion("Ticket ya utilizado", valido = false)
                ticket.estado == EstadoTicket.VENCIDO ->
                    ResultadoValidacion("Ticket vencido", valido = false)
                ticket.estado == EstadoTicket.ANULADO ->
                    ResultadoValidacion("Ticket anulado", valido = false)
                else -> {
                    ticketRepository.marcarUtilizado(ticket)
                    ResultadoValidacion("Ticket válido, aceptado", valido = true)
                }
            }
        }
    }

    fun cerrarSesion() {
        sesionViewModel.cerrarSesion()
    }
}
