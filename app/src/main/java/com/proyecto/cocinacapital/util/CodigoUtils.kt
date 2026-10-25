package com.proyecto.cocinacapital.util

private const val CARACTERES_CODIGO = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"

fun generarCodigoTicket(): String =
    (1..6).map { CARACTERES_CODIGO.random() }.joinToString("")
