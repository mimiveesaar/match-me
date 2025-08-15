package tech.kood.match_me.user_management.internal.features.user.domain.model.email;


import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;

@Component
@DomainLayer
@Factory
public class EmailFactory {
    private final Validator validator;

    public EmailFactory(Validator validator) {
        this.validator = validator;
    }

    public Email of(String value) throws ConstraintViolationException {
        var email = new Email(value);
        var validationResult = validator.validate(email);

        if (!validationResult.isEmpty()) {
            throw new ConstraintViolationException(validationResult);
        }

        return email;
    }
}
