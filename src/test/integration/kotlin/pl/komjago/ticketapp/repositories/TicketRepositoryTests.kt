package pl.komjago.ticketapp.repositories


import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles

@ExperimentalCoroutinesApi
@DataR2dbcTest
@ActiveProfiles("test")
class TicketRepositoryTests(
    @Autowired private val ticketRepository: TicketRepository
) {

    @Nested
    inner class FindAllByScreeningId {
        @Test
        fun `returns elements`() = runTest {
            val ticketList = ticketRepository.findAllByScreeningId(1).toList()

            assertTrue(ticketList.isNotEmpty())
        }
    }

    @Nested
    inner class CountAllByScreeningId {
        @Test
        fun `accurately counts elements`() = runTest {
            val expected = ticketRepository.findAllByScreeningId(1).toList().size

            val result = ticketRepository.countAllByScreeningId(1)

            assertEquals(expected, result)
        }
    }
}