package pl.komjago.ticketapp.controllers.booking.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.math.BigDecimal
import java.time.LocalDateTime

data class MakeReservationOutput(
        val price: BigDecimal,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        val expirationTime: LocalDateTime,
        val ticketIds: List<Int>
)