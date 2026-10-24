package com.proyecto.cocinacapital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.cocinacapital.model.Promocion
import com.proyecto.cocinacapital.repository.PromocionRepository
import com.proyecto.cocinacapital.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InicioViewModel(
    private val promocionRepository: PromocionRepository,
    private val ticketRepository: TicketRepository,
    private val sesionViewModel: SesionViewModel
) : ViewModel() {

    val promociones: StateFlow<List<Promocion>> = promocionRepository.promociones
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    init {
        viewModelScope.launch {
            promocionRepository.sembrarSiVacio()
        }
    }

    fun comprarTicket(promocion: Promocion) {
        val usuario = sesionViewModel.usuarioActual.value ?: return
        viewModelScope.launch {
            ticketRepository.comprarTicket(idUsuario = usuario.id, idPromocion = promocion.id)
            _mensaje.value = "Ticket comprado para ${promocion.comercio}"
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }

    fun cerrarSesion() {
        sesionViewModel.cerrarSesion()
    }
}
