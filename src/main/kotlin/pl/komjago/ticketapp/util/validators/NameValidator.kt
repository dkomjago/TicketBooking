package pl.komjago.ticketapp.util.validators

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class NameValidator : ConstraintValidator<NameConstraint, String> {
    override fun initialize(nameConstraint: NameConstraint) {}

    override fun isValid(name: String, cxt: ConstraintValidatorContext?): Boolean {
        val nameRegex = Regex("\\p{Lu}\\p{Ll}{2,}")
        return nameRegex.matches(name)
    }
}