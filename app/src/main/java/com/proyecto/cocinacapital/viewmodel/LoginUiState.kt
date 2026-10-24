package com.proyecto.cocinacapital.viewmodel

data class LoginUiState(
    val correo: String = "",
    val clave: String = "",

    val errores: LoginErrores = LoginErrores()
)
