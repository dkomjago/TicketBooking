package pl.komjago.ticketapp.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table
data class Ticket(
        val reservationId: Int,
        val screeningId: Int,
        val seatId: Int,
        val ticketTypeId: Int,
        @Id val id: Int = 0
)