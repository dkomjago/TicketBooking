package pl.komjago.ticketapp.repositories

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import pl.komjago.ticketapp.domain.SeatDetails

@Repository
interface SeatDetailsRepository : CoroutineCrudRepository<SeatDetails, Int>