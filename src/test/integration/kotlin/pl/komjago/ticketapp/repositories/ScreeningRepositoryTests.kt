package pl.komjago.ticketapp.repositories

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder
import org.hamcrest.core.IsNot.not
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
@DataR2dbcTest
@ActiveProfiles("test")
class ScreeningRepositoryTests(
    @Autowired private val screeningRepository: ScreeningRepository
) {

    @Nested
    inner class FindAll {
        @Test
        fun `returns elements`() = runTest {
            val screeningList = screeningRepository.findAll().toList()

            assertFalse(screeningList.isEmpty())
        }
    }

    @Nested
    inner class FindAllByStartingTimeBetween {
        @Test
        fun `2 day interval returns limited screenings subset`() = runTest {
            val intervalList = screeningRepository.findAllByStartingTimeBetween(
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(2)
            ).toList()

            val fullScreeningList = screeningRepository.findAll().toList()

            assertThat(intervalList, not(containsInAnyOrder(*fullScreeningList.toTypedArray())))
        }

        @Test
        fun `unlimited interval returns all screenings`() = runTest {
            val intervalList = screeningRepository.findAllByStartingTimeBetween(
                LocalDateTime.MIN,
                LocalDateTime.MAX
            ).toList()

            val fullScreeningList = screeningRepository.findAll().toList()

            assertThat(intervalList, containsInAnyOrder(*fullScreeningList.toTypedArray()))
        }
    }
}