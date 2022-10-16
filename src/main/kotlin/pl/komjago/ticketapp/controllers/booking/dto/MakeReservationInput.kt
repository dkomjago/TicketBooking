package pl.komjago.ticketapp.controllers.booking.dto

import pl.komjago.ticketapp.util.validators.NameConstraint

data class MakeReservationInput(
        val selectedSeats: List<BookedSeatInfo>,
        val screeningId: Int,
        @NameConstraint val name: String
)