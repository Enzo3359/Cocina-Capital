package com.proyecto.cocinacapital.viewmodel

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",
    val cargando: Boolean = false,

    val errores: LoginErrores = LoginErrores()
)
