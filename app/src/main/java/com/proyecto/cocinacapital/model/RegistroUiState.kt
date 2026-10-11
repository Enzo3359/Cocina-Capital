package com.proyecto.cocinacapital.model

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave : String = "",
    val confirmarClave: String = "",
    val aceptaTerminos: Boolean = false,

    val errores: RegistroErrores = RegistroErrores()
)