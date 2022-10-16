package pl.komjago.ticketapp.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table
data class Seat(
        val roomId: Int,
        @Column("seat_row") val row: Char,
        @Column("seat_number") val number: Int,
        @Id val id: Int = 0
)