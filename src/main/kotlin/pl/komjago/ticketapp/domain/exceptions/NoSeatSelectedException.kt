package pl.komjago.ticketapp.domain.exceptions

class NoSeatSelectedException(
    message: String = "No seat selected"
) : InvalidSeatException(null, message)