package tech.kood.match_me.user_management.internal.features.user.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPassword;

public final class PasswordUtils {
    // Encodes a password, generating a salt using bcrypt and returning a
    // HashedPassword
    public static HashedPassword encode(String plainPassword) {
        return HashedPassword.of(hash, salt);
    }

    // Checks if the provided plain password matches the hash using the stored salt
    public static boolean matches(String plainPassword, HashedPassword hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        // BCrypt stores the salt in the hash, but for explicit salt usage:
        String hashToCompare = BCrypt.hashpw(plainPassword, hashedPassword.getSalt());
        return hashToCompare.equals(hashedPassword.getHash());
    }
}
