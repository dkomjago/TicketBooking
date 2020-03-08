package pl.komjago.ticketapp.util

import org.joda.money.Money
import java.math.BigDecimal

fun toZloty(amount: Double): Money {
    return Money.parse("PLN $amount")
}

fun toZloty(amount: BigDecimal): Money {
    return Money.parse("PLN $amount")
}

fun <T> Iterable<T>.sumByBigDecimal(transform: (T) -> BigDecimal): BigDecimal {
    return this.fold(BigDecimal.ZERO) { acc, e -> acc + transform.invoke(e) }
}