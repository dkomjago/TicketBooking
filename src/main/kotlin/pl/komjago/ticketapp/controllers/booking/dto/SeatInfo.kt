package pl.komjago.ticketapp.controllers.booking.dto


data class SeatInfo(
        val seatId: Long,
        val seatRow: Int,
        val seatNumber: Int,
        val booked: Boolean
)