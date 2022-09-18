package pl.komjago.ticketapp.domain.exceptions

open class InvalidMovieException(
    val movieId: Int? = null,
    message: String? = null,
    fullMessage: String = "Selected movie is not available${message?.let{ ": $it" } ?: "."}",
) : Exception(fullMessage)