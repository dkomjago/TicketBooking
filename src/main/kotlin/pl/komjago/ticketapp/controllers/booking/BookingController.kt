package pl.komjago.ticketapp.controllers.booking

import io.swagger.annotations.ApiParam
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import pl.komjago.ticketapp.controllers.booking.dto.ChooseScreeningOutput
import pl.komjago.ticketapp.controllers.booking.dto.GetScreeningsOutput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationInput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationOutput
import pl.komjago.ticketapp.services.BookingService
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException
import javax.validation.Valid


@RestController
@RequestMapping("/api/booking")
class BookingController(
        private val bookingService: BookingService
) {

    @GetMapping("/screenings")
    fun getScreenings(
            @ApiParam(example = "2016-11-16 06:43")
            @Valid @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")from: LocalDateTime,
            @ApiParam(example = "2016-11-16 06:43")
            @Valid @RequestParam @DateTimeFormat(pattern="yyyy-MM-dd HH:mm")to: LocalDateTime
    ): GetScreeningsOutput {
        try {
            return bookingService.getScreenings(from, to)
        } catch (e: IllegalStateException) {
            throw ResponseStatusException(HttpStatus.NO_CONTENT, e.message, e)
        }
    }

    @GetMapping("/screening/{id}")
    fun chooseScreening(
            @Valid @PathVariable(name = "id") screeningId: Long
    ): ChooseScreeningOutput {
        try {
            return bookingService.chooseScreening(screeningId)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message, e)
        }
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/book")
    fun makeReservation(
            @Valid @RequestBody makeReservationInput: MakeReservationInput
    ): MakeReservationOutput {
        try {
            return bookingService.makeReservation(makeReservationInput)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message, e)
        } catch (e: EntityNotFoundException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message, e)
        }
    }

}