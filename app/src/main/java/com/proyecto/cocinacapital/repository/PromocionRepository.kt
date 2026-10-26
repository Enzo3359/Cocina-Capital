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
            condiciones = "Válido de lunes a jueves, no acumulable con otras promociones",
            periodo = "Hasta el 30 de noviembre",
            latitud = -33.0458,
            longitud = -71.6197
        ),
        Promocion(
            comercio = "Sushi Concón",
            beneficio = "30% de descuento en el total de la cuenta",
            condiciones = "Solo almuerzo, válido para mesas de hasta 4 personas",
            periodo = "Hasta el 15 de noviembre",
            latitud = -32.9281,
            longitud = -71.5240
        ),
        Promocion(
            comercio = "Parrilla Valparaíso",
            beneficio = "Postre gratis con cualquier plato principal",
            condiciones = "No válido en días feriados",
            periodo = "Hasta el 20 de noviembre",
            latitud = -33.0472,
            longitud = -71.6128
        )
    )
}
