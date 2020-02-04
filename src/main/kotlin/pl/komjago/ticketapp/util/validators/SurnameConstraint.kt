package pl.komjago.ticketapp.util.validators

import javax.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [SurnameValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class SurnameConstraint(
        val message: String = "Invalid surname",
        val groups: Array<KClass<out Any>> = [],
        val payload: Array<KClass<out Any>> = []
)