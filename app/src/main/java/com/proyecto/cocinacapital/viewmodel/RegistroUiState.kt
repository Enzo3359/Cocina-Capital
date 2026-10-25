package com.proyecto.cocinacapital.viewmodel

data class RegistroUiState(
    val nombre: String = "",
    val correo: String = "",
    val clave : String = "",
    val confirmarClave: String = "",
    val aceptaTerminos: Boolean = false,
    val cargando: Boolean = false,

    val errores: RegistroErrores = RegistroErrores()
)
