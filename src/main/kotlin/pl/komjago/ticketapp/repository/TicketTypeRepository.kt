package pl.komjago.ticketapp.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.komjago.ticketapp.entity.TicketType

@Repository
interface TicketTypeRepository : JpaRepository<TicketType, Long>