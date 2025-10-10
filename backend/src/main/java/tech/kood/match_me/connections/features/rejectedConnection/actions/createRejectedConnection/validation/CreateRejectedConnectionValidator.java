package tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreateRejectedConnectionValidator implements ConstraintValidator<ValidCreateRejectedConnection, CreateRejectedConnectionRequest> {

    @Override
    public boolean isValid(CreateRejectedConnectionRequest value, ConstraintValidatorContext context) {

        // Spring runs class level validations before nested validations, why I don't know, sounds pretty dumb.
        if (
                value == null
                        || value.rejectedByUser() == null
                        || value.rejectedUser() == null
                        || value.rejectedUser().value() == null
                        || value.rejectedByUser().value() == null
        ) {
            return false;
        }

        if (value.rejectedByUser().value().equals(value.rejectedUser().value())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Rejected by user and rejected user must not be the same")
                    .addPropertyNode("rejectedByUser")
                    .addConstraintViolation();
            context.buildConstraintViolationWithTemplate("Rejected by user and rejected user must not be the same")
                    .addPropertyNode("rejectedUser")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
