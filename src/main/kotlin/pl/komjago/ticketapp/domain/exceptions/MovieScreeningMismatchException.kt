package pl.komjago.ticketapp.domain.exceptions

open class MovieScreeningMismatchException(
    screeningId: Int? = null,
    message: String = "No movie assigned to the screening",
) : InvalidScreeningException(screeningId, message)