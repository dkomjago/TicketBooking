package pl.komjago.ticketapp.services

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pl.komjago.ticketapp.*
import pl.komjago.ticketapp.controllers.booking.dto.ScreeningInfo
import pl.komjago.ticketapp.domain.exceptions.InvalidScreeningException
import pl.komjago.ticketapp.domain.exceptions.NoScreeningsException
import pl.komjago.ticketapp.repositories.MovieRepository
import pl.komjago.ticketapp.repositories.RoomRepository
import pl.komjago.ticketapp.repositories.ScreeningRepository
import pl.komjago.ticketapp.repositories.SeatRepository
import pl.komjago.ticketapp.repositories.TicketRepository
import pl.komjago.ticketapp.repositories.TicketTypeRepository
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class MovieScreeningServiceTests {

    private val movieRepository: MovieRepository = mockk()
    private val roomRepository: RoomRepository = mockk()
    private val ticketRepository: TicketRepository = mockk()
    private val seatRepository: SeatRepository = mockk()
    private val ticketTypeRepository: TicketTypeRepository = mockk()
    private val screeningRepository: ScreeningRepository = mockk()

    private val serviceUnderTest = MovieScreeningService(
        movieRepository,
        roomRepository,
        ticketRepository,
        seatRepository,
        ticketTypeRepository,
        screeningRepository
    )

    @BeforeEach
    fun init() {
        every {
            screeningRepository.findAllByStartingTimeBetween(any(), any())
        } returns flow { emit(getTestScreening()) }
        every { movieRepository.findAllById(any<Flow<Int>>()) } returns flow { emit(getTestMovie()) }
        every { roomRepository.findAllById(any<Flow<Int>>()) } returns flow { emit(getTestRoom()) }
        every { seatRepository.findAllById(any<Flow<Int>>()) } returns flow { emit(getTestSeat()) }
        coEvery { ticketRepository.countAllByScreeningId(any()) } returns 0
        coEvery { screeningRepository.findById(any()) } returns getTestScreening()
        every { ticketRepository.findAllByScreeningId(any()) } returns emptyFlow()
        coEvery { movieRepository.findById(any()) } returns getTestMovie()
        coEvery { roomRepository.findById(any()) } returns getTestRoom()
        every { ticketTypeRepository.findAll() } returns flow { emit(getTestTicketType()) }
    }

    @Nested
    inner class GetScreenings {
        @Test
        fun `returns GetScreeningsOutput`() = runTest {
            val expected: List<ScreeningInfo> = listOf(getTestScreeningInfo())

            val result = serviceUnderTest.getScreenings(LocalDateTime.MIN, LocalDateTime.MAX)

            assertEquals(expected, result.toList())
        }

        @Test
        fun `time interval with no screenings throws NoScreeningsException`() = runTest {
            every {
                screeningRepository.findAllByStartingTimeBetween(eq(defaultDateTime), eq(defaultDateTime))
            } returns emptyFlow()

            assertThrows<NoScreeningsException> {
                serviceUnderTest.getScreenings(defaultDateTime, defaultDateTime).collect()
            }
        }
    }

    @Nested
    inner class ChooseScreening {
        @Test
        fun `invalid id throws InvalidScreeningException`() = runTest {
            val nonExistingScreeningId = 0

            coEvery { screeningRepository.findById(eq(nonExistingScreeningId)) } returns null

            assertThrows<InvalidScreeningException> {
                serviceUnderTest.chooseScreening(nonExistingScreeningId)
            }
        }
    }
}