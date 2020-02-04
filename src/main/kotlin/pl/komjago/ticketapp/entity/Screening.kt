package pl.komjago.ticketapp.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne

@Entity
data class Screening(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @OneToOne
        var movie: Movie,

        @OneToOne
        var room: Room,

        @Column(nullable = false)
        var startingTime: LocalDateTime
)