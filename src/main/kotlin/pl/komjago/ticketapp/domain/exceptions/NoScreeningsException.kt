package pl.komjago.ticketapp.domain.exceptions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class NoScreeningsException(
    from: LocalDateTime,
    to: LocalDateTime,
    fullMessage: String = "No screenings found in time interval ${from.format(DateTimeFormatter.ISO_DATE)}-${to.format(DateTimeFormatter.ISO_DATE)}."
) : Exception(fullMessage)