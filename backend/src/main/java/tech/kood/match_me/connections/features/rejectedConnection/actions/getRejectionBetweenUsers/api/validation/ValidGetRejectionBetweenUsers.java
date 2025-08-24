package tech.kood.match_me.connections.features.rejectedConnection.actions.getRejectionBetweenUsers.api.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GetRejectionBetweenUsersValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGetRejectionBetweenUsers {
    String message() default "Invalid get rejection between users.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
