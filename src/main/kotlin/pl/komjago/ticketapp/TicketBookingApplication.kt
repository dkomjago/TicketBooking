package pl.komjago.ticketapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TicketBookingApplication

fun main(args: Array<String>) {
    runApplication<TicketBookingApplication>(*args)
}
