package pl.komjago.ticketapp.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table
data class Screening(
        val roomId: Int,
        val movieId: Int,
        val startingTime: LocalDateTime,
        @Id val id: Int = 0
)