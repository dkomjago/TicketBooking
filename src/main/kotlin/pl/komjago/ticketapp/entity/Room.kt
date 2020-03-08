package pl.komjago.ticketapp.entity

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany


@Entity
data class Room(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @Column(nullable = false)
        var name: String,

        @OneToMany(cascade = [CascadeType.ALL])
        var seats: List<Seat>,

        @Column(nullable = false)
        var seatCount: Int
)