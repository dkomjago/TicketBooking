package pl.komjago.ticketapp.controllers.booking.dto

import java.math.BigDecimal

data class TicketTypeInfo(
        val id: Long,
        val name: String,
        val price: BigDecimal
)