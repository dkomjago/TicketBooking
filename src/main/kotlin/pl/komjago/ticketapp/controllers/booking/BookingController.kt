package pl.komjago.ticketapp.controllers.booking

import io.swagger.v3.oas.annotations.Parameter
import kotlinx.coroutines.flow.Flow
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.komjago.ticketapp.controllers.booking.dto.ChooseScreeningOutput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationInput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationOutput
import pl.komjago.ticketapp.controllers.booking.dto.ScreeningInfo
import pl.komjago.ticketapp.services.BookingService
import pl.komjago.ticketapp.services.ScreeningService
import java.time.LocalDateTime
import javax.validation.Valid

@RestController
@RequestMapping("/api/booking")
class BookingController(
    private val bookingService: BookingService,
    private val screeningService: ScreeningService
) {

    @GetMapping("/screenings")
    fun getScreenings(
        @Parameter(example = "2016-11-16 06:43") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") from: LocalDateTime,
        @Parameter(example = "2016-11-16 06:43") @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") to: LocalDateTime
    ): Flow<ScreeningInfo> {
        return screeningService.getScreenings(from, to)
    }

    @GetMapping("/screening/{id}")
    suspend fun chooseScreening(
        @PathVariable(name = "id") screeningId: Int
    ): ChooseScreeningOutput {
        return screeningService.chooseScreening(screeningId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/book")
    suspend fun makeReservation(
        @RequestBody @Valid makeReservationInput: MakeReservationInput
    ): MakeReservationOutput {
        return bookingService.makeReservation(makeReservationInput)
    }

}