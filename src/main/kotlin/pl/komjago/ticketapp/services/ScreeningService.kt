package pl.komjago.ticketapp.services

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import pl.komjago.ticketapp.controllers.booking.dto.ChooseScreeningOutput
import pl.komjago.ticketapp.controllers.booking.dto.ScreeningInfo
import java.time.LocalDateTime

@Service
interface ScreeningService {
    fun getScreenings(from: LocalDateTime, to: LocalDateTime): Flow<ScreeningInfo>
    suspend fun chooseScreening(screeningId: Int): ChooseScreeningOutput
}