package com.proyecto.cocinacapital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.cocinacapital.model.Promocion
import com.proyecto.cocinacapital.repository.PromocionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: PromocionRepository,
    private val sesionViewModel: SesionViewModel
) : ViewModel() {

    val promociones: StateFlow<List<Promocion>> = repository.promociones
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _estado = MutableStateFlow(AdminUiState())
    val estado: StateFlow<AdminUiState> = _estado.asStateFlow()

    fun abrirFormularioNuevo() {
        _estado.value = AdminUiState(mostrandoFormulario = true)
    }

    fun abrirFormularioEditar(promocion: Promocion) {
        _estado.value = AdminUiState(
            mostrandoFormulario = true,
            editandoId = promocion.id,
            comercio = promocion.comercio,
            beneficio = promocion.beneficio,
            precioTicket = promocion.precioTicket.toString(),
            condiciones = promocion.condiciones,
            periodo = promocion.periodo
        )
    }

    fun cerrarFormulario() {
        _estado.value = AdminUiState()
    }

    fun onComercioChange(valor: String) = _estado.update { it.copy(comercio = valor) }
    fun onBeneficioChange(valor: String) = _estado.update { it.copy(beneficio = valor) }
    fun onPrecioChange(valor: String) = _estado.update { it.copy(precioTicket = valor) }
    fun onCondicionesChange(valor: String) = _estado.update { it.copy(condiciones = valor) }
    fun onPeriodoChange(valor: String) = _estado.update { it.copy(periodo = valor) }

    fun guardar() {
        val actual = _estado.value
        if (actual.comercio.isBlank() || actual.beneficio.isBlank()) return

        val promocion = Promocion(
            id = actual.editandoId ?: 0,
            comercio = actual.comercio,
            beneficio = actual.beneficio,
            precioTicket = actual.precioTicket.toIntOrNull() ?: 0,
            condiciones = actual.condiciones,
            periodo = actual.periodo
        )

        viewModelScope.launch {
            if (actual.editandoId == null) {
                repository.crear(promocion)
            } else {
                repository.actualizar(promocion)
            }
            cerrarFormulario()
        }
    }

    fun eliminar(promocion: Promocion) {
        viewModelScope.launch {
            repository.eliminar(promocion)
        }
    }

    fun cerrarSesion() {
        sesionViewModel.cerrarSesion()
    }
}
