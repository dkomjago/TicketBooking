package pl.komjago.ticketapp.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne

@Entity
data class Reservation(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @Column(nullable = false)
        var buyerName: String,

        @Column(nullable = false)
        var buyerSurname: String,

        @OneToOne
        var screening: Screening,

        @OneToMany
        var tickets: List<Ticket>,

        @Column(nullable = false)
        var expirationTime: LocalDateTime
)