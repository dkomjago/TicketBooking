package pl.komjago.ticketapp.util.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.joda.money.Money

class MoneySerializer : StdSerializer<Money>(Money::class.java) {
    override fun serialize(value: Money, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeString("${value.amount} ${value.currencyUnit}")
    }
}
