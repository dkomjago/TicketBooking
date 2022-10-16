package pl.komjago.ticketapp.domain.exceptions

open class InvalidTicketTypeException(
    val ticketType: Int? = null,
    message: String? = null,
    fullMessage: String = "Selected ticket type is not available${message?.let{ ": $it" } ?: "."}",
) : Exception(fullMessage)