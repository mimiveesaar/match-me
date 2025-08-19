package tech.kood.match_me.user_management.features.accessToken.domain.internal.model;

import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.user_management.common.domain.internal.userId.UserId;
import tech.kood.match_me.user_management.common.exceptions.CheckedConstraintViolationException;


@Factory
@Component
public class AccessTokenFactory {

    private final Validator validator;

    public AccessTokenFactory(Validator validator) {
        this.validator = validator;
    }

    public AccessToken create(String jwt, UserId userId) throws CheckedConstraintViolationException {
        var accessToken = new AccessToken(jwt, userId);
        var validationResult = validator.validate(accessToken);

        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return accessToken;
    }
}
