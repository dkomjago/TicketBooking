package pl.komjago.ticketapp.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.komjago.ticketapp.entity.Ticket

@Repository
interface TicketRepository : JpaRepository<Ticket, Long?>{
    fun countAllByScreeningId(screeningId: Long): Int
    fun findAllByScreeningId(screeningId: Long): List<Ticket>
}