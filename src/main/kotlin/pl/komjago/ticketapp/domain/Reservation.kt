package pl.komjago.ticketapp.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table
data class Reservation(
        val buyerName: String,
        val expirationTime: LocalDateTime,
        @Id val id: Int = 0
)