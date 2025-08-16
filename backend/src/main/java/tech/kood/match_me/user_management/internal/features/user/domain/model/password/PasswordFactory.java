package tech.kood.match_me.user_management.internal.features.user.domain.model.password;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;

@Component
@DomainLayer
@Factory
public final class PasswordFactory {

    private final Validator validator;

    public PasswordFactory(Validator validator) {
        this.validator = validator;
    }

    public Password make(String value) throws CheckedConstraintViolationException {
        var password = new Password(value);
        var validationResult = validator.validate(password);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return password;
    }
}
