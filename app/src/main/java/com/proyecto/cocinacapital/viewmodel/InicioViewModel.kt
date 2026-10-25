package com.proyecto.cocinacapital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.cocinacapital.model.Promocion
import com.proyecto.cocinacapital.repository.PromocionRepository
import com.proyecto.cocinacapital.repository.TicketRepository
import com.proyecto.cocinacapital.util.calcularDistanciaKm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class InicioViewModel(
    private val promocionRepository: PromocionRepository,
    private val ticketRepository: TicketRepository,
    private val sesionViewModel: SesionViewModel
) : ViewModel() {

    private val _ubicacion = MutableStateFlow<Pair<Double, Double>?>(null)

    val promociones: StateFlow<List<PromocionConDistancia>> = combine(
        promocionRepository.promociones,
        _ubicacion
    ) { promociones, ubicacion ->
        if (ubicacion == null) {
            promociones.map { PromocionConDistancia(it, null) }
        } else {
            val (lat, lng) = ubicacion
            promociones
                .map {
                    PromocionConDistancia(it, calcularDistanciaKm(lat, lng, it.latitud, it.longitud))
                }
                .sortedBy { it.distanciaKm }
        }
    }.stateIn(
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

    fun actualizarUbicacion(lat: Double, lng: Double) {
        _ubicacion.value = lat to lng
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
