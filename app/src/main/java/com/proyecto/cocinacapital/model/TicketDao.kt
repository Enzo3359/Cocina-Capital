package com.proyecto.cocinacapital.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Insert
    suspend fun insertar(ticket: Ticket): Long

    @Query("SELECT * FROM tickets WHERE idUsuario = :idUsuario ORDER BY fechaCompra DESC")
    fun obtenerPorUsuario(idUsuario: Int): Flow<List<Ticket>>
}
