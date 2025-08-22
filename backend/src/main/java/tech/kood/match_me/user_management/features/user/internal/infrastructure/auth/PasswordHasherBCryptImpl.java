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
        return hash(plaintext, salt);
    }

    @Override
    public HashingResult hash(String plaintext, String salt) {
        String hashedPassword = BCrypt.hashpw(plaintext, salt);
        return new HashingResult(hashedPassword, salt);
    }
}
