package tech.kood.match_me.user_management.features.user.internal.infrastructure.auth;

import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
@Primary
public class PasswordHasherBCryptImpl implements PasswordHasher {

    @Override
    public HashingResult hash(String plaintext) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(plaintext, salt);
        return new HashingResult(hashedPassword, salt);
    }

    @Override
    public boolean verify(String plaintext, String hashedPassword, String salt) {
        var hashedPasswordWithSalt = BCrypt.hashpw(plaintext, salt);
        return hashedPasswordWithSalt.equals(hashedPassword);
    }
}
