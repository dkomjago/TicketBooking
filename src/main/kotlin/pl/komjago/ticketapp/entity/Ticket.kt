package pl.komjago.ticketapp.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne


@Entity
data class Ticket(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @OneToOne
        var screening: Screening,

        @OneToOne
        var seat: Seat,

        @OneToOne
        var ticketType: TicketType
)