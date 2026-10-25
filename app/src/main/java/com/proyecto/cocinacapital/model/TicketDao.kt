package com.proyecto.cocinacapital.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDao {

    @Insert
    suspend fun insertar(ticket: Ticket): Long

    @Update
    suspend fun actualizar(ticket: Ticket)

    @Query("SELECT * FROM tickets WHERE idUsuario = :idUsuario ORDER BY fechaCompra DESC")
    fun obtenerPorUsuario(idUsuario: Int): Flow<List<Ticket>>

    @Query("SELECT * FROM tickets WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Ticket?
}
