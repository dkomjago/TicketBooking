package pl.komjago.ticketapp.services

import org.springframework.stereotype.Service
import pl.komjago.ticketapp.controllers.booking.dto.ChooseScreeningOutput
import pl.komjago.ticketapp.controllers.booking.dto.GetScreeningsOutput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationInput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationOutput
import java.time.LocalDateTime

@Service
interface BookingService {
    fun getScreenings(from: LocalDateTime, to: LocalDateTime): GetScreeningsOutput
    fun chooseScreening(screeningId: Long): ChooseScreeningOutput
    fun makeReservation(makeReservationInput: MakeReservationInput): MakeReservationOutput
}