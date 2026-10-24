package com.proyecto.cocinacapital.model

import androidx.room.Entity
import androidx.room.PrimaryKey

object EstadoTicket {
    const val DISPONIBLE = "disponible"
    const val UTILIZADO = "utilizado"
    const val VENCIDO = "vencido"
    const val ANULADO = "anulado"
}

@Entity(tableName = "tickets")
data class Ticket(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val idUsuario: Int,
    val idPromocion: Int,
    val estado: String = EstadoTicket.DISPONIBLE,
    val fechaCompra: Long = System.currentTimeMillis()
)
