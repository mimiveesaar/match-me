package tech.kood.match_me.user_management.internal.domain.models;

public record HashedPassword(String hash, String salt) {
    
    public HashedPassword {
        if (hash == null || hash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be null or blank");
        }
        if (salt == null || salt.isBlank()) {
            throw new IllegalArgumentException("Password salt cannot be null or blank");
        }
    }

    public HashedPassword hash(String newHash) {
        return new HashedPassword(newHash, this.salt);
    }
    public HashedPassword salt(String newSalt) {
        return new HashedPassword(this.hash, newSalt);
    }
}
