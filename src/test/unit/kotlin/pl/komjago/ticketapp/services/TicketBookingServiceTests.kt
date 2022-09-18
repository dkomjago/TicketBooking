package pl.komjago.ticketapp.services

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pl.komjago.ticketapp.controllers.booking.dto.BookedSeatInfo
import pl.komjago.ticketapp.domain.Ticket
import pl.komjago.ticketapp.domain.exceptions.DuplicateSeatException
import pl.komjago.ticketapp.domain.exceptions.EmptyPlaceBetweenSeatsException
import pl.komjago.ticketapp.domain.exceptions.InvalidScreeningException
import pl.komjago.ticketapp.domain.exceptions.InvalidSeatException
import pl.komjago.ticketapp.domain.exceptions.InvalidTicketTypeException
import pl.komjago.ticketapp.domain.exceptions.NoSeatSelectedException
import pl.komjago.ticketapp.domain.exceptions.SeatAlreadyBookedException
import pl.komjago.ticketapp.getTestMakeReservationInput
import pl.komjago.ticketapp.getTestMakeReservationOutput
import pl.komjago.ticketapp.getTestReservation
import pl.komjago.ticketapp.getTestScreening
import pl.komjago.ticketapp.getTestSeat
import pl.komjago.ticketapp.getTestTicket
import pl.komjago.ticketapp.getTestTicketType
import pl.komjago.ticketapp.repositories.ReservationRepository
import pl.komjago.ticketapp.repositories.ScreeningRepository
import pl.komjago.ticketapp.repositories.SeatRepository
import pl.komjago.ticketapp.repositories.TicketRepository
import pl.komjago.ticketapp.repositories.TicketTypeRepository
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@FlowPreview
class TicketBookingServiceTests {

    private val reservationRepository: ReservationRepository = mockk()
    private val ticketRepository: TicketRepository = mockk()
    private val seatRepository: SeatRepository = mockk()
    private val ticketTypeRepository: TicketTypeRepository = mockk()
    private val screeningRepository: ScreeningRepository = mockk()

    private val serviceUnderTest = TicketBookingService(
        reservationRepository,
        ticketRepository,
        seatRepository,
        ticketTypeRepository,
        screeningRepository,
        15
    )

    @BeforeEach
    fun init() {
        every {
            screeningRepository.findAllByStartingTimeBetween(any(), any())
        } returns flow { emit(getTestScreening()) }
        every { seatRepository.findAllById(any<Iterable<Int>>()) } returns flow { emit(getTestSeat()) }
        coEvery { screeningRepository.findById(any()) } returns getTestScreening()
        every { ticketRepository.findAllByScreeningId(any()) } returns emptyFlow()
        every { ticketTypeRepository.findAllById(any<Iterable<Int>>()) } returns flow { emit(getTestTicketType()) }
        every { ticketRepository.saveAll(any<Iterable<Ticket>>()) } returns flow { emit(getTestTicket()) }
        every { seatRepository.findAllById(any<Iterable<Int>>()) } returns emptyFlow()
    }

    @Nested
    inner class MakeReservation {
        @Test
        fun `returns MakeReservationOutput`() = runTest {
            val input = getTestMakeReservationInput(selectedSeats = listOf(BookedSeatInfo(1, 1)))
            val expected = getTestMakeReservationOutput(ticketIds = listOf(1))

            every { seatRepository.findAllById(any<Iterable<Int>>()) } returns flow {
                emit(getTestSeat(number = 1, id = 1))
            }
            coEvery { reservationRepository.save(any()) } returns getTestReservation()

            val result = serviceUnderTest.makeReservation(input)

            assertEquals(expected, result)
        }

        @Test
        fun `no seats throws NoSeatSelectedException`() = runTest {
            val input = getTestMakeReservationInput(selectedSeats = emptyList())

            assertThrows<NoSeatSelectedException> {
                serviceUnderTest.makeReservation(input)
            }
        }

        @Test
        fun `duplicated seatId throws DuplicateSeatException`() =
            runTest {
                val input = getTestMakeReservationInput(
                    selectedSeats = listOf(
                        BookedSeatInfo(1, 0),
                        BookedSeatInfo(1, 0)
                    )
                )

                assertThrows<DuplicateSeatException> {
                    serviceUnderTest.makeReservation(input)
                }
            }

        @Test
        fun `invalid screeningId throws InvalidScreeningException`() =
            runTest {
                val input = getTestMakeReservationInput(selectedSeats = listOf(BookedSeatInfo(1, 1)))

                coEvery { screeningRepository.findById(any()) } returns null


                assertThrows<InvalidScreeningException> {
                    serviceUnderTest.makeReservation(input)
                }
            }

        @Test
        fun `invalid seatId throws InvalidSeatException`() =
            runTest {
                val input = getTestMakeReservationInput(selectedSeats = listOf(BookedSeatInfo(1, 1)))

                every { seatRepository.findAllById(any<Iterable<Int>>()) } returns emptyFlow()

                assertThrows<InvalidSeatException> {
                    serviceUnderTest.makeReservation(input)
                }
            }

        @Test
        fun `invalid ticketTypeId throws InvalidTicketTypeException`() =
            runTest {
                val input = getTestMakeReservationInput(selectedSeats = listOf(BookedSeatInfo(1, 1)))

                every { seatRepository.findAllById(any<Iterable<Int>>()) } returns flow {
                    emit(getTestSeat(number = 1, id = 1))
                    emit(getTestSeat(number = 3, id = 2))
                }
                every { ticketTypeRepository.findAllById(any<Iterable<Int>>()) } returns emptyFlow()

                assertThrows<InvalidTicketTypeException> {
                    serviceUnderTest.makeReservation(input)
                }
            }

        @Test
        fun `1 space between seats throws EmptyPlaceBetweenSeatsException`() =
            runTest {
                val input = getTestMakeReservationInput(
                    selectedSeats = listOf(
                        BookedSeatInfo(1, 1),
                        BookedSeatInfo(1, 2)
                    )
                )

                every { seatRepository.findAllById(any<Iterable<Int>>()) } returns flow {
                    emit(getTestSeat(number = 1, id = 1))
                    emit(getTestSeat(number = 3, id = 2))
                }
                every { ticketTypeRepository.findAllById(any<Iterable<Int>>()) } returns flow {
                    emit(getTestTicketType(id = 1))
                }

                assertThrows<EmptyPlaceBetweenSeatsException> {
                    serviceUnderTest.makeReservation(input)
                }
            }

        @Test
        fun `1 space between already reserved seats throws IllegalArgumentException`() =
            runTest {
                val input = getTestMakeReservationInput(selectedSeats = listOf(BookedSeatInfo(1, 1)))

                every { seatRepository.findAllById(any<Iterable<Int>>()) } returns flow {
                    emit(getTestSeat(number = 2, id = 1))
                }
                every { seatRepository.findAllById(any<Flow<Int>>()) } returns flow {
                    emit(getTestSeat(number = 1, id = 2))
                    emit(getTestSeat(number = 4, id = 3))
                }
                every { ticketRepository.findAllByScreeningId(any()) } returns flow {
                    emit(getTestTicket(id = 1, seatId = 2))
                    emit(getTestTicket(id = 2, seatId = 3))
                }
                every { ticketTypeRepository.findAllById(any<Iterable<Int>>()) } returns flow {
                    emit(getTestTicketType(id = 1))
                }

                assertThrows<EmptyPlaceBetweenSeatsException> {
                    serviceUnderTest.makeReservation(input)
                }
            }

        @Test
        fun `already reserved seat throws SeatAlreadyBookedException`() =
            runTest {
                val input = getTestMakeReservationInput(selectedSeats = listOf(BookedSeatInfo(1, 1)))

                every { seatRepository.findAllById(any<Iterable<Int>>()) } returns flow {
                    emit(getTestSeat(number = 1, id = 1))
                }
                every { seatRepository.findAllById(any<Flow<Int>>()) } returns flow {
                    emit(getTestSeat(number = 1, id = 1))
                }
                every { ticketRepository.findAllByScreeningId(any()) } returns flow {
                    emit(getTestTicket(id = 1, seatId = 1))
                }
                every { ticketTypeRepository.findAllById(any<Iterable<Int>>()) } returns flow {
                    emit(getTestTicketType(id = 1))
                }

                assertThrows<SeatAlreadyBookedException> {
                    serviceUnderTest.makeReservation(input)
                }
            }

        @Test
        fun `screening with starting time after threshold throws InvalidScreeningException`() =
            runTest {
                val input = getTestMakeReservationInput(selectedSeats = listOf(BookedSeatInfo(1, 1)))

                coEvery { screeningRepository.findById(any()) } returns getTestScreening(startingTime = LocalDateTime.now())

                assertThrows<InvalidScreeningException> {
                    serviceUnderTest.makeReservation(input)
                }
            }
    }
}