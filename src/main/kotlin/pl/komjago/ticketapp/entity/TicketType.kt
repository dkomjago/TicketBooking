package pl.komjago.ticketapp.entity

import org.hibernate.annotations.Columns
import org.hibernate.annotations.Type
import org.joda.money.Money
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id


@Entity
data class TicketType(
        @Id
        val id: Long,

        @Column(nullable = false)
        var name: String,

        @Columns(columns = [Column(name = "CURRENCY"), Column(name = "AMOUNT")])
        @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmountAndCurrency")
        var price: Money
)