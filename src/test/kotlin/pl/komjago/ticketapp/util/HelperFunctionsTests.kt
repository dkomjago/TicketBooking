package pl.komjago.ticketapp.util

import org.joda.money.Money
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal


class HelperFunctionsTests {

    @Test
    fun `toZloty parses double returns Money with PLN currency`() {
        val zloty = toZloty(0.75)
        assertEquals(Money.parse("PLN 0.75"), zloty)
        assertEquals("PLN", zloty.currencyUnit.code)
    }

    @Test
    fun `sumByBigDecimal sums big decimals`() {
        val bigDecimalList = listOf(BigDecimal.TEN, BigDecimal.ONE)
        val bigDecimalSum = bigDecimalList.sumByBigDecimal { it }
        assertEquals(BigDecimal(11), bigDecimalSum)
    }
}