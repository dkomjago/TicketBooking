package pl.komjago.ticketapp.controllers.booking.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.money.Money
import pl.komjago.ticketapp.util.serializers.MoneySerializer
import java.time.LocalDateTime

data class MakeReservationOutput(
        @JsonSerialize(using = MoneySerializer::class)
        val price: Money,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        val expirationTime: LocalDateTime
)