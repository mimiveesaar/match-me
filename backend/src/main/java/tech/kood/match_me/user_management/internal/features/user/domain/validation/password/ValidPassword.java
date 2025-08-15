package tech.kood.match_me.user_management.internal.features.user.domain.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface ValidPassword {
    String message() default "Password does not meet security requirements.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
