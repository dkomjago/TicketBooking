package pl.komjago.ticketapp.repositories

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import pl.komjago.ticketapp.entity.Movie
import pl.komjago.ticketapp.entity.Room
import pl.komjago.ticketapp.entity.Screening
import pl.komjago.ticketapp.entity.Seat
import pl.komjago.ticketapp.repository.ScreeningRepository
import java.time.LocalDateTime


@DataJpaTest
class ScreeningRepositoryTests @Autowired constructor(private val screeningRepository: ScreeningRepository, private val testEntityManager: TestEntityManager) {

    private val testMovieTitle1 = "TEST 1"
    private val testMovieTitle2 = "TEST 2"

    @BeforeEach
    fun setUp() {
        val movie = Movie(null,
                testMovieTitle1,
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        val movie2 = Movie(null,
                testMovieTitle2,
                "TESTCAST 1",
                "TESTDIRECTOR 2",
                "Shrek is life",
                144
        )
        testEntityManager.persist(movie)
        testEntityManager.persist(movie2)
        val room = testEntityManager.persist(
                Room(null,
                        "testroom",
                        listOf(testEntityManager.persist(
                                Seat(null, 1, 1)
                        )),
                        1)
        )
        val screeningList = listOf(
                Screening(null,
                        movie,
                        room,
                        LocalDateTime.now()
                ),
                Screening(null,
                        movie2,
                        room,
                        LocalDateTime.now().plusDays(1)
                ))
        screeningList.forEach { testEntityManager.persist(it) }
    }

    @Test
    fun `find all movies between given LocalDateTimes`() {
        var screeningList = screeningRepository.findAll()
        //should contain elements
        assertFalse(screeningList.isEmpty())

        screeningList = screeningRepository.findAllByStartingTimeBetween(
                LocalDateTime.now().plusHours(1),
                LocalDateTime.now().plusDays(2))

        //shouldn't contain 2nd screening
        assertFalse(screeningList.any { it.movie.title == testMovieTitle1 })

        screeningList = screeningRepository.findAllByStartingTimeBetween(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(2))

        //should contain both screenings
        assert(screeningList.any { it.movie.title == testMovieTitle1 } &&
                screeningList.any { it.movie.title == testMovieTitle2 })
    }
}