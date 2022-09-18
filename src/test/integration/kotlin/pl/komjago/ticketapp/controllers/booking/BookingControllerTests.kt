package pl.komjago.ticketapp.controllers.booking

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.every
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import pl.komjago.ticketapp.defaultDateTime
import pl.komjago.ticketapp.domain.exceptions.*
import pl.komjago.ticketapp.getTestMakeReservationInput

import pl.komjago.ticketapp.services.BookingService
import pl.komjago.ticketapp.services.ScreeningService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@WebFluxTest(BookingController::class)
class BookingControllerTests {

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @MockkBean(relaxed = true)
    private lateinit var screeningService: ScreeningService

    @MockkBean(relaxed = true)
    private lateinit var bookingService: BookingService

    @Nested
    inner class GetScreenings {
        @Test
        fun `valid from and to dates return OK`() {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val input = LocalDateTime.now().format(formatter)

            webTestClient.get()
                .uri("/api/booking/screenings?from=$input&to=$input")
                .exchange()
                .expectStatus().isOk
                .expectBody()
        }

        @Test
        fun `NoScreeningsException return NO CONTENT`() {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            val input = LocalDateTime.now().format(formatter)

            every { screeningService.getScreenings(any(), any()) } throws NoScreeningsException(defaultDateTime, defaultDateTime)

            webTestClient.get()
                .uri("/api/booking/screenings?from=$input&to=$input")
                .exchange()
                .expectStatus().isNoContent
        }
    }

    @Nested
    inner class ChooseScreening {

        @Test
        fun `valid screening id returns OK`() {
            val screeningId = 1

            webTestClient.get()
                .uri("/api/booking/screening/$screeningId")
                .exchange()
                .expectStatus().isOk
                .expectBody()
        }

        @Test
        fun `InvalidScreeningException returns BAD REQUEST`() {
            val screeningId = 1

            coEvery { screeningService.chooseScreening(screeningId) } throws InvalidScreeningException()

            webTestClient.get()
                .uri("/api/booking/screening/$screeningId")
                .exchange()
                .expectStatus().isBadRequest
        }
    }

    @Nested
    inner class MakeReservation {
        @Test
        fun `returns CREATED`() {
            val input = getTestMakeReservationInput()

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isCreated
        }

        @Test
        fun `InvalidSeatException returns BAD REQUEST`() {
            val input = getTestMakeReservationInput()

            coEvery { bookingService.makeReservation(input) } throws InvalidSeatException()

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `DuplicateSeatException returns BAD REQUEST`() {
            val input = getTestMakeReservationInput()

            coEvery { bookingService.makeReservation(input) } throws DuplicateSeatException(0)

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `EmptyPlaceBetweenSeatsException returns BAD REQUEST`() {
            val input = getTestMakeReservationInput()

            coEvery { bookingService.makeReservation(input) } throws EmptyPlaceBetweenSeatsException(0)

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `InvalidMovieException returns BAD REQUEST`() {
            val input = getTestMakeReservationInput()

            coEvery { bookingService.makeReservation(input) } throws InvalidMovieException(0)

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `InvalidScreeningException returns BAD REQUEST`() {
            val input = getTestMakeReservationInput()

            coEvery { bookingService.makeReservation(input) } throws InvalidScreeningException(0)

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `InvalidTicketTypeException returns BAD REQUEST`() {
            val input = getTestMakeReservationInput()

            coEvery { bookingService.makeReservation(input) } throws InvalidTicketTypeException(0)

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `MovieScreeningMismatchException returns BAD REQUEST`() {
            val input = getTestMakeReservationInput()

            coEvery { bookingService.makeReservation(input) } throws MovieScreeningMismatchException(0)

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `NoSeatSelectedException returns BAD REQUEST`() {
            val input = getTestMakeReservationInput()

            coEvery { bookingService.makeReservation(input) } throws NoSeatSelectedException()

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `SeatAlreadyBookedException returns BAD REQUEST`() {
            val input = getTestMakeReservationInput()

            coEvery { bookingService.makeReservation(input) } throws SeatAlreadyBookedException(0)

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `name starting with a small letter fails validation`() {
            val input = getTestMakeReservationInput(name = "test")

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `name with multiple capital letters fails validation`() {
            val input = getTestMakeReservationInput(name = "TEST")

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `2 letter name fails validation`() {
            val input = getTestMakeReservationInput(name = "T")

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `name with numbers fails validation`() {
            val input = getTestMakeReservationInput(name = "Tester1")

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }

        @Test
        fun `name with polish characters passes validation`() {
            val input = getTestMakeReservationInput(name = "Ęęęęęęęęę")

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isCreated
        }

        @Test
        fun `name where second part starts with a small letter fails validation`() {
            val input = getTestMakeReservationInput(name = "Testerson-son")

            webTestClient.post()
                .uri("/api/booking/book")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(input))
                .exchange()
                .expectStatus().isBadRequest
        }
    }
}