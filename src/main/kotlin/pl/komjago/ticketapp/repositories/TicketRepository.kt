package pl.komjago.ticketapp.repositories

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import pl.komjago.ticketapp.domain.Ticket

@Repository
interface TicketRepository : CoroutineCrudRepository<Ticket, Int> {
    suspend fun countAllByScreeningId(screeningId: Int): Int
    fun findAllByScreeningId(screeningId: Int): Flow<Ticket>
}