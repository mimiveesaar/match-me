package tech.kood.match_me.user_management.features.user.internal.infrastructure.auth;

public interface PasswordHasher {
    HashingResult hash(String plaintext);
    boolean verify(String plaintext, String hashedPassword, String salt);

    record HashingResult(String hash, String salt) {
    }
}