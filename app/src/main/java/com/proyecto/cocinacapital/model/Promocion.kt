package com.proyecto.cocinacapital.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "promociones")
data class Promocion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val comercio: String,
    val beneficio: String,
    val precioTicket: Int,
    val condiciones: String,
    val periodo: String
)
