package tech.kood.match_me.connections.features.rejectedConnection.domain.internal.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tech.kood.match_me.connections.features.rejectedConnection.domain.internal.RejectedConnection;

public class RejectedConnectionValidator implements ConstraintValidator<ValidRejectedConnection, RejectedConnection> {
    @Override
    public boolean isValid(RejectedConnection value, ConstraintValidatorContext context) {
        if (value == null || value.getRejectedUser() == null || value.getRejectedByUser() == null) {
            return false;
        }
        if (value.getRejectedByUser().equals(value.getRejectedUser())) {
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
