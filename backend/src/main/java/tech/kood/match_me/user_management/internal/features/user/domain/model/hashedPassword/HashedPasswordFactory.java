package tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.internal.features.user.domain.model.password.Password;

@Component
@DomainLayer
@Factory
public final class HashedPasswordFactory {

    private final Validator validator;

    public HashedPasswordFactory(Validator validator) {
        this.validator = validator;
    }

    public HashedPassword of(String hash, String salt) throws ConstraintViolationException {
        var hashedPassword = new HashedPassword(hash, salt);
        var validationResult = validator.validate(hashedPassword);

        if (!validationResult.isEmpty()) {
            throw new ConstraintViolationException(validationResult);
        }

        return hashedPassword;
    }

     public HashedPassword of(Password password) throws ConstraintViolationException {
        String salt = BCrypt.gensalt();
        String hash = BCrypt.hashpw(password.getValue(), salt);

        return this.of(hash, salt);
    }
}