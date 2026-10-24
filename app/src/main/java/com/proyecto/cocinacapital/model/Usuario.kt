package com.proyecto.cocinacapital.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

object RolUsuario {
    const val CLIENTE = "cliente"
    const val PERSONAL = "personal"
    const val ADMINISTRADOR = "administrador"
}

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["correo"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: String = RolUsuario.CLIENTE
)
