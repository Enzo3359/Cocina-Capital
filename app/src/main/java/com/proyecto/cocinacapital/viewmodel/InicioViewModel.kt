package com.proyecto.cocinacapital.viewmodel

import androidx.lifecycle.ViewModel
import com.proyecto.cocinacapital.model.Promocion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InicioViewModel : ViewModel() {
    private val _promociones = MutableStateFlow(promocionesDeEjemplo())
    val promociones: StateFlow<List<Promocion>> = _promociones.asStateFlow()

    private fun promocionesDeEjemplo(): List<Promocion> = listOf(
        Promocion(
            comercio = "Café del Puerto",
            beneficio = "2x1 en café de especialidad",
            precioTicket = 2000,
            condiciones = "Válido de lunes a jueves, no acumulable con otras promociones",
            periodo = "Hasta el 30 de noviembre"
        ),
        Promocion(
            comercio = "Sushi Concón",
            beneficio = "30% de descuento en el total de la cuenta",
            precioTicket = 3500,
            condiciones = "Solo almuerzo, válido para mesas de hasta 4 personas",
            periodo = "Hasta el 15 de noviembre"
        ),
        Promocion(
            comercio = "Parrilla Valparaíso",
            beneficio = "Postre gratis con cualquier plato principal",
            precioTicket = 1500,
            condiciones = "No válido en días feriados",
            periodo = "Hasta el 20 de noviembre"
        )
    )
}
