package pl.komjago.ticketapp.services

import org.joda.money.Money
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.komjago.ticketapp.controllers.booking.dto.BookedSeatInfo
import pl.komjago.ticketapp.controllers.booking.dto.ChooseScreeningOutput
import pl.komjago.ticketapp.controllers.booking.dto.GetScreeningsOutput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationInput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationOutput
import pl.komjago.ticketapp.controllers.booking.dto.MovieInfo
import pl.komjago.ticketapp.controllers.booking.dto.RoomInfo
import pl.komjago.ticketapp.controllers.booking.dto.ScreeningInfo
import pl.komjago.ticketapp.controllers.booking.dto.SeatInfo
import pl.komjago.ticketapp.controllers.booking.dto.TicketTypeInfo
import pl.komjago.ticketapp.entity.Reservation
import pl.komjago.ticketapp.entity.Screening
import pl.komjago.ticketapp.entity.Seat
import pl.komjago.ticketapp.entity.Ticket
import pl.komjago.ticketapp.entity.TicketType
import pl.komjago.ticketapp.repository.ReservationRepository
import pl.komjago.ticketapp.repository.ScreeningRepository
import pl.komjago.ticketapp.repository.SeatRepository
import pl.komjago.ticketapp.repository.TicketRepository
import pl.komjago.ticketapp.repository.TicketTypeRepository
import pl.komjago.ticketapp.util.sumByBigDecimal
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException

@Service
class BookingServiceImpl(
        private val reservationRepository: ReservationRepository,
        private val ticketRepository: TicketRepository,
        private val seatRepository: SeatRepository,
        private val ticketTypeRepository: TicketTypeRepository,
        private val screeningRepository: ScreeningRepository,
        @Value("\${bookingEndsBeforeScreeningInMinutes}")
        private val bookingEndsBeforeScreeningInMinutes: Long
) : BookingService {


    override fun getScreenings(from: LocalDateTime, to: LocalDateTime): GetScreeningsOutput {
        val screenings: List<Screening> = screeningRepository.findAllByStartingTimeBetween(
                from,
                to
        )
        if (screenings.isEmpty()) throw IllegalStateException("No screenings found in given time frame")

        return GetScreeningsOutput(screenings.map {
            ScreeningInfo(it.id!!,
                    it.movie.title,
                    it.room.seats.count() - ticketRepository.countAllByScreeningId(it.id),
                    it.startingTime
            )
        }.sortedWith(compareBy({ it.movieTitle }, { it.startingTime })))
    }

    override fun chooseScreening(screeningId: Long): ChooseScreeningOutput {
        val screeningOptional = screeningRepository.findById(screeningId)
        val screening = if (screeningOptional.isPresent) screeningOptional.get()
        else throw IllegalArgumentException("Invalid screeningId")

        val bookedSeats = ticketRepository.findAllByScreeningId(screeningId).map { it.seat }

        val movieInfo = screening.movie.let {
            MovieInfo(it.title,
                    it.cast,
                    it.director,
                    it.description,
                    it.durationInMinutes)
        }

        val roomInfo = screening.room.let { room ->
            RoomInfo(room.name,
                    room.seats.map { seat ->
                        SeatInfo(seat.id!!, seat.seatRow, seat.seatNumber, bookedSeats.contains(seat))
                    }
            )
        }

        val ticketTypeInfoList = ticketTypeRepository.findAll().map { TicketTypeInfo(it.id, it.name, it.price) }

        return ChooseScreeningOutput(screening.id!!,
                movieInfo,
                roomInfo,
                screening.startingTime,
                ticketTypeInfoList
        )
    }

    override fun makeReservation(makeReservationInput: MakeReservationInput): MakeReservationOutput {
        //Check if seat list input is empty
        if (makeReservationInput.selectedSeats.isEmpty()) {
            throw IllegalArgumentException("No seat selected")
        }

        //Check for duplicated seats
        if (makeReservationInput.selectedSeats.groupingBy { it.seatId }.eachCount().any { it.value > 1 }) {
            throw IllegalArgumentException("Duplicated seatId")
        }

        val screeningOptional = screeningRepository.findById(makeReservationInput.screeningId)
        val screening = if (screeningOptional.isPresent) screeningOptional.get() else throw EntityNotFoundException()

        //Check if bookingEndsBeforeScreeningInMinutes or more minutes before screening
        if (LocalDateTime.now().plusMinutes(bookingEndsBeforeScreeningInMinutes).isAfter(screening.startingTime)) {
            throw IllegalArgumentException("$bookingEndsBeforeScreeningInMinutes minutes before screening")
        }

        fun unpackSeatInfo(seatInfo: BookedSeatInfo): Pair<Seat, TicketType> {
            val seatOptional = seatRepository.findById(seatInfo.seatId)
            val seat = if (seatOptional.isPresent) seatOptional.get() else throw EntityNotFoundException("Invalid seatId")
            val ticketTypeOptional = ticketTypeRepository.findById(seatInfo.ticketTypeId)
            val ticketType = if (ticketTypeOptional.isPresent) ticketTypeOptional.get() else throw EntityNotFoundException("Invalid ticketTypeId")
            return Pair(seat, ticketType)
        }

        val selectedSeats = makeReservationInput.selectedSeats.map {
            unpackSeatInfo(it)
        }.toMap()

        val reservedSeats = ticketRepository.findAllByScreeningId(makeReservationInput.screeningId).map { it.seat }.toList()

        if (reservedSeats.any { selectedSeats.keys.contains(it) }) {
            throw IllegalArgumentException("Seat already booked")
        }

        //Check for empty space between seats
        var previous = 0
        var previousRow = 0
        selectedSeats.keys.plus(reservedSeats).sortedWith(compareBy({ it.seatRow }, { it.seatNumber })).forEach {
            when {
                it.seatRow != previousRow -> {
                    previous = it.seatNumber
                    previousRow = it.seatRow
                }
                it.seatNumber - previous != 2 -> previous = it.seatNumber
                else -> throw IllegalArgumentException("Empty place left between seats")
            }
        }

        val ticketList = ticketRepository.saveAll(selectedSeats.map {
            Ticket(null,
                    screening,
                    it.key,
                    it.value
            )
        })
        val savedReservation = reservationRepository.save(
                Reservation(null,
                        makeReservationInput.name,
                        makeReservationInput.surname,
                        screening,
                        ticketList,
                        LocalDateTime.now().plusHours(1)
                ))

        return MakeReservationOutput(
                Money.parse("PLN ${savedReservation.tickets.sumByBigDecimal { it.ticketType.price.amount }}"),
                savedReservation.expirationTime
        )
    }
}