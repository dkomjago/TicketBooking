package pl.komjago.ticketapp.util.validators

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class SurnameValidator : ConstraintValidator<SurnameConstraint, String> {
    override fun initialize(surnameConstraint: SurnameConstraint) {}

    override fun isValid(surname: String, cxt: ConstraintValidatorContext?): Boolean {
        val surnameRegex = Regex("\\p{Lu}\\p{Ll}{2,}(-\\p{Lu}\\p{Ll}{2,})?")
        return surnameRegex.matches(surname)
    }
}