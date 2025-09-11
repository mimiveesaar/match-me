package tech.kood.match_me.connections.features.acceptedConnection.actions.createConnection.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CreateAcceptedConnectionValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCreateAcceptedConnection {
    String message() default "Invalid create accepted connection request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}