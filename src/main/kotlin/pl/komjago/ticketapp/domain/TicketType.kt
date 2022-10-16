package pl.komjago.ticketapp.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal

@Table
data class TicketType(
        @Column("type_name") val type: String,
        val price: BigDecimal,
        @Id val id: Int = 0
)