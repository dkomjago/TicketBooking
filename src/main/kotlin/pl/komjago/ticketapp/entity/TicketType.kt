package pl.komjago.ticketapp.entity

import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id


@Entity
data class TicketType(
        @Id
        val id: Long,

        @Column(nullable = false)
        var name: String,

        @Column
        var price: BigDecimal
)