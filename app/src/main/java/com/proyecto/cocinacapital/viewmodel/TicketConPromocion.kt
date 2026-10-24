package com.proyecto.cocinacapital.viewmodel

import com.proyecto.cocinacapital.model.Promocion
import com.proyecto.cocinacapital.model.Ticket

data class TicketConPromocion(
    val ticket: Ticket,
    val promocion: Promocion?
)
