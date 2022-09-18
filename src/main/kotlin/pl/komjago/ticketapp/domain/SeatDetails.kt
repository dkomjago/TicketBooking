package pl.komjago.ticketapp.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import pl.komjago.ticketapp.domain.enums.SeatType
import java.math.BigDecimal

@Table
data class SeatDetails(
        val seatId: Int,
        val seatType: SeatType,
        val costMultiplier: BigDecimal,
        val isAvailable: Boolean,
        @Id val id: Int = 0
)