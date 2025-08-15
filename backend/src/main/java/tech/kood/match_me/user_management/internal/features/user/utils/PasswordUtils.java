package tech.kood.match_me.user_management.internal.features.user.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import tech.kood.match_me.user_management.internal.features.user.domain.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.internal.features.user.domain.model.password.Password;

public final class PasswordUtils {

    // Checks if the provided plain password matches the hash using the stored salt
    public static boolean matches(Password plainPassword, HashedPassword hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        String hashToCompare = BCrypt.hashpw(plainPassword.getValue(), hashedPassword.getSalt());
        return hashToCompare.equals(hashedPassword.getHash());
    }
}