package com.proyecto.cocinacapital.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PromocionDao {

    @Insert
    suspend fun insertarTodas(promociones: List<Promocion>)

    @Query("SELECT * FROM promociones")
    fun obtenerTodas(): Flow<List<Promocion>>

    @Query("SELECT COUNT(*) FROM promociones")
    suspend fun contar(): Int
}
