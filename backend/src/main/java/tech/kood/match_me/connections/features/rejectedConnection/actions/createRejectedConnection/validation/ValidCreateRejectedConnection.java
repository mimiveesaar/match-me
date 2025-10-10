package tech.kood.match_me.connections.features.rejectedConnection.actions.createRejectedConnection.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CreateRejectedConnectionValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCreateRejectedConnection {
    String message() default "Invalid create rejected connection request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}