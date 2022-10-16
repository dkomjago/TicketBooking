package pl.komjago.ticketapp.controllers.booking.dto


data class SeatInfo(
        val seatId: Int,
        val seatRow: Char,
        val seatNumber: Int,
        val booked: Boolean
)