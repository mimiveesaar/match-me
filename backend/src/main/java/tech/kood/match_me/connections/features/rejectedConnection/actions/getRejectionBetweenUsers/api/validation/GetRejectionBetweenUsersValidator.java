package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.GetRejectionBetweenUsersRequest;

public class GetRejectionBetweenUsersValidator implements ConstraintValidator<ValidGetRejectionBetweenUsers, GetRejectionBetweenUsersRequest> {
    @Override
    public boolean isValid(GetRejectionBetweenUsersRequest value, ConstraintValidatorContext context) {

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
