package pl.komjago.ticketapp.configuration

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import pl.komjago.ticketapp.entity.Movie
import pl.komjago.ticketapp.entity.Room
import pl.komjago.ticketapp.entity.Screening
import pl.komjago.ticketapp.entity.Seat
import pl.komjago.ticketapp.entity.TicketType
import pl.komjago.ticketapp.repository.MovieRepository
import pl.komjago.ticketapp.repository.RoomRepository
import pl.komjago.ticketapp.repository.ScreeningRepository
import pl.komjago.ticketapp.repository.TicketTypeRepository
import java.time.LocalDateTime
import java.util.*


@Component
@Profile("!test")
class DataLoader(
        private val screeningRepository: ScreeningRepository,
        private val movieRepository: MovieRepository,
        private val ticketTypeRepository: TicketTypeRepository,
        private val roomRepository: RoomRepository) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val rowLength = 3

        val seatList: List<Seat> = List(6) {
            Seat(null, it / rowLength + 1, it % rowLength + 1)
        }

        val savedRoomList = roomRepository.saveAll(List(3) { index ->
            Room(null, "ScreeningRoom $index", seatList.map { it.copy() }, seatList.count())
        })

        ticketTypeRepository.saveAll(listOf(
                TicketType(0L, "adult", 25.toBigDecimal()),
                TicketType(1L, "student", 18.toBigDecimal()),
                TicketType(2L, "child", 12.5.toBigDecimal())
        ))

        val savedMovieList = movieRepository.saveAll(listOf(
                Movie(null,
                        "Star Wars: Episode IV",
                        "Mark Hamill 1",
                        "George Lucas 1",
                        "Luke Skywalker joins 1",
                        121
                ),
                Movie(null,
                        "Star Wars: Episode V",
                        "Mark Hamill 2",
                        "George Lucas 2",
                        "Luke Skywalker joins 2",
                        122
                ),
                Movie(null,
                        "Star Wars: Episode VI",
                        "Mark Hamill 3",
                        "George Lucas 3",
                        "Luke Skywalker joins 3",
                        123
                )
        ))


        val screeningList = mutableListOf<Screening>()
        savedRoomList.forEachIndexed { i, room ->
            screeningList.addAll(listOf(
                    Screening(null,
                            savedMovieList[i],
                            room,
                            LocalDateTime.now().plusMinutes(savedMovieList[i].durationInMinutes + Random().nextInt(1337))
                    ),
                    Screening(null,
                            savedMovieList[(i + 1) % savedMovieList.count()],
                            room,
                            LocalDateTime.now().plusMinutes(savedMovieList[(i + 1) % savedMovieList.count()].durationInMinutes + Random().nextInt(80085)))
            ))
        }
        screeningRepository.saveAll(screeningList)
    }
}