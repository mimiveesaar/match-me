package tech.kood.match_me.user_management.features.user.domain.internal.model.password;

import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;

@Component
@Factory
public class PasswordFactory {
    private final Validator validator;

    public PasswordFactory(Validator validator) {
        this.validator = validator;
    }

    public Password create(String value) throws CheckedConstraintViolationException {
        var password = new Password(value);
        var validationResult = validator.validate(password);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return password;
    }
}
