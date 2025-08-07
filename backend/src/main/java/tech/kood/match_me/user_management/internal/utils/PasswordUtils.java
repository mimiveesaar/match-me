package tech.kood.match_me.user_management.internal.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import tech.kood.match_me.user_management.internal.domain.models.HashedPassword;

public final class PasswordUtils {
    // Encodes a password, generating a salt using bcrypt and returning a
    // HashedPassword
    public static HashedPassword encode(String plainPassword) {
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        String salt = BCrypt.gensalt();
        String hash = BCrypt.hashpw(plainPassword, salt);
        return new HashedPassword(hash, salt);
    }

    // Checks if the provided plain password matches the hash using the stored salt
    public static boolean matches(String plainPassword, HashedPassword hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        // BCrypt stores the salt in the hash, but for explicit salt usage:
        String hashToCompare = BCrypt.hashpw(plainPassword, hashedPassword.salt());
        return hashToCompare.equals(hashedPassword.hash());
    }
}
