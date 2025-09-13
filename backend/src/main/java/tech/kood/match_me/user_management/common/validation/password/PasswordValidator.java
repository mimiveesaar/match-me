package tech.kood.match_me.user_management.common.validation.password;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {


    @Value("${user-management.user.password.min-length}")
    private int minLength;
    @Value("${user-management.user.password.max-length}")
    private int maxLength;
    @Value("${user-management.user.password.require-uppercase}")
    private boolean requireUpperCase;
    @Value("${user-management.user.password.require-special}")
    private boolean requireSpecial;


    @PostConstruct
    private void validateConfiguration() {
        if (minLength < 1) {
            throw new IllegalArgumentException("minLength must be greater than 0");
        }

        if (maxLength < minLength) {
            throw new IllegalArgumentException("maxLength must be greater than or equal to minLength");
        }

        if (maxLength > 72) {
            throw new IllegalArgumentException("maxLength must be less than or equal to 72 due to BCrypt limitations");
        }
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return false;
        }

        if (password.length() <= minLength || password.length() >= maxLength) {
            return false;
        }

        boolean hasUpper = password.chars().anyMatch(Character::isUpperCase);
        if (requireUpperCase && !hasUpper) {
            return false;
        }

        boolean hasSpecial = password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));
        if (requireSpecial && !hasSpecial) {
            return false;
        }

        return true;
    }
}