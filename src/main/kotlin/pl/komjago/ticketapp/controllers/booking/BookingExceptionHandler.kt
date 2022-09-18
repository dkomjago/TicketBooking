package pl.komjago.ticketapp.controllers.booking

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import pl.komjago.ticketapp.controllers.ErrorResponse
import pl.komjago.ticketapp.domain.exceptions.*

@RestControllerAdvice(basePackageClasses = [BookingController::class])
class BookingExceptionHandler {

    private val messageNotAvailable = "Message not available."

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleDuplicateSeatException(ex: DuplicateSeatException): ErrorResponse<Int> {
        return ErrorResponse(
            ex.message ?: messageNotAvailable,
            HttpStatus.NOT_FOUND.value(),
            ex.seatId
        )
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleEmptyPlaceBetweenSeatsException(ex: EmptyPlaceBetweenSeatsException): ErrorResponse<Int> {
        return ErrorResponse(
            ex.message ?: messageNotAvailable,
            HttpStatus.NOT_FOUND.value(),
            ex.seatId
        )
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidMovieException(ex: InvalidMovieException): ErrorResponse<Int> {
        return ErrorResponse(
            ex.message ?: messageNotAvailable,
            HttpStatus.NOT_FOUND.value(),
            ex.movieId
        )
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidRoomException(ex: InvalidRoomException): ErrorResponse<Int> {
        return ErrorResponse(
            ex.message ?: messageNotAvailable,
            HttpStatus.NOT_FOUND.value(),
            ex.roomId
        )
    }

    @ExceptionHandler(InvalidScreeningException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidScreeningException(ex: InvalidScreeningException): ErrorResponse<Int> {
        return ErrorResponse(
            ex.message ?: messageNotAvailable,
            HttpStatus.NOT_FOUND.value(),
            ex.screeningId
        )
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidSeatException(ex: InvalidSeatException): ErrorResponse<Int> {
        return ErrorResponse(
            ex.message ?: messageNotAvailable,
            HttpStatus.NOT_FOUND.value(),
            ex.seatId
        )
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleInvalidTicketTypeException(ex: InvalidTicketTypeException): ErrorResponse<Int> {
        return ErrorResponse(
            ex.message ?: messageNotAvailable,
            HttpStatus.NOT_FOUND.value(),
            ex.ticketType
        )
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMovieScreeningMismatchException(ex: MovieScreeningMismatchException): ErrorResponse<Int> {
        return ErrorResponse(
            ex.message ?: messageNotAvailable,
            HttpStatus.NOT_FOUND.value(),
            ex.screeningId
        )
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun handleNoScreeningsException(ex: NoScreeningsException) {}

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleNoSeatSelectedException(ex: NoSeatSelectedException): ErrorResponse<Int> {
        return ErrorResponse(
            ex.message ?: messageNotAvailable,
            HttpStatus.NOT_FOUND.value(),
            ex.seatId
        )
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleSeatAlreadyBookedException(ex: SeatAlreadyBookedException): ErrorResponse<Int> {
        return ErrorResponse(
            ex.message ?: messageNotAvailable,
            HttpStatus.NOT_FOUND.value(),
            ex.seatId
        )
    }
}