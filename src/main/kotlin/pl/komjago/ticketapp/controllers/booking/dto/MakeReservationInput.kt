package pl.komjago.ticketapp.controllers.booking.dto

import pl.komjago.ticketapp.util.validators.NameConstraint
import pl.komjago.ticketapp.util.validators.SurnameConstraint

data class MakeReservationInput(
        val selectedSeats: List<BookedSeatInfo>,
        val screeningId: Long,
        @NameConstraint
        val name: String,
        @SurnameConstraint
        val surname: String
)