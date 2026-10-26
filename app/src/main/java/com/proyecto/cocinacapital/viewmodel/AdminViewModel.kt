package com.proyecto.cocinacapital.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyecto.cocinacapital.model.Promocion
import com.proyecto.cocinacapital.repository.PromocionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: PromocionRepository,
    private val sesionViewModel: SesionViewModel
) : ViewModel() {

    val promociones: StateFlow<List<Promocion>> = repository.promociones
        .catch { emit(emptyList()) }
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
            condiciones = promocion.condiciones,
            periodo = promocion.periodo
        )
    }

    fun cerrarFormulario() {
        _estado.value = AdminUiState()
    }

    fun onComercioChange(valor: String) =
        _estado.update { it.copy(comercio = valor, errores = it.errores.copy(comercio = null)) }

    fun onBeneficioChange(valor: String) =
        _estado.update { it.copy(beneficio = valor, errores = it.errores.copy(beneficio = null)) }

    fun onCondicionesChange(valor: String) = _estado.update { it.copy(condiciones = valor) }
    fun onPeriodoChange(valor: String) = _estado.update { it.copy(periodo = valor) }

    private fun validarComercio(comercio: String): String? {
        if (comercio.isBlank()) return "El comercio no puede estar vacío"
        return null
    }

    private fun validarBeneficio(beneficio: String): String? {
        if (beneficio.isBlank()) return "El beneficio no puede estar vacío"
        return null
    }

    fun guardar() {
        val actual = _estado.value
        if (actual.cargando) return

        val errores = AdminErrores(
            comercio = validarComercio(actual.comercio),
            beneficio = validarBeneficio(actual.beneficio)
        )
        _estado.update { it.copy(errores = errores) }
        if (errores != AdminErrores()) return

        val promocion = Promocion(
            id = actual.editandoId ?: 0,
            comercio = actual.comercio,
            beneficio = actual.beneficio,
            condiciones = actual.condiciones,
            periodo = actual.periodo
        )

        viewModelScope.launch {
            _estado.update { it.copy(cargando = true) }
            try {
                if (actual.editandoId == null) {
                    repository.crear(promocion)
                } else {
                    repository.actualizar(promocion)
                }
                cerrarFormulario()
            } catch (e: Exception) {
                _estado.update { it.copy(cargando = false, error = "No se pudo guardar la promoción, intentá de nuevo") }
            }
        }
    }

    fun eliminar(promocion: Promocion) {
        viewModelScope.launch {
            try {
                repository.eliminar(promocion)
            } catch (e: Exception) {
                _estado.update { it.copy(error = "No se pudo eliminar la promoción, intentá de nuevo") }
            }
        }
    }

    fun cerrarSesion() {
        sesionViewModel.cerrarSesion()
    }
}
