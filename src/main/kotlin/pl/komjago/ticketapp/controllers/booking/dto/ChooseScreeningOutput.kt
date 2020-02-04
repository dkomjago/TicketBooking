package pl.komjago.ticketapp.controllers.booking.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ChooseScreeningOutput(
        val id: Long,
        val movie: MovieInfo,
        val room: RoomInfo,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        val startingTime: LocalDateTime,
        val ticketTypes: List<TicketTypeInfo>
)