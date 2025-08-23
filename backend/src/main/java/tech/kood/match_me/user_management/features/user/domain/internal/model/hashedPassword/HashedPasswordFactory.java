package tech.kood.match_me.user_management.features.user.domain.internal.model.hashedPassword;

import jakarta.validation.Validator;
import org.jmolecules.ddd.annotation.Factory;
import org.springframework.stereotype.Component;
import tech.kood.match_me.common.exceptions.CheckedConstraintViolationException;
import tech.kood.match_me.user_management.common.domain.internal.password.Password;
import tech.kood.match_me.user_management.features.user.internal.infrastructure.auth.PasswordHasher;

@Factory
@Component
public class HashedPasswordFactory {

    private final Validator validator;
    private final PasswordHasher passwordHasher;

    public HashedPasswordFactory(Validator validator, PasswordHasher passwordHasher) {
        this.validator = validator;
        this.passwordHasher = passwordHasher;
    }

    public HashedPassword create(String hash, String salt) throws CheckedConstraintViolationException {
        var hashedPassword = new HashedPassword(hash, salt);

        var validationResult = validator.validate(hashedPassword);
        if (!validationResult.isEmpty()) {
            throw new CheckedConstraintViolationException(validationResult);
        }

        return hashedPassword;
    }

    /**
     * Hashes a plain text password using BCrypt.
     * <p>
     * <b>Note:</b> The provided {@link Password} must be limited to 72 ASCII characters.
     * BCrypt only considers the first 72 bytes of the password and ignores any additional characters.
     * Non-ASCII characters may be treated inconsistently by BCrypt implementations.
     * </p>
     *
     * @param password the plain text password to hash (max 72 ASCII chars)
     * @return a new {@link HashedPassword} containing the hash and salt
     */
    public HashedPassword fromPlainText(Password password) {
        var hashedPassword = passwordHasher.hash(password.toString());
        return new HashedPassword(hashedPassword.hash(), hashedPassword.salt());
    }

    public HashedPassword fromPlainText(Password password, String salt) {
        var hashedPassword = passwordHasher.hash(password.toString(), salt);
        return new HashedPassword(hashedPassword.hash(), hashedPassword.salt());
    }
}
