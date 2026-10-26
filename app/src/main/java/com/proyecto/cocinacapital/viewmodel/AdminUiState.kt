package com.proyecto.cocinacapital.viewmodel

data class AdminUiState(
    val mostrandoFormulario: Boolean = false,
    val editandoId: Int? = null,
    val comercio: String = "",
    val beneficio: String = "",
    val condiciones: String = "",
    val periodo: String = "",
    val cargando: Boolean = false,
    val error: String? = null,
    val errores: AdminErrores = AdminErrores()
)
