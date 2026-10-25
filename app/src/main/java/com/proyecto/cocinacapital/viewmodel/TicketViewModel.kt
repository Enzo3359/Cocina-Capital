package com.proyecto.cocinacapital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.cocinacapital.repository.PromocionRepository
import com.proyecto.cocinacapital.repository.TicketRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class TicketViewModel(
    ticketRepository: TicketRepository,
    promocionRepository: PromocionRepository,
    private val sesionViewModel: SesionViewModel
) : ViewModel() {

    val tickets: StateFlow<List<TicketConPromocion>> = sesionViewModel.usuarioActual
        .flatMapLatest { usuario ->
            if (usuario == null) {
                flowOf(emptyList())
            } else {
                combine(
                    ticketRepository.obtenerTicketsDeUsuario(usuario.id),
                    promocionRepository.promociones
                ) { tickets, promociones ->
                    tickets.map { ticket ->
                        TicketConPromocion(
                            ticket = ticket,
                            promocion = promociones.find { it.id == ticket.idPromocion }
                        )
                    }
                }
            }
        }
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun cerrarSesion() {
        sesionViewModel.cerrarSesion()
    }
}
