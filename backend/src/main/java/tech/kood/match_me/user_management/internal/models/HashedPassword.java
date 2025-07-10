package tech.kood.match_me.user_management.internal.models;

public record HashedPassword(String password_hash, String password_salt) {
    
    public HashedPassword {
        if (password_hash == null || password_hash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be null or blank");
        }
        if (password_salt == null || password_salt.isBlank()) {
            throw new IllegalArgumentException("Password salt cannot be null or blank");
        }
    }

}
