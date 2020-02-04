package pl.komjago.ticketapp.controllers.booking

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import pl.komjago.ticketapp.controllers.booking.dto.ChooseScreeningOutput
import pl.komjago.ticketapp.controllers.booking.dto.GetScreeningsInput
import pl.komjago.ticketapp.controllers.booking.dto.GetScreeningsOutput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationInput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationOutput
import pl.komjago.ticketapp.services.BookingService
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import javax.persistence.EntityNotFoundException
import javax.validation.Valid


@RestController
@RequestMapping("/api/booking")
class BookingController(private val bookingService: BookingService) {

    @PostMapping("/screenings")
    fun getScreenings(@Valid @RequestBody getScreeningsInput: GetScreeningsInput
    ): GetScreeningsOutput {
        try {
            return bookingService.getScreenings(getScreeningsInput)
        } catch (e: IllegalStateException) {
            throw ResponseStatusException(HttpStatus.NO_CONTENT, e.message, e)
        }
    }

    @GetMapping("/screening/{id}")
    fun chooseScreening(@Valid @PathVariable(name = "id") screeningId: Long
    ): ChooseScreeningOutput {
        try {
            return bookingService.chooseScreening(screeningId)
        } catch (e: IllegalArgumentException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, e.message, e)
        }
    }

    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping("/book")
    fun makeReservation(@Valid @RequestBody makeReservationInput: MakeReservationInput
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