package pl.komjago.ticketapp.domain.exceptions

open class InvalidRoomException(
    val roomId: Int? = null,
    message: String? = null,
    fullMessage: String = "Selected room is not available${message?.let{ ": $it" } ?: "."}",
) : Exception(fullMessage)