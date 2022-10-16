package pl.komjago.ticketapp.controllers.booking.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ScreeningInfo(
        val id: Int,
        val movieTitle: String,
        val seatsLeft: Int,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        val startingTime: LocalDateTime
)