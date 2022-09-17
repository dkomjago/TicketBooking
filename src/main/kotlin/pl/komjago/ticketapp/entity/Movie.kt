package pl.komjago.ticketapp.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id


@Entity
data class Movie(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @Column(nullable = false)
        var title: String,

        @Column(name="movie_cast", nullable = false)
        var cast: String,

        @Column(nullable = false)
        var director: String,

        @Column(nullable = false)
        var description: String,

        @Column(name = "duration_minutes", nullable = false)
        var durationInMinutes: Long
)