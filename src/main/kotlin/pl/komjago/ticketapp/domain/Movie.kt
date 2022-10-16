package pl.komjago.ticketapp.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table
data class Movie(
        val title: String,
        @Column("movie_cast") val cast: String?,
        val director: String?,
        val description: String?,
        @Column("duration_minutes") val durationInMinutes: Short?,
        @Id val id: Int = 0
)