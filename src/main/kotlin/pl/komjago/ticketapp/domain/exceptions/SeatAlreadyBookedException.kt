package pl.komjago.ticketapp.domain.exceptions

class SeatAlreadyBookedException(
    seatId: Int,
    message: String = "Seat already booked",
) : InvalidSeatException(seatId, message)