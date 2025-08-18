package tech.kood.match_me.user_management.common.domain.internal.email;

import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;

@Component
@Factory
public final class EmailFactory {
    private final Validator validator;

    public EmailFactory(Validator validator) {
        this.validator = validator;
    }

    public Email create(String value) throws CheckedConstraintViolationException {
        var email =  new Email(value);
        var validationResult = validator.validate(email);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return email;
    }
}
