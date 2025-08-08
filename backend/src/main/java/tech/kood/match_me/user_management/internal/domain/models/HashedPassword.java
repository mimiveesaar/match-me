package tech.kood.match_me.user_management.internal.domain.models;

import java.util.Objects;
import jakarta.validation.constraints.NotBlank;
import tech.kood.match_me.user_management.internal.common.validation.DomainObjectInputValidator;

/**
 * Represents a hashed password along with its associated salt.
 * <p>
 * This class is immutable and ensures that both the hash and salt are non-null and non-blank.
 * </p>
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 * HashedPassword hp = new HashedPassword("hashedValue", "saltValue");
 * </pre>
 * </p>
 *
 * @author
 */
public final class HashedPassword {


    @NotBlank
    public final String hash;

    @NotBlank
    public final String salt;

    private HashedPassword(String hash, String salt) {
        this.hash = hash;
        this.salt = salt;
    }

    /**
     * Static factory method to create a HashedPassword instance with validation.
     * 
     * @param hash the hashed password (must not be null or blank)
     * @param salt the salt (must not be null or blank)
     * @return a new HashedPassword instance
     * @throws IllegalArgumentException if hash or salt are invalid
     */
    public static HashedPassword of(String hash, String salt) {
        var hashedPassword = new HashedPassword(hash, salt);
        var violations = DomainObjectInputValidator.instance.validate(hashedPassword);

        if (!violations.isEmpty()) {
            throw new IllegalArgumentException("Invalid HashedPassword: " + violations);
        }

        return hashedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HashedPassword))
            return false;
        HashedPassword that = (HashedPassword) o;
        return hash.equals(that.hash) && salt.equals(that.salt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, salt);
    }

    @Override
    public String toString() {
        return "HashedPassword{" + "hash='" + hash + '\'' + ", salt='" + salt + '\'' + '}';
    }
}
