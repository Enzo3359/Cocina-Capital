package com.proyecto.cocinacapital.viewmodel

import com.proyecto.cocinacapital.model.Promocion

data class PromocionConDistancia(
    val promocion: Promocion,
    val distanciaKm: Double?
)
