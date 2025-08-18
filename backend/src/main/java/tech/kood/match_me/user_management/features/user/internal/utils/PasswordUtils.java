package tech.kood.match_me.user_management.features.user.internal.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;
import tech.kood.match_me.user_management.features.user.domain.internal.model.hashedPassword.HashedPassword;
import tech.kood.match_me.user_management.common.domain.internal.password.Password;

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