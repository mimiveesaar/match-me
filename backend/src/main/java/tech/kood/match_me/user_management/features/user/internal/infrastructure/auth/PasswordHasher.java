package tech.kood.match_me.user_management.features.user.internal.infrastructure.auth;

import org.springframework.security.crypto.bcrypt.BCrypt;

public interface PasswordHasher {
    HashingResult hash(String plaintext);
    HashingResult hash(String plaintext, String salt);

    record HashingResult(String hash, String salt) {
    }
}