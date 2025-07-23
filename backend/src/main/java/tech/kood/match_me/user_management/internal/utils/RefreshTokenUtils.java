package tech.kood.match_me.user_management.internal.utils;

public final class RefreshTokenUtils {

    public static String generateToken() {
        // Logic to generate a secure random token
        return java.util.UUID.randomUUID().toString();
    }
}