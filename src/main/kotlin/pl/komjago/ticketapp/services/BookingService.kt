package pl.komjago.ticketapp.services

import org.springframework.stereotype.Service
import pl.komjago.ticketapp.controllers.booking.dto.ChooseScreeningOutput
import pl.komjago.ticketapp.controllers.booking.dto.GetScreeningsInput
import pl.komjago.ticketapp.controllers.booking.dto.GetScreeningsOutput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationInput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationOutput

@Service
interface BookingService {
    fun getScreenings(getScreeningsInput: GetScreeningsInput): GetScreeningsOutput
    fun chooseScreening(screeningId: Long): ChooseScreeningOutput
    fun makeReservation(makeReservationInput: MakeReservationInput): MakeReservationOutput
}