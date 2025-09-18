package tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.CreateAcceptedConnectionRequest;

public class CreateAcceptedConnectionValidator implements ConstraintValidator<ValidCreateAcceptedConnection, CreateAcceptedConnectionRequest> {

    @Override
    public boolean isValid(CreateAcceptedConnectionRequest value, ConstraintValidatorContext context) {

        if (
                value == null
                        || value.acceptedByUser() == null
                        || value.acceptedUser() == null
                        || value.acceptedUser().value() == null
                        || value.acceptedByUser().value() == null
        ) {
            return false;
        }

        if (value.acceptedUser().value().equals(value.acceptedByUser().value())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Accepted by user and accepted user must not be the same")
                    .addPropertyNode("acceptedByUserId")
                    .addConstraintViolation();
            context.buildConstraintViolationWithTemplate("Accepted by user and accepted user must not be the same")
                    .addPropertyNode("acceptedUserId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
