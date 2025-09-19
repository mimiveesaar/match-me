package tech.kood.match_me.connections.features.acceptedConnection.domain.internal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AcceptedConnectionValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAcceptedConnection {
    String message() default "Invalid accepted connection";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}