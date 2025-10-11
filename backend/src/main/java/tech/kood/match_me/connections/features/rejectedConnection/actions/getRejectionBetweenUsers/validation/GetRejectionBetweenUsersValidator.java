package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tech.kood.match_me.connections.features.rejectedConnection.actions.GetRejectionBetweenUsers;

public class GetRejectionBetweenUsersValidator implements ConstraintValidator<ValidGetRejectionBetweenUsers, GetRejectionBetweenUsers.Request> {
    @Override
    public boolean isValid(GetRejectionBetweenUsers.Request value, ConstraintValidatorContext context) {

        if (value == null || value.user1() == null || value.user2() == null || value.user1().value() == null || value.user2().value() == null) {
            return false;
        }

        if (value.user1().value().equals(value.user2().value())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Rejected by user and rejected user must not be the same")
                    .addPropertyNode("user1")
                    .addConstraintViolation();
            context.buildConstraintViolationWithTemplate("Rejected by user and rejected user must not be the same")
                    .addPropertyNode("user2")
                    .addConstraintViolation();

            return false;
        }

        return true;
    }
}