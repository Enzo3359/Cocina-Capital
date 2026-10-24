package com.proyecto.cocinacapital.repository

import com.proyecto.cocinacapital.model.Promocion
import com.proyecto.cocinacapital.model.PromocionDao
import kotlinx.coroutines.flow.Flow

class PromocionRepository(private val dao: PromocionDao) {

    val promociones: Flow<List<Promocion>> = dao.obtenerTodas()

    suspend fun sembrarSiVacio() {
        if (dao.contar() == 0) {
            dao.insertarTodas(promocionesDeEjemplo())
        }
    }

    suspend fun crear(promocion: Promocion) = dao.insertar(promocion)

    suspend fun actualizar(promocion: Promocion) = dao.actualizar(promocion)

    suspend fun eliminar(promocion: Promocion) = dao.eliminar(promocion)

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
