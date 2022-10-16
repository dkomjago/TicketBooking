package pl.komjago.ticketapp.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import pl.komjago.ticketapp.domain.Seat

@Repository
interface SeatRepository : CoroutineCrudRepository<Seat, Int>