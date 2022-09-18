package pl.komjago.ticketapp.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import pl.komjago.ticketapp.domain.enums.RoomType

@Table
data class Room(
        @Column("room_type") val type: RoomType,
        @Column("room_name") val name: String,
        @Id val id: Int = 0
)