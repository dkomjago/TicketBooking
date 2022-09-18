package pl.komjago.ticketapp.services

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationInput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationOutput
import pl.komjago.ticketapp.domain.Reservation
import pl.komjago.ticketapp.domain.Ticket
import pl.komjago.ticketapp.domain.exceptions.DuplicateSeatException
import pl.komjago.ticketapp.domain.exceptions.EmptyPlaceBetweenSeatsException
import pl.komjago.ticketapp.domain.exceptions.InvalidScreeningException
import pl.komjago.ticketapp.domain.exceptions.InvalidSeatException
import pl.komjago.ticketapp.domain.exceptions.InvalidTicketTypeException
import pl.komjago.ticketapp.domain.exceptions.NoSeatSelectedException
import pl.komjago.ticketapp.domain.exceptions.SeatAlreadyBookedException
import pl.komjago.ticketapp.repositories.*
import java.time.LocalDateTime

@FlowPreview
@Service
class TicketBookingService(
    private val reservationRepository: ReservationRepository,
    private val ticketRepository: TicketRepository,
    private val seatRepository: SeatRepository,
    private val ticketTypeRepository: TicketTypeRepository,
    private val screeningRepository: ScreeningRepository,
    @Value("\${bookingEndsBeforeScreeningInMinutes}") private val bookingEndsBeforeScreeningInMinutes: Long
) : BookingService {
    override suspend fun makeReservation(makeReservationInput: MakeReservationInput): MakeReservationOutput {

        if (makeReservationInput.selectedSeats.isEmpty()) {
            throw NoSeatSelectedException()
        }

        makeReservationInput.selectedSeats.groupingBy { it.seatId }.eachCount()
            .filter { it.value > 1 }.run {
                if (isNotEmpty()) throw DuplicateSeatException(keys.first())
            }

        val screening = screeningRepository.findById(makeReservationInput.screeningId)
            ?: throw InvalidScreeningException(screeningId = makeReservationInput.screeningId)

        if (LocalDateTime.now().plusMinutes(bookingEndsBeforeScreeningInMinutes).isAfter(screening.startingTime)) {
            throw InvalidScreeningException(
                screening.id,
                "$bookingEndsBeforeScreeningInMinutes minutes before screening",
            )
        }

        val selectedSeatIds = makeReservationInput.selectedSeats.map { it.seatId }
        val ticketTypeIds = makeReservationInput.selectedSeats.map { it.ticketTypeId }
        val seats = seatRepository.findAllById(selectedSeatIds)
        val ticketTypes = ticketTypeRepository.findAllById(ticketTypeIds)

        val selectedSeats = makeReservationInput.selectedSeats.associate { selectedSeat ->
            Pair(
                seats.firstOrNull { it.id == selectedSeat.seatId }
                    ?: throw InvalidSeatException(selectedSeat.seatId),
                ticketTypes.firstOrNull { it.id == selectedSeat.ticketTypeId }
                    ?: throw InvalidTicketTypeException(selectedSeat.ticketTypeId)
            )
        }

        val bookedSeats =
            ticketRepository.findAllByScreeningId(makeReservationInput.screeningId).flatMapMerge { ticket ->
                val bookedSeatIds = ticketRepository.findAllByScreeningId(ticket.screeningId).map { it.seatId }
                seatRepository.findAllById(bookedSeatIds)
            }

        bookedSeats.firstOrNull { selectedSeats.keys.contains(it) }?.run {
            throw SeatAlreadyBookedException(id)
        }

        selectedSeats.keys.plus(bookedSeats.toSet())
            .sortedBy { it.number }
            .groupingBy { it.row }
            .reduce { _, previousSeat, seat ->
                if (seat.number - previousSeat.number > 1)
                    throw EmptyPlaceBetweenSeatsException(seat.id)

                previousSeat
            }

        val savedReservation = reservationRepository.save(
            Reservation(
                makeReservationInput.name,
                LocalDateTime.now().plusHours(1)
            )
        )

        val savedTickets = ticketRepository.saveAll(selectedSeats.map {
            Ticket(
                savedReservation.id,
                screening.id,
                it.key.id,
                it.value.id
            )
        })

        return MakeReservationOutput(
            selectedSeats.values.sumOf { it.price },
            savedReservation.expirationTime,
            savedTickets.map { it.id }.toList()
        )
    }
}