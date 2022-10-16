package pl.komjago.ticketapp.domain.exceptions

class DuplicateSeatException(
    seatId: Int,
    message: String = "Duplicate seatId",
) : InvalidSeatException(seatId, message)