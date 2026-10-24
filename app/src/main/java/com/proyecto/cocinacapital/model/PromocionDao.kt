package com.proyecto.cocinacapital.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PromocionDao {

    @Insert
    suspend fun insertarTodas(promociones: List<Promocion>)

    @Insert
    suspend fun insertar(promocion: Promocion)

    @Update
    suspend fun actualizar(promocion: Promocion)

    @Delete
    suspend fun eliminar(promocion: Promocion)

    @Query("SELECT * FROM promociones")
    fun obtenerTodas(): Flow<List<Promocion>>

    @Query("SELECT COUNT(*) FROM promociones")
    suspend fun contar(): Int
}
