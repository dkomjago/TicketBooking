package pl.komjago.ticketapp.services

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import pl.komjago.ticketapp.controllers.booking.dto.ChooseScreeningOutput
import pl.komjago.ticketapp.controllers.booking.dto.MovieInfo
import pl.komjago.ticketapp.controllers.booking.dto.RoomInfo
import pl.komjago.ticketapp.controllers.booking.dto.ScreeningInfo
import pl.komjago.ticketapp.controllers.booking.dto.SeatInfo
import pl.komjago.ticketapp.controllers.booking.dto.TicketTypeInfo
import pl.komjago.ticketapp.domain.exceptions.InvalidMovieException
import pl.komjago.ticketapp.domain.exceptions.InvalidRoomException
import pl.komjago.ticketapp.domain.exceptions.InvalidScreeningException
import pl.komjago.ticketapp.domain.exceptions.MovieScreeningMismatchException
import pl.komjago.ticketapp.domain.exceptions.NoScreeningsException
import pl.komjago.ticketapp.repositories.MovieRepository
import pl.komjago.ticketapp.repositories.RoomRepository
import pl.komjago.ticketapp.repositories.ScreeningRepository
import pl.komjago.ticketapp.repositories.SeatRepository
import pl.komjago.ticketapp.repositories.TicketRepository
import pl.komjago.ticketapp.repositories.TicketTypeRepository
import java.time.LocalDateTime

@Service
class MovieScreeningService(
    private val movieRepository: MovieRepository,
    private val roomRepository: RoomRepository,
    private val ticketRepository: TicketRepository,
    private val seatRepository: SeatRepository,
    private val ticketTypeRepository: TicketTypeRepository,
    private val screeningRepository: ScreeningRepository
) : ScreeningService {

    override fun getScreenings(from: LocalDateTime, to: LocalDateTime): Flow<ScreeningInfo> {
        val screenings = screeningRepository.findAllByStartingTimeBetween(from, to)
        val movies = movieRepository.findAllById(screenings.map { it.movieId })
        val rooms = roomRepository.findAllById(screenings.map { it.roomId })
        val roomSeats = seatRepository.findAllById(screenings.map { it.roomId })

        return screenings.onEmpty { throw NoScreeningsException(from, to) }.map { screening ->
            val movie = movies.firstOrNull { it.id == screening.movieId }
                ?: throw MovieScreeningMismatchException(screening.id)
            val room = rooms.firstOrNull { it.id == screening.roomId }
            val seats = roomSeats.filter { it.roomId == room?.id }

            ScreeningInfo(
                screening.id,
                movie.title,
                seats.count() - ticketRepository.countAllByScreeningId(screening.id),
                screening.startingTime
            )
        }
    }

    override suspend fun chooseScreening(screeningId: Int): ChooseScreeningOutput {
        val screening =
            screeningRepository.findById(screeningId) ?: throw InvalidScreeningException(screeningId)

        val movie = movieRepository.findById(screening.movieId)
        val room = roomRepository.findById(screening.roomId) ?: throw InvalidRoomException(screening.roomId)
        val roomSeats = seatRepository.findAllById(flow { room.id })
        val bookedSeatIds = ticketRepository.findAllByScreeningId(screeningId).map { it.seatId }
        val bookedSeats = seatRepository.findAllById(bookedSeatIds)

        val movieInfo = movie?.let {
            MovieInfo(it.title, it.cast, it.director, it.description, it.durationInMinutes)
        } ?: throw InvalidMovieException(screening.movieId)

        val roomInfo = RoomInfo(
            room.name, bookedSeats.map { seat ->
                SeatInfo(seat.id, seat.row, seat.number, roomSeats.firstOrNull { seat.id == it.id } != null)
            }.toList()
        )

        val ticketTypeInfoList = ticketTypeRepository.findAll().map { TicketTypeInfo(it.id, it.type, it.price) }

        return ChooseScreeningOutput(
            screening.id, movieInfo, roomInfo, screening.startingTime, ticketTypeInfoList.toList()
        )
    }
}