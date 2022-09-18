package pl.komjago.ticketapp.repositories

import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import pl.komjago.ticketapp.domain.Screening
import java.time.LocalDateTime

@Repository
interface ScreeningRepository : CoroutineCrudRepository<Screening, Int> {
    fun findAllByStartingTimeBetween(from: LocalDateTime, to: LocalDateTime): Flow<Screening>
}