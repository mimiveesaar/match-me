package tech.kood.match_me.connections.features.acceptedConnection.domain.internal.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tech.kood.match_me.connections.features.acceptedConnection.domain.internal.AcceptedConnection;

public class AcceptedConnectionValidator implements ConstraintValidator<ValidAcceptedConnection, AcceptedConnection> {
    @Override
    public boolean isValid(AcceptedConnection value, ConstraintValidatorContext context) {
        if (value == null || value.getAcceptedByUser() == null || value.getAcceptedUser() == null) {
            return false;
        }
        if (value.getAcceptedUser().equals(value.getAcceptedByUser())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Accepted by user and accepted user must not be the same")
                    .addPropertyNode("acceptedByUser")
                    .addConstraintViolation();
            context.buildConstraintViolationWithTemplate("Accepted by user and accepted user must not be the same")
                    .addPropertyNode("acceptedUser")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
