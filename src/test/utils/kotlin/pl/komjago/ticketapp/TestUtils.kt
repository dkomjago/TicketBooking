package pl.komjago.ticketapp

import pl.komjago.ticketapp.controllers.booking.dto.BookedSeatInfo
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationInput
import pl.komjago.ticketapp.controllers.booking.dto.MakeReservationOutput
import pl.komjago.ticketapp.controllers.booking.dto.MovieInfo
import pl.komjago.ticketapp.controllers.booking.dto.RoomInfo
import pl.komjago.ticketapp.controllers.booking.dto.ScreeningInfo
import pl.komjago.ticketapp.controllers.booking.dto.SeatInfo
import pl.komjago.ticketapp.controllers.booking.dto.TicketTypeInfo
import pl.komjago.ticketapp.domain.Movie
import pl.komjago.ticketapp.domain.Reservation
import pl.komjago.ticketapp.domain.Room
import pl.komjago.ticketapp.domain.Screening
import pl.komjago.ticketapp.domain.Seat
import pl.komjago.ticketapp.domain.Ticket
import pl.komjago.ticketapp.domain.TicketType
import pl.komjago.ticketapp.domain.enums.RoomType
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

val defaultDate: LocalDate = LocalDate.of(2032,11,11)
val defaultTime: LocalTime = LocalTime.of(11,11,11)
val defaultDateTime: LocalDateTime = LocalDateTime.of(defaultDate, defaultTime)

fun getTestMovie(
    title: String = "Test Title",
    cast: String? = null,
    director: String? = null,
    description: String? = null,
    durationInMinutes: Short? = null,
    id: Int = 1
) = Movie(title, cast, director, description, durationInMinutes, id)

fun getTestRoom(
    type: RoomType = RoomType.PUBLIC,
    name: String = "Test Room",
    id: Int = 1
) = Room(type, name, id)

fun getTestScreening(
    roomId: Int = 1,
    movieId: Int = 1,
    startingTime: LocalDateTime = defaultDateTime,
    id: Int = 1
) = Screening(roomId, movieId, startingTime, id)

fun getTestScreeningInfo(
    id: Int = 1,
    movieTitle: String = "Test Title",
    seatsLeft: Int = 1,
    startingTime: LocalDateTime = defaultDateTime
) = ScreeningInfo(id, movieTitle, seatsLeft, startingTime)

fun getTestMovieInfo(
    title: String = "Test Title",
    cast: String? = null,
    director: String? = null,
    description: String? = null,
    durationInMinutes: Short? = null
) = MovieInfo(title, cast, director, description, durationInMinutes)

fun getTestRoomInfo(
    name: String = "Test Room",
    seats: List<SeatInfo> = emptyList()
) = RoomInfo(name, seats)

fun getTestSeat(
    roomId: Int = 1,
    row: Char = 'A',
    number: Int = 1,
    id: Int = 1
) = Seat(roomId, row, number, id)

fun getTestTicket(
    reservationId: Int = 1,
    screeningId: Int = 1,
    seatId: Int = 1,
    ticketTypeId: Int = 1,
    id: Int = 1
) = Ticket(reservationId, screeningId, seatId, ticketTypeId, id)

fun getTestTicketType(
    type: String = "Test",
    price: BigDecimal = BigDecimal.ONE,
    id: Int = 1
) = TicketType(type, price, id)

fun getTestTicketTypeInfo(
    id: Int = 1,
    name: String = "Test",
    price: BigDecimal = BigDecimal.ONE,
) = TicketTypeInfo(id, name, price)

fun getTestReservation(
    buyerName: String = "Test Name",
    expirationTime: LocalDateTime = defaultDateTime,
    id: Int = 1
) = Reservation(buyerName, expirationTime, id)

fun getTestMakeReservationInput(
    selectedSeats: List<BookedSeatInfo> = emptyList(),
    screeningId: Int = 1,
    name: String = "Test Name"
) = MakeReservationInput(selectedSeats, screeningId, name)

fun getTestMakeReservationOutput(
   price: BigDecimal = 1.toBigDecimal(),
   expirationTime: LocalDateTime = defaultDateTime,
   ticketIds: List<Int> = emptyList()
) = MakeReservationOutput(price, expirationTime, ticketIds)