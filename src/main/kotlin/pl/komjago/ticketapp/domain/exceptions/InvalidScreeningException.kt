package pl.komjago.ticketapp.domain.exceptions

open class InvalidScreeningException(
    val screeningId: Int? = null,
    message: String? = null,
    fullMessage: String = "Selected screening is not available${message?.let{ ": $it" } ?: "."}",
) : Exception(fullMessage)