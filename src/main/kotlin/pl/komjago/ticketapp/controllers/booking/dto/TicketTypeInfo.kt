package pl.komjago.ticketapp.controllers.booking.dto

import java.math.BigDecimal

data class TicketTypeInfo(
        val id: Int,
        val name: String,
        val price: BigDecimal
)