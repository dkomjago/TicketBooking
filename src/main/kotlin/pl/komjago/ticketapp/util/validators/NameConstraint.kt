package pl.komjago.ticketapp.util.validators

import javax.validation.Constraint
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [NameValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class NameConstraint(
        val message: String = "Invalid name",
        val groups: Array<KClass<out Any>> = [],
        val payload: Array<KClass<out Any>> = []
)