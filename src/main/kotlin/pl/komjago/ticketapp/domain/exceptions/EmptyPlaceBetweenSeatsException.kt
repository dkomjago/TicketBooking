package pl.komjago.ticketapp.domain.exceptions

class EmptyPlaceBetweenSeatsException(
    seatId: Int,
    message: String = "Empty place left between seats",
) : InvalidSeatException(seatId, message)