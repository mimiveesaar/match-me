package tech.kood.match_me.connections.features.rejectedConnection.domain.internal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RejectedConnectionValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRejectedConnection {
    String message() default "Invalid rejected connection";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}