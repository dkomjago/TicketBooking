package pl.komjago.ticketapp.controllers.booking.dto

data class MovieInfo(
        val title: String,
        val cast: String?,
        val director: String?,
        val description: String?,
        val durationInMinutes: Short?
)