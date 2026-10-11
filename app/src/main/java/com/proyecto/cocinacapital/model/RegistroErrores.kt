package com.proyecto.cocinacapital.model

data class RegistroErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val confirmacion: String? = null,
    val aceptaTerminos: Boolean? = null,

)