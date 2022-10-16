package pl.komjago.ticketapp.services

import org.springframework.stereotype.Service
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationInput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationOutput

@Service
interface BookingService {
    suspend fun makeReservation(makeReservationInput: MakeReservationInput): MakeReservationOutput
}