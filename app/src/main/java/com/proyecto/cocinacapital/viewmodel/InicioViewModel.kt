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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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
    }.catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _comprandoIds = MutableStateFlow<Set<Int>>(emptySet())
    val comprandoIds: StateFlow<Set<Int>> = _comprandoIds.asStateFlow()

    val nombreUsuario: StateFlow<String?> = sesionViewModel.usuarioActual
        .map { it?.nombre }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = sesionViewModel.usuarioActual.value?.nombre
        )

    init {
        viewModelScope.launch {
            promocionRepository.sembrarSiVacio()
        }
    }

    fun actualizarUbicacion(lat: Double, lng: Double) {
        _ubicacion.value = lat to lng
    }

    fun comprarTicket(promocion: Promocion) {
        if (promocion.id in _comprandoIds.value) return
        val usuario = sesionViewModel.usuarioActual.value ?: return

        _comprandoIds.update { it + promocion.id }
        viewModelScope.launch {
            try {
                if (ticketRepository.yaTieneTicket(usuario.id, promocion.id)) {
                    _error.value = "Ya tenés un ticket de esta promoción"
                    return@launch
                }
                ticketRepository.comprarTicket(idUsuario = usuario.id, idPromocion = promocion.id)
                _mensaje.value = "Ticket adquirido para ${promocion.comercio}"
            } catch (e: Exception) {
                _error.value = "No se pudo comprar el ticket, intentá de nuevo"
            } finally {
                _comprandoIds.update { it - promocion.id }
            }
        }
    }

    fun mensajeMostrado() {
        _mensaje.value = null
    }

    fun errorMostrado() {
        _error.value = null
    }

    fun cerrarSesion() {
        sesionViewModel.cerrarSesion()
    }
}
