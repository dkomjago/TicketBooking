package pl.komjago.ticketapp.controllers.booking.dto


data class RoomInfo(
        val name: String,
        val seats: List<SeatInfo>,
        val seatCount: Int
)