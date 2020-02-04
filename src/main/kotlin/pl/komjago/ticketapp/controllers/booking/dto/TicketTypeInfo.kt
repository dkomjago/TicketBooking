package pl.komjago.ticketapp.controllers.booking.dto

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.money.Money
import pl.komjago.ticketapp.util.serializers.MoneySerializer

data class TicketTypeInfo(
        val id: Long,
        val name: String,
        @JsonSerialize(using = MoneySerializer::class)
        val price: Money
)