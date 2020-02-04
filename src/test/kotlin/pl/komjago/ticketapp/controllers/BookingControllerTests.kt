import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import pl.komjago.ticketapp.TicketBookingApplication
import pl.komjago.ticketapp.controllers.booking.dto.BookedSeatInfo
import pl.komjago.ticketapp.controllers.booking.dto.ChooseScreeningOutput
import pl.komjago.ticketapp.controllers.booking.dto.GetScreeningsInput
import pl.komjago.ticketapp.controllers.booking.dto.GetScreeningsOutput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationInput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationOutput
import pl.komjago.ticketapp.controllers.booking.dto.MovieInfo
import pl.komjago.ticketapp.controllers.booking.dto.RoomInfo
import pl.komjago.ticketapp.controllers.booking.dto.ScreeningInfo
import pl.komjago.ticketapp.controllers.booking.dto.SeatInfo
import pl.komjago.ticketapp.controllers.booking.dto.TicketTypeInfo
import pl.komjago.ticketapp.entity.Movie
import pl.komjago.ticketapp.entity.Room
import pl.komjago.ticketapp.entity.Screening
import pl.komjago.ticketapp.entity.Seat
import pl.komjago.ticketapp.entity.TicketType

import pl.komjago.ticketapp.services.BookingService
import pl.komjago.ticketapp.util.toZloty
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException

@WebMvcTest
@ContextConfiguration(classes = [TicketBookingApplication::class])
class BookingControllerTests @Autowired constructor(private val mvc: MockMvc, private val mapper: ObjectMapper) {

    @MockkBean
    private lateinit var service: BookingService

    @Test
    fun `get screenings with GetScreeningsInput returns GetScreeningsOutput responds with OK`() {
        val input = GetScreeningsInput(LocalDateTime.now().minusYears(1), LocalDateTime.now().plusYears(1))
        val expectedOutput = GetScreeningsOutput(List(2) {
            ScreeningInfo(it.toLong(), "Test$it", it, LocalDateTime.now())
        })

        every { service.getScreenings(any()) } returns expectedOutput

        mvc.post("/api/booking/screenings")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isOk }
            content {
                contentType(MediaType.APPLICATION_JSON)
                contentTypeCompatibleWith("application/json")
                json(mapper.writeValueAsString(expectedOutput))
            }
        }
    }

    @Test
    fun `get screenings with GetScreeningsInput catches IllegalStateException responds with NO CONTENT`() {
        val input = GetScreeningsInput(LocalDateTime.now(), LocalDateTime.now())

        every { service.getScreenings(any()) } throws IllegalStateException()

        mvc.post("/api/booking/screenings")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent }
        }
    }

    @Test
    fun `choose screening with id returns ChooseScreeningOutput responds with OK`() {
        val screeningId = 0L

        //region mock output DTO
        val movie = Movie(null,
                "TEST1",
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val room = Room(null,
                "testroom",
                listOf(Seat(1, 1, 1)),
                1)

        val screening = Screening(screeningId,
                movie,
                room,
                LocalDateTime.now())

        val ticketType = TicketType(1,
                "test",
                toZloty(0.50))

        val ticketTypeInfo = ticketType.let {
            TicketTypeInfo(it.id,
                    it.name,
                    it.price)
        }

        val expectedOutput = ChooseScreeningOutput(
                screeningId,
                MovieInfo("TEST1",
                        "TESTCAST 1",
                        "TESTDIRECTOR 1",
                        "Shrek is love",
                        144
                ),
                RoomInfo("testroom",
                        listOf(SeatInfo(1, 1, 1, false)),
                        1
                ),
                screening.startingTime,
                listOf(ticketTypeInfo)
        )
        //endregion

        every { service.chooseScreening(screeningId) } returns expectedOutput

        mvc.get("/api/booking/screening/$screeningId")
                .andExpect {
                    status { isOk }
                    content {
                        contentType(MediaType.APPLICATION_JSON)
                        contentTypeCompatibleWith("application/json")
                        json(mapper.writeValueAsString(expectedOutput))
                    }
                }
    }

    @Test
    fun `choose screening with id catches IllegalArgumentException responds with BAD REQUEST`() {
        val screeningId = 0L

        every { service.chooseScreening(screeningId) } throws IllegalArgumentException()

        mvc.get("/api/booking/screening/$screeningId")
                .andExpect {
                    status { isBadRequest }
                }
    }

    @Test
    fun `make reservation with MakeReservationInput returns MakeReservationOutput responds with CREATED`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Test",
                "Testerson"
        )

        val expectedOutput = MakeReservationOutput(
                toZloty(0.50),
                LocalDateTime.now().plusHours(1)
        )

        every { service.makeReservation(input) } returns expectedOutput

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated }
            content {
                contentType(MediaType.APPLICATION_JSON)
                contentTypeCompatibleWith("application/json")
                json(mapper.writeValueAsString(expectedOutput))
            }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput catches IllegalArgumentException responds with BAD REQUEST`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Test",
                "Testerson"
        )

        every { service.makeReservation(input) } throws java.lang.IllegalArgumentException()

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput catches EntityNotFoundException responds with BAD REQUEST`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Test",
                "Testerson"
        )

        every { service.makeReservation(input) } throws EntityNotFoundException()

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput has name starting with small letter fails name validation`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "test",
                "Testerson"
        )

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput name has more than one capital letter fails name validation`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "TEST",
                "Testerson"
        )

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput has less than 3 letter name fails name validation`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Ex",
                "Testerson"
        )

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput has name with numbers fails name validation`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Tester1",
                "Testerson"
        )

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput has polish characters in the name passes validation`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Ęęęęęęęęę",
                "Testerson"
        )

        val expectedOutput = MakeReservationOutput(
                toZloty(0.50),
                LocalDateTime.now()
        )

        every { service.makeReservation(input) } returns expectedOutput

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput has less than 3 letter surname fails surname validation`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Tester",
                "Ex"
        )

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput has surname starting with a small letter fails surname validation`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Tester",
                "testerson"
        )

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput has polish characters in the surname passes validation`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Tester",
                "Ęęęęęęe"
        )

        val expectedOutput = MakeReservationOutput(
                toZloty(0.50),
                LocalDateTime.now()
        )

        every { service.makeReservation(input) } returns expectedOutput

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isCreated }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput has 2 part surname starting where second part starts with a small letter fails surname validation`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Tester",
                "Testerson-son"
        )

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `make reservation with MakeReservationInput has surname with numbers fails surname validation`() {
        val input = MakeReservationInput(
                listOf(BookedSeatInfo(1, 0)),
                0,
                "Tester",
                "Testerson1"
        )

        mvc.post("/api/booking/book")
        {
            contentType = MediaType.APPLICATION_JSON
            content = mapper.writeValueAsString(input)
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest }
        }
    }
}