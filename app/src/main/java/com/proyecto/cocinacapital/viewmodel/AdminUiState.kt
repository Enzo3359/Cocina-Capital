package com.proyecto.cocinacapital.viewmodel

data class AdminUiState(
    val mostrandoFormulario: Boolean = false,
    val editandoId: Int? = null,
    val comercio: String = "",
    val beneficio: String = "",
    val precioTicket: String = "",
    val condiciones: String = "",
    val periodo: String = ""
)
