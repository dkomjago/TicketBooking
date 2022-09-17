package pl.komjago.ticketapp.services

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
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
import pl.komjago.ticketapp.entity.Movie
import pl.komjago.ticketapp.entity.Reservation
import pl.komjago.ticketapp.entity.Room
import pl.komjago.ticketapp.entity.Screening
import pl.komjago.ticketapp.entity.Seat
import pl.komjago.ticketapp.entity.Ticket
import pl.komjago.ticketapp.entity.TicketType
import pl.komjago.ticketapp.repository.ReservationRepository
import pl.komjago.ticketapp.repository.ScreeningRepository
import pl.komjago.ticketapp.repository.SeatRepository
import pl.komjago.ticketapp.repository.TicketRepository
import pl.komjago.ticketapp.repository.TicketTypeRepository
import java.time.LocalDateTime
import java.util.*
import javax.persistence.EntityNotFoundException

class BookingServiceTests {

    private val reservationRepository: ReservationRepository = mockk()
    private val ticketRepository: TicketRepository = mockk()
    private val seatRepository: SeatRepository = mockk()
    private val ticketTypeRepository: TicketTypeRepository = mockk()
    private val screeningRepository: ScreeningRepository = mockk()

    private val bookingService: BookingService =
            BookingServiceImpl(reservationRepository,
                    ticketRepository,
                    seatRepository,
                    ticketTypeRepository,
                    screeningRepository,
                    15
            )

    @Test
    fun `get screenings with from and to dates returns GetScreeningsOutput sorted by title and screening time`() {

        //region create expected output
        val movie = Movie(null,
                "TEST1",
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val movie2 = Movie(null,
                "TEST2",
                "TESTCAST 1",
                "TESTDIRECTOR 2",
                "Shrek is life",
                144
        )
        val room = Room(null,
                "testroom",
                listOf(Seat(null, 1, 1)),
                1)
        val screeningList = listOf(
                Screening(1,
                        movie,
                        room,
                        LocalDateTime.now().plusDays(1)
                ),
                Screening(2,
                        movie2,
                        room,
                        LocalDateTime.now()
                ),
                Screening(3,
                        movie,
                        room,
                        LocalDateTime.now().plusDays(2)
                ))
        val expectedOutputUnsorted = GetScreeningsOutput(
                screeningList.map {
                    ScreeningInfo(it.id!!,
                            it.movie.title,
                            it.room.seatCount,
                            it.startingTime)
                })
        val expectedOutputSorted = GetScreeningsOutput(
                screeningList.map {
                    ScreeningInfo(it.id!!,
                            it.movie.title,
                            it.room.seatCount,
                            it.startingTime)
                }.sortedWith(compareBy({ it.movieTitle }, { it.startingTime }))
        )
        //endregion

        //region repository mocking
        every { screeningRepository.findAllByStartingTimeBetween(LocalDateTime.MIN, LocalDateTime.MAX) } returns screeningList
        every { ticketRepository.countAllByScreeningId(any()) } returns 0
        //endregion

        assertNotEquals(expectedOutputUnsorted, expectedOutputSorted)
        assertEquals(expectedOutputSorted, bookingService.getScreenings(LocalDateTime.MIN, LocalDateTime.MAX))
    }

    @Test
    fun `get screenings with from and to dates finds no screenings throws IllegalStateException`() {
        val input = LocalDateTime.now()

        //region repository mocking
        every { screeningRepository.findAllByStartingTimeBetween(input,input) } returns emptyList()
        every { ticketRepository.countAllByScreeningId(any()) } returns 0
        //endregion

        assertThrows(IllegalStateException::class.java) {
            bookingService.getScreenings(input,input)
        }
    }

    @Test
    fun `choose screening with id returns ChooseScreeningOutput`() {
        val screeningId = 0L

        //region create expected output
        val movie = Movie(1,
                "TEST1",
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val room = Room(1,
                "testroom",
                listOf(Seat(1, 1, 1)),
                1)

        val screening = Screening(screeningId,
                movie,
                room,
                LocalDateTime.now())

        val ticketType = TicketType(1,
                "test",
                0.5.toBigDecimal())

        val ticketTypeInfo = ticketType.let {
            TicketTypeInfo(it.id,
                    it.name,
                    it.price)
        }

        val output = ChooseScreeningOutput(
                screeningId,
                MovieInfo("TEST1",
                        "TESTCAST 1",
                        "TESTDIRECTOR 1",
                        "Shrek is love",
                        144
                ),
                RoomInfo("testroom",
                        listOf(SeatInfo(1, 1, 1, false))
                ),
                screening.startingTime,
                listOf(ticketTypeInfo)
        )
        //endregion

        //region repository mocking
        every { screeningRepository.findById(any()) } returns Optional.of(screening)
        every { ticketRepository.findAllByScreeningId(any()) } returns emptyList()
        every { ticketRepository.countAllByScreeningId(any()) } returns 0
        every { ticketTypeRepository.findAll() } returns listOf(ticketType)
        //endregion

        assertEquals(bookingService.chooseScreening(screeningId), output)
    }

    @Test
    fun `choose screening with invalid id throws IllegalArgumentException`() {
        val screeningId = 0L

        //region repository mocking
        every { screeningRepository.findById(any()) } returns Optional.empty()
        //endregion

        assertThrows(IllegalArgumentException::class.java) {
            bookingService.chooseScreening(screeningId)
        }
    }

    @Test
    fun `make reservation with MakeReservationInput returns MakeReservationOutput`() {

        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Test",
                "Testerson"
        )

        val expectedOutput = MakeReservationOutput(
                0.5.toBigDecimal(),
                LocalDateTime.now().plusHours(1)
        )

        //region repository mocking
        val movie = Movie(null,
                "TEST1",
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val room = Room(null,
                "testroom",
                listOf(Seat(null, 1, 1)),
                1)

        val screening = Screening(0,
                movie,
                room,
                LocalDateTime.now().plusDays(1))

        val ticketType = TicketType(1,
                "test",
                0.5.toBigDecimal())

        val seat = Seat(0, 1, 1)

        every { screeningRepository.findById(any()) } returns Optional.of(screening)
        every { ticketTypeRepository.findById(any()) } returns Optional.of(ticketType)
        every { seatRepository.findById(any()) } returns Optional.of(seat)
        every { ticketRepository.findAllByScreeningId(any()) } returns emptyList()

        //counters for sequenced id simulation
        var ticketCounter = 0L
        var reservationCounter = 0L

        val ticket = slot<List<Ticket>>()
        every { ticketRepository.saveAll(capture(ticket)) } answers {
            ticket.captured.map {
                it.copy(id = ticketCounter++)
            }
        }

        val reservation = slot<Reservation>()
        every { reservationRepository.save(capture(reservation)) } answers { reservation.captured.copy(id = reservationCounter++) }
        //endregion

        val serviceOutput = bookingService.makeReservation(input)

        assertEquals(expectedOutput.price, serviceOutput.price)
    }

    @Test
    fun `make reservation with MakeReservationInput containing no seats throws IllegalArgumentException`() {

        val input = MakeReservationInput(
                emptyList(),
                0,
                "Test",
                "Testerson"
        )

        assertThrows(IllegalArgumentException::class.java) {
            bookingService.makeReservation(input)
        }
    }

    @Test
    fun `make reservation with MakeReservationInput containing duplicated seatId throws IllegalArgumentException`() {

        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0),
                        BookedSeatInfo(1, 0)),
                0,
                "Test",
                "Testerson"
        )

        assertThrows(IllegalArgumentException::class.java) {
            bookingService.makeReservation(input)
        }
    }

    @Test
    fun `make reservation with MakeReservationInput containing invalid screeningId throws EntityNotFoundException`() {

        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Test",
                "Testerson"
        )

        every { screeningRepository.findById(any()) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java) {
            bookingService.makeReservation(input)
        }
    }

    @Test
    fun `make reservation with MakeReservationInput containing invalid seatId throws EntityNotFoundException`() {

        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Test",
                "Testerson"
        )

        //region repository mocking
        val movie = Movie(null,
                "TEST1",
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val room = Room(null,
                "testroom",
                listOf(Seat(null, 1, 1)),
                1)

        val screening = Screening(0,
                movie,
                room,
                LocalDateTime.now().plusDays(1))

        every { screeningRepository.findById(any()) } returns Optional.of(screening)
        every { seatRepository.findById(any()) } returns Optional.empty()
        //endregion

        assertThrows(EntityNotFoundException::class.java) {
            bookingService.makeReservation(input)
        }
    }

    @Test
    fun `make reservation with MakeReservationInput containing invalid ticketTypeId throws EntityNotFoundException`() {

        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Test",
                "Testerson"
        )

        //region repository mocking
        val movie = Movie(null,
                "TEST1",
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val room = Room(null,
                "testroom",
                listOf(Seat(null, 1, 1)),
                1)

        val screening = Screening(0,
                movie,
                room,
                LocalDateTime.now().plusDays(1))

        val seat = Seat(0, 1, 1)

        every { screeningRepository.findById(any()) } returns Optional.of(screening)
        every { seatRepository.findById(any()) } returns Optional.of(seat)
        every { ticketTypeRepository.findById(any()) } returns Optional.empty()
        //endregion

        assertThrows(EntityNotFoundException::class.java) {
            bookingService.makeReservation(input)
        }
    }

    @Test
    fun `make reservation with MakeReservationInput containing 1 space between seats throws IllegalArgumentException`() {
        //region create example input
        val movie = Movie(null,
                "TEST1",
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val room = Room(null,
                "testroom",
                listOf(Seat(null, 1, 1),
                        Seat(null, 1, 2),
                        Seat(null, 1, 3)),
                1)

        val screening = Screening(0,
                movie,
                room,
                LocalDateTime.now().plusDays(1))

        val ticketType = TicketType(1,
                "test",
                0.5.toBigDecimal())

        val seatList = List(3) {
            Seat(it.toLong(), 1, it + 1)
        }

        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0),
                        BookedSeatInfo(1, 2)),
                0,
                "Test",
                "Testerson"
        )
        //endregion

        //region repository mocking
        every { screeningRepository.findById(any()) } returns Optional.of(screening)
        every { ticketTypeRepository.findById(any()) } returns Optional.of(ticketType)
        every { ticketRepository.findAllByScreeningId(any()) } returns emptyList()

        val seatId = slot<Long>()
        every { seatRepository.findById(capture(seatId)) } answers { Optional.of(seatList[seatId.captured.toInt()]) }

        //endregion

        assertThrows(IllegalArgumentException::class.java) {
            bookingService.makeReservation(input)
        }
    }

    @Test
    fun `make reservation with MakeReservationInput containing 1 space between already reserved seats throws IllegalArgumentException`() {
        //region create example input
        val movie = Movie(null,
                "TEST1",
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val room = Room(null,
                "testroom",
                listOf(Seat(null, 1, 1),
                        Seat(null, 1, 2),
                        Seat(null, 1, 3)),
                1)

        val screening = Screening(0,
                movie,
                room,
                LocalDateTime.now().plusDays(1))

        val ticketType = TicketType(1,
                "test",
                0.5.toBigDecimal())

        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0),
                        BookedSeatInfo(1, 2)),
                0,
                "Test",
                "Testerson"
        )
        //endregion

        //region repository mocking
        every { screeningRepository.findById(any()) } returns Optional.of(screening)
        every { ticketTypeRepository.findById(any()) } returns Optional.of(ticketType)
        every { ticketRepository.findAllByScreeningId(any()) } returns listOf(
                Ticket(1, screening, Seat(3, 1, 3), ticketType)
        )

        val seatId = slot<Long>()
        every { seatRepository.findById(capture(seatId)) } answers { Optional.of(room.seats[seatId.captured.toInt()]) }

        //endregion

        assertThrows(IllegalArgumentException::class.java) {
            bookingService.makeReservation(input)
        }
    }

    @Test
    fun `make reservation with MakeReservationInput containing already reserved seat throws IllegalArgumentException`() {
        //region create example input
        val movie = Movie(null,
                "TEST1",
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val room = Room(null,
                "testroom",
                listOf(Seat(null, 1, 1),
                        Seat(null, 1, 2),
                        Seat(null, 1, 3)),
                1)

        val screening = Screening(0,
                movie,
                room,
                LocalDateTime.now().plusDays(1))

        val ticketType = TicketType(1,
                "test",
                0.5.toBigDecimal())

        val seatList = List(3) {
            Seat(it.toLong(), 1, it + 1)
        }

        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0),
                        BookedSeatInfo(1, 2)),
                0,
                "Test",
                "Testerson"
        )
        //endregion

        //region repository mocking
        every { screeningRepository.findById(any()) } returns Optional.of(screening)
        every { ticketTypeRepository.findById(any()) } returns Optional.of(ticketType)
        every { ticketRepository.findAllByScreeningId(any()) } returns listOf(
                Ticket(1, screening, seatList[0], ticketType)
        )

        val seatId = slot<Long>()
        every { seatRepository.findById(capture(seatId)) } answers { Optional.of(seatList[seatId.captured.toInt()]) }

        //endregion

        assertThrows(IllegalArgumentException::class.java) {
            bookingService.makeReservation(input)
        }
    }

    @Test
    fun `make reservation with MakeReservationInput containing screening with starting time after threshold throws IllegalArgumentException`() {
        //region create example input
        val movie = Movie(null,
                "TEST1",
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val room = Room(null,
                "testroom",
                listOf(Seat(null, 1, 1),
                        Seat(null, 1, 2),
                        Seat(null, 1, 3)),
                1)

        val screening = Screening(0,
                movie,
                room,
                LocalDateTime.now())

        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0),
                        BookedSeatInfo(1, 2)),
                0,
                "Test",
                "Testerson"
        )
        //endregion

        //region repository mocking
        every { screeningRepository.findById(any()) } returns Optional.of(screening)
        //endregion

        assertThrows(IllegalArgumentException::class.java) {
            bookingService.makeReservation(input)
        }
    }
}