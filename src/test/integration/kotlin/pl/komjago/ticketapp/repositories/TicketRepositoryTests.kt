package pl.komjago.ticketapp.repositories


import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import pl.komjago.ticketapp.entity.*
import pl.komjago.ticketapp.repository.TicketRepository
import java.time.LocalDateTime

@DataJpaTest
class TicketRepositoryTests @Autowired constructor(private val ticketRepository: TicketRepository, private val testEntityManager: TestEntityManager) {

    private val testMovieTitle1 = "TEST 1"
    private val testMovieTitle2 = "TEST 2"

    private val ticketCount = 2
    private val seatCount = 2

    @BeforeEach
    fun setUp() {
        val movie = Movie(null,
                testMovieTitle1,
                "TESTCAST 1",
                "TESTDIRECTOR 1",
                "Shrek is love",
                144
        )
        testEntityManager.persist(movie)

        val movie2 = Movie(null,
                testMovieTitle2,
                "TESTCAST 1",
                "TESTDIRECTOR 2",
                "Shrek is life",
                144
        )
        testEntityManager.persist(movie2)

        val room = testEntityManager.persist(
                Room(null,
                        "testroom",
                        List(seatCount) {
                            testEntityManager.persist(Seat(null, 1, 1)
                            )
                        },
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

        val ticketType = testEntityManager.persist(TicketType(1, "test", 0.5.toBigDecimal()))

        val ticketList = List(ticketCount) {
            Ticket(null,
                    screeningList[0],
                    screeningList[0].room.seats[it],
                    ticketType
            )
        }
        ticketList.forEach { testEntityManager.persist(it) }
    }

    @Test
    fun `count all tickets by screening id`() {
        val ticketList = ticketRepository.findAll()
        //should contain elements
        assertFalse(ticketList.isEmpty())

        assertEquals(ticketRepository.countAllByScreeningId(ticketList[0].screening.id!!), ticketCount)
    }


}