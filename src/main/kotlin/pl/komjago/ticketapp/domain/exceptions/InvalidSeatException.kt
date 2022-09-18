package pl.komjago.ticketapp.domain.exceptions

open class InvalidSeatException(
    val seatId: Int? = null,
    message: String? = null,
    fullMessage: String = "Selected seat is not available${message?.let{ ": $it" } ?: "."}",
) : Exception(fullMessage)