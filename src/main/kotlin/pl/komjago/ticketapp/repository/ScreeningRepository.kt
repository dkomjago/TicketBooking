package pl.komjago.ticketapp.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.komjago.ticketapp.entity.Screening
import java.time.LocalDateTime

@Repository
interface ScreeningRepository : JpaRepository<Screening, Long?> {
    fun findAllByStartingTimeBetween(from: LocalDateTime, to: LocalDateTime): List<Screening>
}